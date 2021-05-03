package app.repository.restaurant;

import app.entity.Restaurant;
import app.entity.Vote;
import app.exception.IllegalRequestDataException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantRepositoryJPA repositoryJPA;

    public RestaurantRepositoryImpl(RestaurantRepositoryJPA repositoryJPA) {
        this.repositoryJPA = repositoryJPA;
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return repositoryJPA.save(restaurant);
    }

    @Override
    public boolean delete(int id) {
        return repositoryJPA.deleteById(id) != 0;
    }

    @Override
    public Restaurant getById(int id) {
        return repositoryJPA.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return repositoryJPA.findAll();
    }

    @Override
    public List<Restaurant> getAllWithMenus() {
        return repositoryJPA.getAllWithMenus();
    }

    @Override
    public Restaurant getByIdWithMenus(int id) {
        return repositoryJPA.getByIdWithMenus(id);
    }

    @Override
    public boolean saveVote(int restId, LocalDate date, int userId) {
        return repositoryJPA.saveVote(restId, date, userId) != 0;
    }

    @Override
    public List<Vote> getAllVotes() {
        return repositoryJPA.getAllVotes()
                .stream()
                .map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vote> getVotesForRestaurant(int id) {
        return repositoryJPA.getVotesForRestaurant(id)
                .stream()
                .map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean changeVote(Vote oldVote, int newRestId, LocalTime time) {
        if(time.isAfter(LocalTime.of(11, 0, 0))){
            throw new IllegalRequestDataException("You can't change the vote, it is already 11 o'clock");
        }
        return repositoryJPA.changeVote(newRestId, oldVote.getDate(), oldVote.getUserId()) != 0;

    }
}
