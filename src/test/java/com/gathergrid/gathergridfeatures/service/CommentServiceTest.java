package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Comment;
import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.domain.User;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.CommentRepositryImpl;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.EventRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    private CommentService commentService;
    private CommentRepositryImpl commentRepository;
    private EventService eventService;
    private EventRepositoryImpl eventRepository;


    @BeforeEach
    void setup(){
        eventRepository = Mockito.mock(EventRepositoryImpl.class);
        eventService = new EventService(eventRepository);
        commentRepository = Mockito.mock(CommentRepositryImpl.class);
        commentService = new CommentService(commentRepository, eventService);
    }

    @Test
    void testEventDoesExistWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setUser(new User());

        Comment comment2 = new Comment("comment", 5);
        comment2.setId(1L);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment2);

        assertThrows(IllegalArgumentException.class,() -> commentService.createComment(comment),"Event must not be null");
    }

    @Test
    void testUserDoesExistWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setEvent(new Event());

        Comment comment2 = new Comment("comment", 5);
        comment2.setId(1L);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment2);

        assertThrows(IllegalArgumentException.class,() -> commentService.createComment(comment), "User does not exist");
    }

    @Test
    void testEventIsNotNullWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setUser(new User());
        comment.setEvent(new Event());

        Comment comment2 = new Comment("comment", 5);
        comment2.setId(1L);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment2);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment), "Event is empty");
    }

    @Test
    void testTextIsNotBlankWhenSavingComment(){
        Comment comment = new Comment("", 10);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment), "Comment Text is empty");
    }

    @Test
    void testTextIsSafeWhenSavingComment(){
        Comment comment = new Comment("<text>", 5);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment),"Comment should contain the tags <>");
    }

    @Test
    void testRatingIsLessThanTenAndMoreThanZeroWhenSavingComment(){
        Comment comment = new Comment("text", 11);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment),"Rating must be from 1 to 10");
    }

    @Test
    void testCreateComment(){
        Comment comment = new Comment("text", 10);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Comment result = commentService.createComment(comment);
        assertEquals(comment, result);
    }

    @Test
    void testEventExistWhenGettingItsComments(){
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("comment1", 10));
        comments.add(new Comment("comment2", 10));

        Mockito.when(eventRepository.find(1L)).thenReturn(null);
        Mockito.when(commentRepository.show(1L)).thenReturn(comments);

        assertThrows(NoSuchElementException.class, () -> commentService.ListComment(1L), "Event must exist");
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
    public void testFindCommentWhenUpdatingComment(){
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(null);

        assertThrows(NoSuchElementException.class,()->commentService.updateComment(comment),"Comment doesn't exist!");
    }

    @Test
    public void testIfCommentUsersMatchesWhenUpdatingComment() {
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(2L)).thenReturn(comment);

        assertThrows(Exception.class, () -> commentService.updateComment(comment),"Users of the comments don't match");
        Mockito.verify(commentRepository).findById(comment.getId());
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment comment = new Comment("text",10);
        comment.setId(1L);
        comment.setEvent(new Event("EVENT-NAME", LocalDateTime.now(),"ADDRESS","A short description"));
        comment.setUser(new User(1L,"Youness","AHASLA","youness@gmail.com", "123456789"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(comment);
        Mockito.when(commentRepository.update(comment)).thenReturn(comment);

        Comment result = commentService.updateComment(comment);
        assertEquals(comment, result);
    }

    @Test
    public void testFindCommentWhenDeletingComment(){
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(null);

        assertThrows(NoSuchElementException.class,()->commentService.deleteComment(comment.getId(), 1L),"Comment doesn't exist!");
    }

    @Test
    public void testIfCommentBelongsToUserWhenDeletingComment() {
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User(1,"firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(1L)).thenReturn(comment);

        assertThrows(Exception.class, () -> commentService.deleteComment(comment.getUser().getId(),2L),"Users of the comments don't match");
        Mockito.verify(commentRepository).findById(comment.getId());
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