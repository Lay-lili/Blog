package com.lili.blog.service;

import com.lili.blog.bean.Comment;
import com.lili.blog.dao.CommentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements  CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort =  Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
       // List<Comment> cs = dealComments(comments);

        return dealComments(comments);
    }

    /**
     * 复制一份，再对复制的进行处理。
     * @param comments
     * @return
     */
    private List<Comment> dealComments(List<Comment> comments){
        List<Comment> cs = new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            cs.add(c);
        }
        //合并评论的各层子评论到第一层子评论中
        mergeChildrenComments(cs);
        return cs;
    }

    /**
     * 将各层子评论合并为最顶层评论的子评论
     * @param cs
     */
    private void mergeChildrenComments(List<Comment> cs) {

        for (Comment c : cs){
            List<Comment> replys = c.getReplycomments();
            for (Comment r :replys){
                 getAllChildrenComments(r);
            }
            //将各层的子评论设置为最顶层子评论的直接子评论
            c.setReplycomments(allChildrenComments);
            //清空
            allChildrenComments = new ArrayList<>();
        }
    }

    private List<Comment> allChildrenComments = new ArrayList<>();
    /**
     *递归找到所有层的子评论
     * @param r
     */
    private void getAllChildrenComments(Comment r) {
        allChildrenComments.add(r);
        if (r.getReplycomments().size() > 0){
            List<Comment> replys = r.getReplycomments();
            for (Comment reply : replys){
                getAllChildrenComments(reply);
            }
        }
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long commentParentId = comment.getParentComment().getId();
        if (commentParentId != 0){
            comment.setParentComment(commentRepository.findById(commentParentId).orElse(null));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
