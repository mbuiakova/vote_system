package app.repository.restaurant;

import app.entity.Restaurant;
import app.entity.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(int id);

    Restaurant getById(int id);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithMenus();

    Restaurant getByIdWithMenus(int id);

    boolean saveVote(int rest_id, LocalDateTime date, int user_id);

    List<Vote> getAllVotes();

    List<Vote> getVotesForRestaurant(int id);


}
