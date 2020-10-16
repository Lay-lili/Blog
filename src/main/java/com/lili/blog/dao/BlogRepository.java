package com.lili.blog.dao;

import com.lili.blog.bean.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query(value = "select b from t_blog b where b.recommend=true ")
    List<Blog> findTop(Pageable pageable);

    @Query(value = "select b from t_blog b where b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findBySearch(String search, Pageable pageable);
}
