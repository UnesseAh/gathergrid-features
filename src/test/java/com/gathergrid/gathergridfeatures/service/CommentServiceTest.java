package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Comment;
import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.domain.User;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.CommentRepositryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentServiceTest {

    private CommentService commentService;
    private CommentRepositryImpl commentRepository;

    @BeforeEach
    void setup(){
        commentRepository = Mockito.mock(CommentRepositryImpl.class);
        commentService = new CommentService(commentRepository);
    }


    @Test
    public void testCreatComment(){
        Comment comment = new Comment();
        comment.setText("Comment");
        comment.setUser(new User("Younes","AHASLA","youness@gmail.com", "123456789"));
        comment.setEvent(new Event("EVENT-NAME", LocalDateTime.now(),"ADDRESS","A short description"));

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        Comment output = commentService.createComment(comment);
        assertEquals(comment, output);
        Mockito.verify(commentRepository).save(comment);
    }

    @Test
    public void testListComment(){
        Long eventId = 1L;
        List<Comment> outputComments = new ArrayList<>();
        outputComments.add(new Comment("comment 1", 5));
        outputComments.add(new Comment("comment 2", 3));

        Mockito.when(commentRepository.show(eventId)).thenReturn(outputComments);

        List<Comment> result = commentService.ListComment(eventId);

        assertEquals(outputComments, result);
        Mockito.verify(commentRepository).show(eventId);
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Comment");
        comment.setRating(5);
        comment.setEvent(new Event("EVENT-NAME", LocalDateTime.now(),"ADDRESS","A short description"));
        comment.setUser(new User(1L,"Youness","AHASLA","youness@gmail.com", "123456789"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(comment);
        Mockito.when(commentRepository.update(comment)).thenReturn(comment);

        Comment result = commentService.updateComment(comment);
        assertEquals(comment, result);

    }

    @Test
    public void testDeleteComment() throws Exception{
        User user = new User(1L, "Youness", "AHASLA", "youness@gmail.com","123456789");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(comment);

        assertDoesNotThrow(()-> commentService.deleteComment(comment.getId(), user.getId()));

        Mockito.verify(commentRepository).findById(comment.getId());
        Mockito.verify(commentRepository).delete(comment.getId());
    }

}