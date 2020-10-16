package com.lili.blog.web;

import com.lili.blog.NotFoundException;
import com.lili.blog.service.BlogService;
import com.lili.blog.service.TagService;
import com.lili.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //@RequestMapping("/")
    @GetMapping("/")
   // @ResponseBody
    //@PathVariable Integer id, @PathVariable String name
    public String index(Model model
            , @PageableDefault(size=2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){

        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(3));
        model.addAttribute("tags", tagService.listTagTop(5));
        model.addAttribute("recommendBlogs", blogService.listBlogTop(5));
        return "index";
    }

    @PostMapping("/search")
    public String Search(Model model, @RequestParam String search
            , @PageableDefault(size=2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page", blogService.listBlogSearch("%" + search + "%", pageable));
        model.addAttribute("search", search);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getAndConvertBlog(id));
         return "blog";
    }
}
