package com.gathergrid.gathergridfeatures.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "Text")
    private String text;

    @Min(1)
    @Max(5)
    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    public Comment() {}

    public Comment(String text, int rating) {
        this.text = text;
        this.rating = rating;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && rating == comment.rating && Objects.equals(text, comment.text) && Objects.equals(user, comment.user) && Objects.equals(event, comment.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, rating, user, event);
    }
}
