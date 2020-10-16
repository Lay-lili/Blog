package com.lili.blog.web.admin;

import com.lili.blog.bean.Tag;
import com.lili.blog.service.TagService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TagController {


    @Autowired
    private TagService tagService;

    //跳转修改页面，获取需要改的记录
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tagI";
    }

    //跳转管理页面，分页显示所有分类的记录
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable  //Pageable根据前端页面参数自动封装而成
            , Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tagM";
    }

    //跳转增加页面，并带一条空记录
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/tagI";
    }

    //对增加的分类进行校验，并保存
    @PostMapping("/tagSave")
    public String post(@Valid Tag tag
            , BindingResult result
            , RedirectAttributes attributes){
        //不重复校验
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name","nameError","不能添加重复标签");
        }
        //后端非空校验
        if (result.hasErrors()){
            return "admin/tagI";
        }
        Tag t = tagService.saveTag(tag);
        //System.out.println("------------------");
        if (t == null){
            attributes.addFlashAttribute("tagMessage", "新增失败！");
        }else {
            attributes.addFlashAttribute("tagMessage", "新增成功！");
        }
        //System.out.println(attributes.getAttribute("message"));
        return "redirect:/admin/tags";
    }

    //对修改的标签进行校验，并保存
    @PostMapping("/tagEdit/{id}")
    public String editPost(@PathVariable Long id
            ,@Valid Tag tag
            , BindingResult result
            , RedirectAttributes attributes){
        //不重复校验
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            result.rejectValue("name","nameError","不能添加重复标签");
        }
        //后端非空校验
        if (result.hasErrors()){
            return "admin/tagI";
        }
        Tag t = tagService.updateTag(id, tag);
        //System.out.println("------------------");
        if (t == null){
            attributes.addFlashAttribute("tagMessage", "更新失败！");
        }else {
            attributes.addFlashAttribute("tagMessage", "更新成功！");
        }
        //System.out.println(attributes.getAttribute("message"));
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteType(@PathVariable Long id
            ,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("tagMessage", "删除成功！");
        return "redirect:/admin/tags";
    }
}
