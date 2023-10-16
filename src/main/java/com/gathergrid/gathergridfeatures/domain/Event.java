package com.gathergrid.gathergridfeatures.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private LocalDateTime date;
    private String address;
    @Column(columnDefinition = "Text")
    private String description;

    @ManyToOne
    private User organizer;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    private Category category;

    public Event() {}

    public Event(String name, LocalDateTime date, String address, String description) {
        this.name = name;
        this.date = date;
        this.address = address;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}