package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zach on 10/24/16.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    public int id;

    @Column(nullable = false, unique = true)
    public String name;

    @JsonIgnore
    @Column(nullable = false)
    public String password;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    List<Hurricane> hurricanes;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
