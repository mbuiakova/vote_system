package app.repository.restaurant;

import app.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface RestaurantRepositoryJpa extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int deleteById(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus WHERE r.id=?1")
    Restaurant getByIdWithMenus(@Param("id") int id);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menus")
    List<Restaurant> getAllWithMenus();


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO MENU_HISTORY (restaurant_id, date, menu) VALUES (:restId, :date, :menu)", nativeQuery = true)
    int saveMenu(@Param("restId") int restId, @Param("date") LocalDate date, @Param("menu") String menu);

    @Query(value = "SELECT m.date AS date, m.MENU AS menu FROM MENU_HISTORY m WHERE m.DATE=:date AND m.RESTAURANT_ID=:restId", nativeQuery = true)
    Optional<MenuProjection> getMenuByDateForRestaurant(@Param("date") LocalDate date, @Param("restId") int restId);

    @Query(value = "SELECT m.date AS date, m.MENU AS menu FROM MENU_HISTORY m WHERE m.RESTAURANT_ID=:restId", nativeQuery = true)
    List<MenuProjection> getAllMenusForRestaurant(@Param("restId") int restId);


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO votes (restaurant_id, date, user_id) VALUES (:restId, :date, :userId)", nativeQuery = true)
    int saveVote(@Param("restId") int restId, @Param("date") LocalDate date, @Param("userId") int userId);

    @Query(value = "SELECT v.restaurant_id AS restaurantId, v.date AS date, v.user_id AS userId FROM votes v WHERE v.restaurant_id=:restId ", nativeQuery = true)
    List<VoteProjection> getVotesForRestaurant(@Param("restId") int restId);

    @Query(value = "SELECT v.restaurant_id AS restaurantId, v.date AS date, v.user_id AS userId FROM votes v", nativeQuery = true)
    List<VoteProjection> getAllVotes();

    @Transactional
    @Modifying
    @Query(value = "UPDATE VOTES v SET RESTAURANT_ID=(:restId) WHERE v.DATE=:date AND v.USER_ID=:userId", nativeQuery = true)
    int changeVote(@Param("restId") int restId, @Param("date") LocalDate date, @Param("userId") int userId);

    @Query(value = "SELECT v.restaurant_id AS restaurantId, v.date AS date, v.user_id AS userId FROM votes v WHERE v.DATE=:date AND v.USER_ID=:userId", nativeQuery = true)
    Optional<VoteProjection> getVoteByDateAndUserId(@Param("date") LocalDate date, @Param("userId") int userId);

}
