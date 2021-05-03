package app.web.controller;

import app.entity.Restaurant;
import app.entity.Vote;
import app.repository.restaurant.RestaurantRepository;
import app.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        Restaurant restaurant = repository.getById(id);
        if (restaurant != null) {
            return restaurant;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
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
    public void update(@RequestBody Restaurant r, @PathVariable int id){
        repository.save(r);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/vote/{id}")
    public ResponseEntity<Vote> saveVote(@PathVariable int id){
        log.info("save vote for restaurant {}", id);
        LocalDateTime date = LocalDateTime.now();
        Vote created = new Vote(id, date.toLocalDate(), 1);
        repository.saveVote(id, date.toLocalDate(), 1 );//SecurityUtil.authUserId()
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/vote/{id}")
                .buildAndExpand(created.getRestaurantId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/votes")
    public List<Vote> getAllVotes(){
        log.info("get all votes");
        return repository.getAllVotes();
    }

    @GetMapping("/votes/{id}")
    public List<Vote> getVotesForRestaurant(@PathVariable int id){
        log.info("get all votes for restaurant {}", id);
        return repository.getVotesForRestaurant(id);
    }

    @PutMapping(value = "/vote/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateVote(@RequestBody Vote v, @PathVariable int id){
        log.info("update vote for restaurant {}", id);
        var time = LocalTime.now();
        repository.changeVote(v, id, time);
    }
}
