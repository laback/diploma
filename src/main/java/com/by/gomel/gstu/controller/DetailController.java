package com.by.gomel.gstu.controller;

import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.service.CategoryService;
import com.by.gomel.gstu.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DetailController {

    private final DetailService detailService;
    private final CategoryService categoryService;

    @Autowired
    public DetailController(DetailService detailService, CategoryService categoryService) {
        this.detailService = detailService;
        this.categoryService = categoryService;
    }


    @GetMapping("/user/detail/{page}")
    public String getAllDetails(Model model, @PathVariable int page, @Param(value = "vinCode") String vinCode, @Param(value = "detailName") String detailName, @Param(value = "isInStock") String isInStock) throws IOException {

        List<Detail> details;

        if(vinCode != null && !vinCode.isBlank()){
            details = detailService.getAnalogsByVinCodeAndPage(vinCode, page);
        } else{
            details = detailService.getAllDetails(page, detailName);
        }

        if("true".equals(isInStock)){
            details = details.stream().filter(detail -> detail.getDetailCount() > 0).collect(Collectors.toList());
        }

        model.addAttribute("details", details);


        model.addAttribute("isInStock", isInStock);
        model.addAttribute("vinCode", vinCode);
        model.addAttribute("detailName", detailName);

        model.addAttribute("page", page);
        model.addAttribute("maxPages", detailService.getMaxPages(details));

        return "details/index";
    }

    @GetMapping("/user/detailInStock")
    public String getAllDetails(Model model) throws IOException {

        return getAllDetails(model, 0, null, null, "true");
    }

    @GetMapping("/user/detail/{category}/{page}")
    public String getAllDetailsByCategory(Model model, @PathVariable int page, @PathVariable String category){

        var details = detailService.getAllDetailByCategory(category, page);


        model.addAttribute("details", details);

        model.addAttribute("page", page);
        model.addAttribute("maxPages", detailService.getMaxPages(details));

        return "details/index";
    }

    @PostMapping("user/detail/increase/{id}")
    public String increaseDetailCountById(@PathVariable int id){
        detailService.increaseDetailsCountById(id);
        return "redirect:/user/detail/0";
    }

    @PostMapping("user/detail/decrease/{id}")
    public String decreaseDetailCountById(@PathVariable int id){
        detailService.decreaseDetailsCountById(id);
        return "redirect:/user/detail/0";
    }

    @GetMapping("user/detail/getDetail/{id}")
    public String getDetailById(@PathVariable long id, Model model){
        model.addAttribute("detail", detailService.getDetailById(id));

        return "details/detail";
    }

    @GetMapping("user/detail/add")
    public String getAddForm(Model model, @ModelAttribute("detail") Detail detail){

        model.addAttribute("categories", categoryService.getAllChildCategory());

        return "details/add";
    }

    @PostMapping("user/detail")
    public String addDetail(@ModelAttribute("detail") Detail detail){

        detailService.addDetail(detail);

        return "redirect:/user/detail/0";
    }
}
