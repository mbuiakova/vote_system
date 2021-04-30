package app.web.controller;

import app.entity.Restaurant;
import app.repository.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    static final String REST_URL = "/restaurants";

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id){
        log.info("get restaurant {}", id);
        return repository.getById(id);
    }

    @GetMapping
    public List<Restaurant> getAll(){
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/allWithMenus")
    public List<Restaurant> getAllWithMenus(){
        log.info("getAll with menus");
        return repository.getAllWithMenus();
    }

    @GetMapping("/{id}/withMenus")
    public Restaurant getByIdWithMenus(@PathVariable int id){
        log.info("get restaurant {} with menus", id);
        return repository.getByIdWithMenus(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id){
        log.info("delete restaurant {}", id);
        repository.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant r){
        Restaurant created = repository.save(r);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Restaurant r, int id){
        repository.save(r);
    }

}
