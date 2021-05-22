package app.web.controller;

import app.entity.Menu;
import app.exception.IllegalRequestDataException;
import app.repository.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "/menus";
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RestaurantRepository repository;
    @Autowired
    private Clock clock;

    @PostMapping(value = "/{id}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> saveMenu(@RequestBody String menu, @PathVariable int id) {
        log.info("register menu for restaurant {}", id);

        if (menu.isEmpty() || menu.isBlank()) throw new IllegalRequestDataException("You can't save empty menu");

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

    @PutMapping(value = "/{id}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateMenu(@RequestBody String menu, @PathVariable int id) {
        log.info("update menu for restaurant {}", id);
        if (menu.isEmpty() || menu.isBlank()) throw new IllegalRequestDataException("You can't save empty menu");
        LocalDateTime dateTime = LocalDateTime.now(clock);
        repository.saveMenu(id, dateTime.toLocalDate(), menu);
    }

    @GetMapping("/{id}/menu")
    public Menu getMenuByDateForRestaurant(@RequestParam @NotNull LocalDate date, @PathVariable int id) {
        log.info("get menu for restaurant {}", id);
        return repository.getMenuByDateForRestaurant(date, id);
    }

    @GetMapping("/{id}/menus")
    public List<Menu> getAllMenusForRestaurant(@PathVariable int id) {
        log.info("get all menus for restaurant {}", id);
        return repository.getAllMenusForRestaurant(id);
    }
}
