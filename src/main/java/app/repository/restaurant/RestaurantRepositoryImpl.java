package app.repository.restaurant;

import app.entity.Menu;
import app.entity.Restaurant;
import app.entity.Vote;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantRepositoryJpa repository;

    public RestaurantRepositoryImpl(RestaurantRepositoryJpa repository) {
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
    public boolean saveVote(int restId, LocalDate date, int userId) {
        return repository.saveVote(restId, date, userId) != 0;
    }

    @Override
    public boolean changeVote(int restId, LocalDate date, int userId) {
        return repository.changeVote(restId, date, userId) != 0;
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

    @Override
    public Vote getVoteByDateAndUserId(LocalDate date, int userId) {
        Optional<Vote> v = repository.getVoteByDateAndUserId(date, userId).map(e -> new Vote(e.getRestaurantId(), e.getDate(), e.getUserId()));
        return v.orElse(null); // !!!! make a constructor in Vote with VoteProjection
    }

    @Override
    public boolean saveMenu(int restId, LocalDate date, String menu) {
        if (getMenuByDateForRestaurant(date, restId) != null) {
            return repository.updateMenu(restId, date, menu) != 0;
        }
        return repository.saveMenu(restId, date, menu) != 0;
    }

    @Override
    public Menu getMenuByDateForRestaurant(LocalDate date, int id) {
        Optional<Menu> m = repository.getMenuByDateForRestaurant(date, id).map(e -> new Menu(e.getDate(), e.getMenu()));
        return m.orElse(null);
    }

    @Override
    public List<Menu> getAllMenusForRestaurant(int id) {
        return repository.getAllMenusForRestaurant(id).stream()
                .map(e -> new Menu(e.getDate(), e.getMenu())).collect(Collectors.toList());
    }


}
