package com.by.gomel.gstu.service;

import com.by.gomel.gstu.email.EmailSender;
import com.by.gomel.gstu.model.PasswordResetToken;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.repository.PasswordResetTokenRepository;
import com.by.gomel.gstu.repository.UserRepository;
import com.by.gomel.gstu.viewModel.ChangePasswordViewModel;
import com.by.gomel.gstu.viewModel.ResetPasswordViewModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    PasswordResetTokenRepository resetTokenRepository;
    RoleService roleService;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    EmailSender sender;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordResetTokenRepository passwordResetTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailSender sender) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.resetTokenRepository = passwordResetTokenRepository;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        this.sender = sender;
    }

    //Проверяет, есть ли пользователь с таким логином
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return user;
    }

    //Возвращает всех пользователей
    public List<User> getAll(){
        return userRepository.findAll();
    }


    //Возвращает всех пользователей, без авторизированного
    public List<User> getAllWithoutCurrent(){

        User user = (User) loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        return userRepository.findAll().stream().filter(u -> u.getId() != user.getId()).collect(Collectors.toList());

    }

    //Возвращает пользователя по id
    public User getById(Long id){
        return userRepository.findById(id).orElseThrow();
    }


    //Возвращает пользователя по почте
    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElse(null);
    }

    //Добавляет пользователя
    public boolean add(User user) throws MessagingException {

        boolean result = false;

        if(isEmailUnique(user.getEmail())){

            if(Objects.equals(user.getRole().getId(), roleService.getEmployeeRoleId()) || Objects.equals(user.getRole().getId(), roleService.getAdminRoleId())){

                user.appendUniqueNumber(getUniqueNumber());

                sender.sendCredentialsMessage(user.getEmail(), user.getUsername(), user.getPassword());

                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            }

            user.setCreated(LocalDateTime.now());

            userRepository.save(user);

            result = true;
        }

        return result;
    }

    private boolean isEmailUnique(String email){
        return userRepository.findUserByEmail(email).isEmpty();
    }

    public void addUsers(List<User> users) throws MessagingException {
        for(var user: users){
            add(user);
        }
    }

    //Обновляет пользователя
    public void update(User user){

        user.setUpdated(LocalDateTime.now());

        userRepository.save(user);
    }

    //Удаляет пользователя
    public void delete(Long id){
        userRepository.deleteById(id);
    }

    //Получает id последней записи
    private long getUniqueNumber(){
        if(getAll().isEmpty()){
            return 1;
        } else{
            return getAll().get(getAll().size() - 1).getId();
        }
    }

    //Проверяет валидность обновляемого пароля
    public boolean isPasswordEquals(String enteredPassword, String originalPassword){
        return bCryptPasswordEncoder.matches(enteredPassword, originalPassword);
    }

    //Обновляет пароль
    public boolean updatePassword(ChangePasswordViewModel changePasswordViewModel){

        User user = (User) loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(!isPasswordEquals(changePasswordViewModel.getOldPassword(), user.getPassword())){
            return false;
        } else if(!changePasswordViewModel.getNewPassword().equals(changePasswordViewModel.getConfirmPassword())){
            return false;
        } else{

            user.setPassword(bCryptPasswordEncoder.encode(changePasswordViewModel.getNewPassword()));

            update(user);

            return true;
        }
    }

    //Устанавливает новый пароль
    public void setNewPassword(ResetPasswordViewModel resetPasswordViewModel){

        User user = resetTokenRepository.getUserByToken(resetPasswordViewModel.getToken());

        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordViewModel.getNewPassword()));

        update(user);
    }

    //Создает токен для восстановления пароля
    public void createPasswordResetTokenForUser(User user, String token){
        PasswordResetToken myToken = new PasswordResetToken(token, user);

        resetTokenRepository.save(myToken);
    }

    //Отправляет сообщение для восстановления пароля
    public void sendResetPasswordEmail(String token, User user) throws MessagingException {
        sender.sendPasswordResetLink(user.getEmail(), "Значение для восстановления пароля\n" + token);
    }

    public PasswordResetToken getPasswordResetToken(String token){
        return resetTokenRepository.getPasswordResetTokenByToken(token);
    }

    public boolean isTokenExpired(String token){
        var tokenObject = getPasswordResetToken(token);
        return tokenObject == null || LocalDateTime.now().isAfter(tokenObject.getExpiryDate());
    }
}
