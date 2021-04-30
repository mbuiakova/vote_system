package app.repository;

import app.entity.User;
import app.repository.user.UserRepository;
import app.testData.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static app.testData.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
// Надо все профили для работы. У нас это типы для hibernate и базы данных.
@ActiveProfiles({"hsqldb", "datajpa"})
//@WebAppConfiguration // работает и без него, т.к. в SpringJUnitConfig уже он есть.
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))//"classpath:initDB.sql",
class UserRepositoryImplTest {

    @Autowired
    private UserRepository repository;

    @Test
    void getAll() {
        List<User> all = repository.getAll();
        USER_TEST_MATCHER.assertMatch(all, users);
    }

    @Test
    void save() {
        User created = repository.save(getNew());
        int id = created.getId();
        User newUser = getNew();
        newUser.setId(id);
        USER_TEST_MATCHER.assertMatch(created, newUser);
        USER_TEST_MATCHER.assertMatch(repository.get(id), newUser);
    }

    @Test
    void update() {
        User updated = getUpdated();
        repository.save(updated);
        USER_TEST_MATCHER.assertMatch(repository.get(USER_ID), getUpdated());
    }

    @Test
    void get() {
        User actual = repository.get(USER_ID);
        USER_TEST_MATCHER.assertMatch(actual, user);
    }

    @Test
    void getNotFound(){
        assertNull(repository.get(NOT_FOUND));
    }

    @Test
    void getByEmail(){
        User user = repository.getByEmail("user@domain.com");
        USER_TEST_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    void delete() {
        repository.delete(USER_ID);
        assertNull(repository.get(USER_ID));
    }

    @Test
    void deleteNotFound(){
        assertFalse(repository.delete(NOT_FOUND));
    }
}