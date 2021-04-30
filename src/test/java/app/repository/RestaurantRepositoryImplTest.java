package app.repository;

import app.entity.Restaurant;
import app.repository.restaurant.RestaurantRepository;
import app.testData.RestaurantsTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import static app.testData.RestaurantsTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
// херь ниже не надо, они уже в @SpringJUnitConfig
//@ContextConfiguration(locations = {"classpath:spring/spring-app.xml",
//        "classpath:spring/spring-db.xml"})
// Надо все профили для работы. У нас это типы для hibernate и базы данных.
@ActiveProfiles({"hsqldb", "datajpa"})
//@WebAppConfiguration // работает и без него, т.к. в SpringJUnitConfig уже он есть.
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))//"classpath:initDB.sql",
public class RestaurantRepositoryImplTest {

    @Autowired
    private RestaurantRepository repository;

    @Test
    void save() {
        Restaurant created = repository.save(getNew());
        int newId = created.getId();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_TEST_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(newId), newRestaurant);
    }

    @Test
    void update(){
        Restaurant updated = getUpdated();
        repository.save(updated);
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(REST_ID_1), getUpdated());
    }

    @Test
    void delete() {
        repository.delete(rest_3.getId());
        assertNull(repository.getById(rest_3.getId()));
    }

    @Test
    void deleteNotFound(){
        assertFalse(repository.delete(NOT_FOUND));
    }

    @Test
    void getById() {
        Restaurant actual = repository.getById(RestaurantsTestData.REST_ID_1);
        RESTAURANT_TEST_MATCHER.assertMatch(actual, rest_1);
    }

    @Test
    void getNotFound() {
        assertNull(repository.getById(NOT_FOUND));
    }

    @Test
    void getAll() {
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getAll(), restaurants);
    }

    @Test
    void getAllWithMenus() {
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getAllWithMenus(), restaurants);
    }

    @Test
    void getByIdWithMenus() {
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getByIdWithMenus(REST_ID_1), rest_1);
    }
}