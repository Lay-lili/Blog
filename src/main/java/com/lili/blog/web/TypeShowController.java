package com.lili.blog.web;

import com.lili.blog.bean.Blog;
import com.lili.blog.bean.Type;
import com.lili.blog.service.BlogService;
import com.lili.blog.service.TypeService;
import com.lili.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("types/{id}")
    public String types(Model model, @PathVariable Long id
            , @PageableDefault(size = 1, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){

        List<Type> types = typeService.listTypeTop(1000);
        if (id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
