package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Comment;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.CommentRepositryImpl;

import java.util.List;

public class CommentService {

    private final CommentRepositryImpl commentRepositry;

    public CommentService(CommentRepositryImpl commentRepositry) {
        this.commentRepositry = commentRepositry;
    }
    public Comment createComment(Comment comment){
        validate(comment);
        return commentRepositry.save(comment);
    }

    public List<Comment> ListComment(Long event_id){
        return commentRepositry.show(event_id);
    }

    public Comment updateComment(Comment comment) throws Exception {
        validate(comment);
        Comment exestingComment = commentRepositry.findById(comment.getId());
        if(exestingComment.getUser().getId() == comment.getUser().getId()){
            return commentRepositry.update(comment);
        }
        throw new Exception("this user is not same user who create this comment");
    }
    public void deleteComment(Long comment_id,Long user_id) throws Exception {
        Comment exestingComment = commentRepositry.findById(comment_id);
        if(exestingComment.getUser().getId() == user_id){
            commentRepositry.delete(comment_id);
        }else {
            throw new Exception("this user is not same user who create this comment");
        }
    }
    private void validate(Comment comment){
        if(comment.getText().isBlank() || comment.getUser() == null || comment.getEvent() == null){
            throw new IllegalArgumentException("All fields is needed");
        }
        if(comment.getText().contains("<") && comment.getText().contains(">")){
            throw new IllegalArgumentException("it's forbidden using both < and > for xss reasons");
        }
    }
}
