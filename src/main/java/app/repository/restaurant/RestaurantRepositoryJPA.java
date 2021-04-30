package app.repository.restaurant;

import app.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepositoryJPA extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int deleteById(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN  FETCH r.menus WHERE r.id=?1")
    Restaurant getByIdWithMenus(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus")
    List<Restaurant> getAllWithMenus();

}
