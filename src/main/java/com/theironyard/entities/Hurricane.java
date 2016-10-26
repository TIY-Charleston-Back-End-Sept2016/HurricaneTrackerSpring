package com.theironyard.entities;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by zach on 10/21/16.
 */
@Entity
@Table(name = "hurricanes")
public class Hurricane {
    public enum Category {
        ONE, TWO, THREE, FOUR, FIVE
    }

    @Id
    @GeneratedValue
    public int id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String location;

    @Column(nullable = false)
    public Category category;

    @Column(nullable = false)
    public String image;

    @Column(nullable = false)
    public LocalDate date;

    @ManyToOne
    public User user;

    @Transient
    public boolean isMe;

    public Hurricane() {
    }

    public Hurricane(String name, String location, Category category, String image, LocalDate date, User user) {
        this.name = name;
        this.location = location;
        this.category = category;
        this.image = image;
        this.date = date;
        this.user = user;
    }
}
