package app.web.controller;

import app.entity.Vote;
import app.exception.IllegalRequestDataException;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private Clock clock;

    static final String REST_URL = "/votes";

    @PostMapping(value = "/{id}/vote")
    public ResponseEntity<Vote> saveVote(@PathVariable int id) {
        log.info("register vote for restaurant {}", id);

        LocalDateTime dateTime = LocalDateTime.now(clock);

        if (repository.getMenuByDateForRestaurant(dateTime.toLocalDate(), id) == null) {
            throw new IllegalRequestDataException("Can't vote for restaurant without menu on this day.");
        }

        Vote created = new Vote(id, dateTime.toLocalDate(), SecurityUtil.authUserId());
        if (!repository.saveVote(id, dateTime, SecurityUtil.authUserId())) {
            throw new IllegalRequestDataException("Can't save vote.");
        }

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/vote")
                .buildAndExpand(created.getRestaurantId()).toUri();
        return ResponseEntity.created(uriOfNewResource).contentType(MediaType.APPLICATION_JSON).body(created);
    }

    @PutMapping(value = "/{id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateVote(@PathVariable int id) {
        log.info("update vote for restaurant {}", id);
        LocalDateTime time = LocalDateTime.now(clock);
        repository.saveVote(id, time, SecurityUtil.authUserId());
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
}
