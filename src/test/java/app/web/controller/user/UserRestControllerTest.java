package app.web.controller.user;

import app.entity.User;
import app.repository.user.UserRepository;
import app.testData.TestUtil;
import app.to.UserTo;
import app.util.UserUtil;
import app.web.AbstractControllerTest;
import app.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static app.exception.ErrorType.VALIDATION_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static app.testData.UserTestData.*;
import static app.testData.TestUtil.*;

class UserRestControllerTest extends AbstractControllerTest {

    static final String REST_URL = "/profile";

    @Autowired
    UserRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_TEST_MATCHER.contentJson(user));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL).with(userHttpBasic(user))).andExpect(status().isOk());
        USER_TEST_MATCHER.assertMatch(repository.getAll(), admin);
    }

    @Test
    void save() throws Exception {
        UserTo userTo = new UserTo(null, "Name Name", "newemail@email.com", "lklklklk");
        User newUser = UserUtil.createNewFromTo(userTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_TEST_MATCHER.assertMatch(created, newUser);
        USER_TEST_MATCHER.assertMatch(repository.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        UserTo updated = new UserTo(null, "Updated Name", "newemail@email.com", "lklklklk");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());

        USER_TEST_MATCHER.assertMatch(repository.get(USER_ID), UserUtil.updateFromTo(new User(user), updated));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
//                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
//                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "User Userovich", "user@domain.com", "123456");

        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
//                .andExpect(errorType(VALIDATION_ERROR));
//                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
    }
}