package app.web.controller;

import app.entity.Menu;
import app.entity.Restaurant;
import app.entity.Vote;
import app.exception.IllegalRequestDataException;
import app.exception.NotFoundException;
import app.repository.restaurant.RestaurantRepository;
import app.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private Clock clock;

    static final String REST_URL = "/restaurants";

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        Restaurant restaurant = repository.getById(id);
        if (restaurant != null) {
            return restaurant;
        } else {
            throw new NotFoundException("Unable to find resource");
        }
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/allWithMenus")
    public List<Restaurant> getAllWithMenus() {
        log.info("getAll with menus");
        return repository.getAllWithMenus();
    }

    @GetMapping("/{id}/withMenus")
    public Restaurant getByIdWithMenus(@PathVariable int id) {
        log.info("get restaurant {} with menus", id);
        return repository.getByIdWithMenus(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        repository.save(restaurant);
    }

    @PostMapping(value = "/{id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> saveVote(@PathVariable int id) {
        log.info("register vote for restaurant {}", id);

        LocalDateTime dateTime = LocalDateTime.now(clock);
        Vote created = new Vote(id, dateTime.toLocalDate(), SecurityUtil.authUserId());
        repository.saveVote(id, dateTime, SecurityUtil.authUserId());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/vote")
                .buildAndExpand(created.getRestaurantId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/votes")
    public List<Vote> getAllVotes() {
        log.info("get all votes");
        return repository.getAllVotes();
    }

    @GetMapping("/{id}/votes")
    public List<Vote> getVotesForRestaurant(@PathVariable int id) {
        log.info("get all votes for restaurant {}", id);
        return repository.getVotesForRestaurant(id);
    }

    @PutMapping(value = "/{id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateVote(@RequestBody Vote vote, @PathVariable int id) {
        log.info("update vote for restaurant {}", id);
        LocalDateTime time = LocalDateTime.now(clock);
        repository.saveVote(id, time, vote.getUserId());
    }

    @PostMapping(value = "/{id}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> saveMenu(@RequestBody String menu, @PathVariable int id) {
        log.info("register menu for restaurant {}", id);

        LocalDateTime dateTime = LocalDateTime.now(clock);
        if (repository.getMenuByDateForRestaurant(dateTime.toLocalDate(), id) != null) {
            throw new IllegalRequestDataException("You can't create the menu, it already exists");
        }

        repository.saveMenu(id, dateTime.toLocalDate(), menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/menu")
                .buildAndExpand(id).toUri();
        Menu created = repository.getMenuByDateForRestaurant(dateTime.toLocalDate(), id);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}/menu")
    public Menu getMenuByDateForRestaurant(@RequestParam LocalDate date, @PathVariable int id) {
        log.info("get menu for restaurant {}", id);
        return repository.getMenuByDateForRestaurant(date, id);
    }

    @GetMapping("/{id}/menus")
    public List<Menu> getAllMenusForRestaurant(@PathVariable int id) {
        log.info("get all menus for restaurant {}", id);
        return repository.getAllMenusForRestaurant(id);
    }
}
