package com.lili.blog.service;

import com.lili.blog.NotFoundException;
import com.lili.blog.bean.Blog;
import com.lili.blog.bean.Type;
import com.lili.blog.dao.BlogRepository;
import com.lili.blog.util.MarkdownUtils;
import com.lili.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Blog getAndConvertBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null){

            throw new NotFoundException("博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        String c = MarkdownUtils.markdownToHtmlExtensions(content);
        b.setContent(c);

        blogRepository.updateViews(b.getId());
        return b;

    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if ( !"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        blog.setCreateTime(new Date());
        blog.setViews(0);

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {

        Blog b = blogRepository.findById(id).orElse(null);
        if (b ==null){
            throw new NotFoundException("该博客不存在");
        }
        blog.setCreateTime(b.getCreateTime());
        blog.setViews(b.getViews());
        BeanUtils.copyProperties(blog, b);
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
       return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlogPublished(Pageable pageable) {    //查询所有分布的博客
        return blogRepository.findAllPublished(pageable);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort =  Sort.by(Sort.Direction.DESC, "updateTime");  //按照该分类下的博客数量倒序排列
        Pageable pageable =  PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listBlogSearch(String search, Pageable pageable) {

        return blogRepository.findBySearch(search, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");

                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlogs() {
        List<String> years = blogRepository.findYears();
        Map<String,List<Blog>> archiveBlogs = new LinkedHashMap<>();
        for (String year : years){
            //System.out.println(year);
            archiveBlogs.put(year,blogRepository.findByYear(year));
        }
        return archiveBlogs;
    }

    @Override
    public Long getBlogCount() {
        return blogRepository.count();
    }
}
