package app.web.controller.user;

import app.entity.User;
import app.repository.user.UserRepository;
import app.web.AbstractControllerTest;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static app.testData.UserTestData.*;
import static app.testData.TestUtil.*;

@ActiveProfiles({"baseDateClock"})
class AdminRestControllerTest extends AbstractControllerTest {

    static final String REST_URL = "/admin/users/";

    @Autowired
    UserRepository repository;

    @Test
    void getAll_admin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(users));
    }

    @Test
    void getAll_user() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getById_admin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(user));
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNewUser();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_TEST_MATCHER.assertMatch(created, newUser);
        USER_TEST_MATCHER.assertMatch(repository.get(newId), newUser);
    }

    @Test
    void delete_ifAdmin() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk());
        USER_TEST_MATCHER.assertMatch(repository.getAll(), admin);
    }

    @Test
    void delete_ifUser() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(content().string(IsEmptyString.emptyOrNullString()));
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(admin));
    }

    @Test
    void update() throws Exception {
        User updated = getUpdatedUser();
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, "updated")))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_TEST_MATCHER.assertMatch(repository.get(USER_ID), getUpdatedUser());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}