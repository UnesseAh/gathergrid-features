package com.gathergrid.gathergridfeatures.service;

import com.gathergrid.gathergridfeatures.domain.Comment;
import com.gathergrid.gathergridfeatures.domain.Event;
import com.gathergrid.gathergridfeatures.domain.User;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.CommentRepositryImpl;
import com.gathergrid.gathergridfeatures.repository.interfacesImpl.EventRepositoryImpl;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    private static CommentService commentService;
    private static CommentRepositryImpl commentRepository;
    private static EventService eventService;
    private static EventRepositoryImpl eventRepository;


    @BeforeEach
    void setup(){
        eventRepository = Mockito.mock(EventRepositoryImpl.class);
        eventService = new EventService(eventRepository);
        commentRepository = Mockito.mock(CommentRepositryImpl.class);
        commentService = new CommentService(commentRepository, eventService);
    }

    @Test
    @DisplayName("Test if event is not null when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment without an associated event.")
    void testEventIsNotNullWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setUser(new User());

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class,() -> commentService.createComment(comment),"Event must not be null");
    }

    @Test
    @DisplayName("Test if event is not empty when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment with an empty event.")
    void testEventIsNotEmptyWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setUser(new User());

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment), "Event must not be empty");
    }

    @Test
    @DisplayName("Test if user is not null when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment without an associated user.")
    void testUserIsNotNullWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setEvent(new Event());

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class,() -> commentService.createComment(comment), "User must not be null");
    }

    @Test
    @DisplayName("Test if user is not empty when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment with an empty user.")
    void testUserIsNotEmptyWhenSavingComment(){
        Comment comment = new Comment("comment", 5);
        comment.setUser(new User());

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class,() -> commentService.createComment(comment), "User must not be empty");
    }

    @Test
    @DisplayName("Test if text is not blank when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment with an empty text.")
    void testTextIsNotBlankWhenSavingComment(){
        Comment comment = new Comment("", 10);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment), "Comment Text is empty");
    }

    @Test
    @DisplayName("Test if text is safe when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment containing unsafe characters.")
    void testTextIsSafeWhenSavingComment(){
        Comment comment = new Comment("<text>", 5);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment),"Comment should contain the tags <>");
    }

    @Test
    @DisplayName("Test if rating is within valid range when saving comment")
    @Description("This test verifies that an exception is thrown when saving a comment with a rating outside the valid range (1-10).")
    void testRatingIsLessThanTenAndMoreThanZeroWhenSavingComment(){
        Comment comment = new Comment("text", 11);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(comment),"Rating must be from 1 to 10");
    }

    @Test
    @DisplayName("Test creating a comment")
    @Description("This test verifies the successful creation of a comment.")
    void testCreateComment(){
        Comment comment = new Comment("text", 10);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Comment result = commentService.createComment(comment);
        assertEquals(comment, result);
    }

    @Test
    @DisplayName("Test if event exists when getting its comments")
    @Description("This test verifies that an exception is thrown when trying to retrieve comments for a non-existing event.")
    void testEventExistWhenGettingItsComments(){
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("comment1", 10));
        comments.add(new Comment("comment2", 10));

        Mockito.when(eventRepository.find(1L)).thenReturn(null);
        Mockito.when(commentRepository.show(1L)).thenReturn(comments);

        assertThrows(NoSuchElementException.class, () -> commentService.ListComment(1L), "Event must exist");
    }

    @Test
    @DisplayName("Test list comments for an event")
    @Description("This test verifies the successful retrieval of comments for an event.")
    public void testListComment(){
        Long eventId = 1L;
        List<Comment> outputComments = new ArrayList<>();
        Comment comment = new Comment("text", 10);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));
        outputComments.add(comment);
        outputComments.add(comment);

        Mockito.when(eventService.findById(eventId)).thenReturn(new Event());
        Mockito.when(commentRepository.show(eventId)).thenReturn(outputComments);

        List<Comment> result = commentService.ListComment(eventId);

        assertEquals(outputComments, result);
        Mockito.verify(commentRepository).show(eventId);
    }

    @Test
    @DisplayName("Test finding comment when updating comment")
    @Description("This test verifies that an exception is thrown when trying to update a non-existing comment.")
    public void testFindCommentWhenUpdatingComment(){
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(null);

        assertThrows(NoSuchElementException.class,()->commentService.updateComment(comment),"Comment doesn't exist!");
    }

    @Test
    @DisplayName("Test if comment users match when updating comment")
    @Description("This test verifies that an exception is thrown when trying to update a comment where the users don't match.")
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
    @DisplayName("Test updating a comment")
    @Description("This test verifies the successful update of a comment.")
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
    @DisplayName("Test finding comment when deleting comment")
    @Description("This test verifies that an exception is thrown when trying to delete a non-existing comment.")
    public void testFindCommentWhenDeletingComment(){
        Comment comment = new Comment("comment",10);
        comment.setId(1L);
        comment.setEvent(new Event("name",LocalDateTime.now(),"address", "description"));
        comment.setUser(new User("firstname","lastname","yns@gmail.com","password"));

        Mockito.when(commentRepository.findById(comment.getId())).thenReturn(null);

        assertThrows(NoSuchElementException.class,()->commentService.deleteComment(comment.getId(), 1L),"Comment doesn't exist!");
    }

    @Test
    @DisplayName("Test if comment belongs to user when deleting comment")
    @Description("This test verifies that an exception is thrown when trying to delete a comment that doesn't belong to the user.")
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
    @DisplayName("Test deleting a comment")
    @Description("This test verifies the successful deletion of a comment.")
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