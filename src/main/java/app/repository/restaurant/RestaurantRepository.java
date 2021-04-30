package app.repository.restaurant;

import app.entity.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean delete(int id);

    Restaurant getById(int id);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithMenus();

    Restaurant getByIdWithMenus(int id);
}
