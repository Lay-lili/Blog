package com.lili.blog.service;

import com.lili.blog.bean.Blog;
import com.lili.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvertBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlogPublished(Pageable pageable);

    List<Blog> listBlogTop(Integer size);

    Page<Blog> listBlogSearch(String search, Pageable pageable);

    Page<Blog> listBlog(Long tagId, Pageable pageable);

    Map<String, List<Blog>> archiveBlogs();

    Long getBlogCount();
}
