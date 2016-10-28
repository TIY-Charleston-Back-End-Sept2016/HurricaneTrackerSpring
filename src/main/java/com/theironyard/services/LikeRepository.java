package com.theironyard.services;

import com.theironyard.entities.Hurricane;
import com.theironyard.entities.Like;
import com.theironyard.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by zach on 10/28/16.
 */
public interface LikeRepository extends CrudRepository<Like, Integer> {
    Like findFirstByUserAndHurricane(User user, Hurricane hurricane);
    List<Like> findByUser(User user);
}
