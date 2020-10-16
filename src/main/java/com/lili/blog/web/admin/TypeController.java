package com.lili.blog.web.admin;

import com.lili.blog.bean.Type;
import com.lili.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //跳转修改页面，获取需要改的记录
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/typeI";
    }

    //跳转管理页面，分页显示所有分类的记录
    @GetMapping("/types")
    public String types(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable  //Pageable根据前端页面参数自动封装而成
                                    , Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/typeM";
    }

    //跳转增加页面，并带一条空记录
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/typeI";
    }

    //对增加的分类进行校验，并保存
    @PostMapping("/typeSave")
    public String post(@Valid Type type
                       , BindingResult result
                       , RedirectAttributes attributes){
        //不重复校验
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        //后端非空校验
        if (result.hasErrors()){
            return "admin/typeI";
        }
        Type t = typeService.saveType(type);
        //System.out.println("------------------");
        if (t == null){
            attributes.addFlashAttribute("typeMessage", "新增失败！");
        }else {
            attributes.addFlashAttribute("typeMessage", "新增成功！");
        }
        //System.out.println(attributes.getAttribute("message"));
        return "redirect:/admin/types";
    }

    //对修改的分类进行校验，并保存
    @PostMapping("/typeEdit/{id}")
    public String editPost(@PathVariable Long id
            ,@Valid Type type
            , BindingResult result
            , RedirectAttributes attributes){
        //不重复校验
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        //后端非空校验
        if (result.hasErrors()){
            return "admin/typeI";
        }
        Type t = typeService.updateType(id, type);
        //System.out.println("------------------");
        if (t == null){
            attributes.addFlashAttribute("typeMessage", "更新失败！");
        }else {
            attributes.addFlashAttribute("typeMessage", "更新成功！");
        }
        //System.out.println(attributes.getAttribute("message"));
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id
                             ,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("typeMessage", "删除成功！");
        return "redirect:/admin/types";
    }

}
