package app.web;

import app.entity.Restaurant;
import app.entity.Vote;
import app.repository.restaurant.RestaurantRepository;
import app.testData.RestaurantsTestData;
import app.testData.TestUtil;
import app.testData.UserTestData;
import app.web.json.JsonUtil;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static app.testData.RestaurantsTestData.*;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/";

    @Autowired
    private RestaurantRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_ID_1)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TEST_MATCHER.contentJson(rest_1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TEST_MATCHER.contentJson(restaurants));
    }

    @Test
    void getAllWithMenus() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "allWithMenus")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TEST_MATCHER.contentJson(restaurants));
    }

    @Test
    void getByIdWithMenus() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_ID_1 + "/withMenus")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TEST_MATCHER.contentJson(rest_1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST_ID_1)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk());
        Assertions.assertNull(repository.getById(REST_ID_1));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(content().string(IsEmptyString.emptyOrNullString()));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + REST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(UserTestData.user))).andExpect(status().isOk());

        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(REST_ID_1), updated);
    }

    @Test
    void create() throws Exception {
        Restaurant newR = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newR))
                .with(TestUtil.userHttpBasic(UserTestData.user)));

        Restaurant created = TestUtil.readFromJson(action, Restaurant.class);
        int newId = created.id();
        newR.setId(newId);
        RESTAURANT_TEST_MATCHER.assertMatch(created, newR);
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(newId), newR);
    }

    @Test
    void saveVote() throws Exception {
        Vote newVote = getNewVote();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "vote/" + (REST_ID_1 + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote))
                .with(TestUtil.userAuth(UserTestData.user)));

        Vote created = TestUtil.readFromJson(action, Vote.class);
        VOTE_TEST_MATCHER.assertMatch(created, newVote);


    }
}
