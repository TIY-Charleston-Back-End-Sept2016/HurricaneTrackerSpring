package com.theironyard.controllers;

import com.theironyard.entities.Hurricane;
import com.theironyard.entities.Like;
import com.theironyard.entities.User;
import com.theironyard.services.HurricaneRepository;
import com.theironyard.services.LikeRepository;
import com.theironyard.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zach on 10/28/16.
 */
@RestController
public class HurricaneTrackerRestController {
    @Autowired
    UserRepository users;

    @Autowired
    HurricaneRepository hurricanes;

    @Autowired
    LikeRepository likes;

    @RequestMapping(path = "/hurricanes.json", method = RequestMethod.GET)
    public Iterable<Hurricane> getHurricanes() {
        return hurricanes.findAll();
    }

    @RequestMapping(path = "/liked-hurricanes.json", method = RequestMethod.GET)
    public List<Hurricane> getLikedHurricanes(HttpSession session) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        ArrayList<Hurricane> likedHurricanes = new ArrayList<>();
        for (Like like : likes.findByUser(user)) {
            likedHurricanes.add(like.hurricane);
        }
        return likedHurricanes;
    }
}
