package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Comment;
import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.domain.User;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.CommentRepositryImpl;

import java.util.List;
import java.util.NoSuchElementException;

public class CommentService {

    private final CommentRepositryImpl commentRepositry;
    private final EventService eventService;

    public CommentService(CommentRepositryImpl commentRepositry, EventService eventService) {
        this.commentRepositry = commentRepositry;
        this.eventService = eventService;
    }
    public Comment createComment(Comment comment){
        validate(comment);
        return commentRepositry.save(comment);
    }

    public List<Comment> ListComment(Long event_id){
        Event event = eventService.findById(event_id);
        if (event != null)
            return commentRepositry.show(event_id);
        else
            throw new NoSuchElementException("Event not found");
    }

    public Comment updateComment(Comment comment) throws Exception {
        validate(comment);
        Comment exestingComment = commentRepositry.findById(comment.getId());
        if(exestingComment != null){
            if(exestingComment.getUser().getId() == comment.getUser().getId()){
                return commentRepositry.update(comment);
            }else
                throw new Exception("This user is not the same user who create this comment");
        }else
            throw new NoSuchElementException("Comment with ID: " + comment.getId() + " doesn't exist");
    }

    public void deleteComment(Long comment_id,Long user_id) throws Exception {
        Comment exestingComment = commentRepositry.findById(comment_id);
        if(exestingComment != null){
            if(exestingComment.getUser().getId() == user_id){
                commentRepositry.delete(comment_id);
            }else {
                throw new Exception("this user is not same user who create this comment");
            }
        }else
            throw new NoSuchElementException("Comment with id : " + comment_id + " doesn't exist!");

    }
    private void validate(Comment comment){
        if (comment.getEvent() == null || comment.getEvent().equals(new Event())){
            throw  new IllegalArgumentException("Event must not be null or empty");
        }
        if (comment.getUser() == null || comment.getUser().equals(new User())){
            throw  new IllegalArgumentException("User must not be null or empty");
        }
        if(comment.getText().isBlank() ){
            throw new IllegalArgumentException("Text must not be blank");
        }
        if(comment.getText().contains("<") || comment.getText().contains(">")){
            throw new IllegalArgumentException("it's forbidden using both < and > for xss reasons");
        }
        if(comment.getRating() < 1 || comment.getRating() > 10){
            throw new IllegalArgumentException("rating must not be larger than 10");
        }
    }
}
