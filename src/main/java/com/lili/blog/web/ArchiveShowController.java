package com.lili.blog.web;

import com.lili.blog.bean.Blog;
import com.lili.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){
        Map<String, List<Blog>> archiveBlogs = blogService.archiveBlogs();
        model.addAttribute("archiveBlogsMap",archiveBlogs);
        model.addAttribute("blogCount", blogService.getBlogCount());
        return "archives";
    }
}
