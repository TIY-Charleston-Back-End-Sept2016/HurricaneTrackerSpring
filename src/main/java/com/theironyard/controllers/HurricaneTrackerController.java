package com.theironyard.controllers;

import com.theironyard.entities.Like;
import com.theironyard.services.LikeRepository;
import com.theironyard.utilities.PasswordStorage;
import com.theironyard.entities.Hurricane;
import com.theironyard.entities.User;
import com.theironyard.services.HurricaneRepository;
import com.theironyard.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by zach on 10/21/16.
 */
@Controller
public class HurricaneTrackerController {
    @Autowired
    HurricaneRepository hurricanes;

    @Autowired
    UserRepository users;

    @Autowired
    LikeRepository likes;

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        User defaultUser = new User("Zach", PasswordStorage.createHash("hunter2"));
        if (users.findFirstByName(defaultUser.name) == null) {
            users.save(defaultUser);
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, Hurricane.Category category, String search, HttpSession session) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);

        List<Hurricane> hlist;
        if (category != null) {
            hlist = hurricanes.findByCategory(category);
        }
        else if (search != null) {
            hlist = hurricanes.findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(search, search);
        }
        else {
            hlist = hurricanes.findByOrderByDateDesc();
        }

        for (Hurricane h : hlist) {
            h.isMe = h.user.name.equals(name);
            h.isLiked = likes.findFirstByUserAndHurricane(user, h) != null;
        }

        model.addAttribute("hurricanes", hlist);
        model.addAttribute("user", user);
        model.addAttribute("now", LocalDate.now());
        return "home";
    }

    @RequestMapping(path = "/hurricane", method = RequestMethod.POST)
    public String addHurricane(String hname, String hlocation, Hurricane.Category hcategory, String himage, String date, HttpSession session) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        if (user == null) {
            throw new Exception("Not logged in.");
        }
        Hurricane h = new Hurricane(hname, hlocation, hcategory, himage, LocalDate.parse(date), user);
        hurricanes.save(h);
        return "redirect:/";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if (!PasswordStorage.verifyPassword(password, user.password)) {
            throw new Exception("Wrong password!");
        }
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/delete-hurricane", method = RequestMethod.POST)
    public String delete(HttpSession session, int id) throws Exception {
        if (!validateUser(session, id)) {
            throw new Exception("Not allowed!");
        }
        hurricanes.delete(id);
        return "redirect:/";
    }

    @RequestMapping(path = "/edit-hurricane", method = RequestMethod.GET)
    public String editGet(Model model, int id) {
        Hurricane h = hurricanes.findOne(id);
        model.addAttribute("hurricane", h);
        return "edit";
    }

    @RequestMapping(path = "/edit-hurricane", method = RequestMethod.POST)
    public String editPost(int id, String hname, String hlocation, Hurricane.Category hcategory, String himage, HttpSession session) throws Exception {
        if (!validateUser(session, id)) {
            throw new Exception("Not allowed!");
        }
        Hurricane h = hurricanes.findOne(id);
        h.name = hname;
        h.location = hlocation;
        h.category = hcategory;
        h.image = himage;
        hurricanes.save(h);
        return "redirect:/";
    }

    @RequestMapping(path = "/like-hurricane", method = RequestMethod.POST)
    public String addLike(int id, HttpSession session) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        Hurricane h = hurricanes.findOne(id);
        Like like = likes.findFirstByUserAndHurricane(user, h);
        if (like != null) {
            likes.delete(like);
        }
        else {
            like = new Like(user, h);
            likes.save(like);
        }
        return "redirect:/";
    }

    public boolean validateUser(HttpSession session, int id) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        Hurricane h = hurricanes.findOne(id);
        return user != null && h != null && user.name.equals(h.user.name);
    }
}
