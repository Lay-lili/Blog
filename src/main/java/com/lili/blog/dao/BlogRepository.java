package com.lili.blog.dao;

import com.lili.blog.bean.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query(value = "select b from t_blog b where b.recommend=true ")
    List<Blog> findTop(Pageable pageable);

    @Query(value = "select b from t_blog b where b.published=true ")
    Page<Blog> findAllPublished(Pageable pageable);

    @Query(value = "select b from t_blog b where b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findBySearch(String search, Pageable pageable);

    @Modifying
    @Query("update t_blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from t_blog b group by year order by  function('date_format',b.updateTime,'%Y') desc")
    List<String> findYears();

    @Query("select b from t_blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
