package com.lili.blog.web.admin;

import com.lili.blog.bean.Blog;
import com.lili.blog.bean.Tag;
import com.lili.blog.bean.Type;
import com.lili.blog.bean.User;
import com.lili.blog.service.BlogService;
import com.lili.blog.service.TagService;
import com.lili.blog.service.TypeService;
import com.lili.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogI";
    private static final String LIST = "admin/blogM";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //跳转博客列表页面
    @GetMapping("/blogs")
    public String listBlog(Model model
            , @PageableDefault(size=2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogM";
    }

    //博客搜索
    @PostMapping("/blogs/search")
    public String searchBlog(Model model
            , @PageableDefault(size=2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable
            , BlogQuery blog){
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogM :: blogList";
    }

    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        Blog blog = new Blog();
       // blog.setType(new Type());
        model.addAttribute("blog", blog);
        return INPUT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("types", typeService.listType());
    }

    //跳转博客修改页面
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        String ids = tagIds(blog.getTags());
        blog.setTagIds(ids);
        model.addAttribute("blog", blog);
        return INPUT;
    }

    //将当前需要修改的博客中多个标签的id拼接成字符串，例：“1，2，3”
    private String tagIds(List<Tag> tags){
        if ( !tags.isEmpty() ){
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags){
                if (flag){
                    ids.append(",");
                }
                flag =true;
                ids.append(tag.getId());
            }
            return ids.toString();
        }else{
            return "";
        }
    }

    //新增和修改博客
    @PostMapping("/blogs")
    public String post(Model model, HttpSession session, Blog blog, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getTypeId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;
        if (blog.getId() == null){
             b = blogService.saveBlog(blog);
        }else{
             b = blogService.updateBlog(blog.getId() , blog);
        }

        if (b == null){
            attributes.addFlashAttribute("blogMessage", "操作失败");
        }else {
            attributes.addFlashAttribute("blogMessage", "操作成功");
        }
        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/delete")
    public String deleteType(@PathVariable Long id
            ,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("blogMessage", "删除成功！");
        return REDIRECT_LIST;
    }

}
