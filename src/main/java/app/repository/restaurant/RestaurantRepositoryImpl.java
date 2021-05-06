package app.repository.restaurant;

import app.entity.Restaurant;
import app.entity.Vote;
import app.exception.IllegalRequestDataException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantRepositoryJPA repository;

    public RestaurantRepositoryImpl(RestaurantRepositoryJPA repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @Override
    public boolean delete(int id) {
        return repository.deleteById(id) != 0;
    }

    @Override
    public Restaurant getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Restaurant> getAllWithMenus() {
        return repository.getAllWithMenus();
    }

    @Override
    public Restaurant getByIdWithMenus(int id) {
        return repository.getByIdWithMenus(id);
    }

    @Override
    public boolean saveVote(int restId, LocalDateTime date, int userId) {
        Vote vote = getVoteByDateAndUserId(date.toLocalDate(), userId);
        if (vote == null) {
            return repository.saveVote(restId, date.toLocalDate(), userId) != 0;
        } else {
            if (date.toLocalTime().isAfter(LocalTime.of(11, 0, 0))) {
                throw new IllegalRequestDataException("You can't change the vote, it is already 11 o'clock");
            }
            return repository.changeVote(restId, vote.getDate(), vote.getUserId()) != 0;
        }
    }

    @Override
    public List<Vote> getAllVotes() {
        return repository.getAllVotes()
                .stream()
                .map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vote> getVotesForRestaurant(int id) {
        return repository.getVotesForRestaurant(id)
                .stream()
                .map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()))
                .collect(Collectors.toList());
    }

    public Vote getVoteByDateAndUserId(LocalDate date, int userId) {
        Optional<Vote> v = repository.getVoteByDateAndUserId(date, userId).map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()));
        return v.orElse(null); // !!!! make a constructor in Vote with VoteProjection
    }
}
