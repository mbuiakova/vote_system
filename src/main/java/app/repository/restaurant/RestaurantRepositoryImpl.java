package app.repository.restaurant;

import app.entity.Restaurant;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
