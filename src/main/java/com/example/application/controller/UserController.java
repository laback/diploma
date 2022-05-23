package com.example.application.controller;


import com.example.application.model.User;
import com.example.application.service.RoleService;
import com.example.application.service.UserService;
import com.example.application.viewModel.ChangePasswordViewModel;
import com.example.application.viewModel.ResetPasswordViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //Возращает форму для авторизации
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginError", false);
        return "users/login";
    }

    //Возвращает форму, если пользователя не существует
    @GetMapping("/loginFailed")
    public String failedLogin(Model model){
        model.addAttribute("loginError", true);
        return "users/login";
    }

    //Возвращает форму для регистрации пользователя
    @GetMapping("admin/registration")
    public String registration(@ModelAttribute("userForm") User user, Model model){
        model.addAttribute("employeeRoleId", roleService.getEmployeeRoleId());
        model.addAttribute("customerRoleId", roleService.getCustomerRoleId());

        return "users/registration";

    }

    //Добавляет пользователя в базу
    @PostMapping("admin/registration")
    public String addUser(@ModelAttribute("userForm") User user) throws MessagingException {

        userService.add(new User(user));

        return "redirect:/";
    }

    //Возвращает форму для смены пароля
    @GetMapping("/changePassword")
    public String showChangePasswordForm(@ModelAttribute("changePasswordViewModel") ChangePasswordViewModel changePasswordViewModel){


        return "users/passwordChange";
    }

    //Изменяет пароль
    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("changePasswordViewModel") ChangePasswordViewModel changePasswordViewModel){

        userService.updatePassword(changePasswordViewModel);

        return "status/passwordChanged";
    }

    //Форма для отправки сообщения на почту, если пользователь хочет восстановить пароль
    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model){
        model.addAttribute("userExists", true);

        model.addAttribute("email", "");

        return "users/forgotPassword";
    }

    //Форма, если пользователя с такой почтой не существует
    @GetMapping("/resetPasswordError")
    public String resetPasswordFormError(Model model){
        model.addAttribute("userExists", false);

        model.addAttribute("email", "");

        return "users/forgotPassword";
    }

    //Форма для смены пароля при восстановлении
    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute(name = "email") String email, Model model) throws MessagingException {
        User user = userService.getUserByEmail(email);

        if(user == null){
            return "redirect:/resetPasswordError";
        }

        String token = UUID.randomUUID().toString();

        userService.createPasswordResetTokenForUser(user, token);

        userService.sendResetPasswordEmail(token, user);

        model.addAttribute("resetPasswordViewModel", new ResetPasswordViewModel());

        return "users/resetPassword";
    }

    //Смена пароля, если токен валиден
    @PostMapping("/resetPasswordAfterForget")
    public String updatePassword(@ModelAttribute(name = "resetPasswordAfterForget") ResetPasswordViewModel resetPasswordViewModel){

        if(userService.isTokenExpired(resetPasswordViewModel.getToken())){
            return "errors/expiredToken";
        }

        userService.setNewPassword(resetPasswordViewModel);

        return "redirect:/";
    }

    //Возвращает всех пользователей
    @GetMapping("/admin/userList")
    public String getAllUsers(Model model){
        model.addAttribute("users", userService.getAllWithoutCurrent());

        return "users/userList";
    }

    //Форма для изменения данных пользователя
    @GetMapping("/admin/user/{id}")
    public String showUpdateUserForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.getById(id));

        return "users/update";
    }

    //Обновляет пользователя
    @PutMapping("/admin/user/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id){

        User updatingUser = userService.getById(id);

        updatingUser.setFirstName(user.getFirstName());
        updatingUser.setLastName(user.getLastName());
        updatingUser.setSurName(user.getSurName());

        userService.update(updatingUser);

        return "redirect:/admin/userList";
    }

    //Удаляет пользователя
    @DeleteMapping("/admin/user/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.delete(id);

        return "redirect:/admin/userList";
    }
}
