package com.lili.blog.dao;

import com.lili.blog.bean.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
