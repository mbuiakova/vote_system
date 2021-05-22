package app.repository.restaurant;

import app.entity.Menu;
import app.entity.Restaurant;
import app.entity.Vote;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(int id);

    Restaurant getById(int id);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithMenus();

    Restaurant getByIdWithMenus(int id);

    boolean saveVote(int restId, LocalDate date, int userId);

    List<Vote> getAllVotes();

    List<Vote> getVotesForRestaurant(int id);

    boolean saveMenu(int restId, LocalDate date, String menu);

    Menu getMenuByDateForRestaurant(LocalDate date, int id);

    List<Menu> getAllMenusForRestaurant(int id);

    boolean changeVote(int restId, LocalDate date, int userId);

    Vote getVoteByDateAndUserId(LocalDate date, int userId);
}
