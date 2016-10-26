package com.theironyard.services;

import com.theironyard.entities.Hurricane;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by zach on 10/21/16.
 */
public interface HurricaneRepository extends CrudRepository<Hurricane, Integer> {
    List<Hurricane> findByOrderByDateDesc();
    List<Hurricane> findByCategory(Hurricane.Category category);
    List<Hurricane> findByLocation(String location);
    List<Hurricane> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String name, String location);

    Hurricane findFirstByLocation(String location);
    int countByCategory(Hurricane.Category category);
}
