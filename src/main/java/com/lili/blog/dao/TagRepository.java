package com.lili.blog.dao;

import com.lili.blog.bean.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    @Query(value = "select t from t_tag t")
    List<Tag> findTop(Pageable pageable);
}
