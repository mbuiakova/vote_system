package app.web;

import app.entity.Restaurant;
import app.entity.Vote;
import app.repository.restaurant.RestaurantRepository;
import app.testData.TestUtil;
import app.testData.UserTestData;
import app.web.json.JsonUtil;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static app.testData.RestaurantsTestData.*;
import static app.testData.TestUtil.*;
import static app.testData.UserTestData.*;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/";

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private Clock clock;

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
    void deleteRestaurantIfAdmin() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST_ID_1)
                .with(TestUtil.userHttpBasic(admin)))
                .andExpect(status().isOk());
        Assertions.assertNull(repository.getById(REST_ID_1));
    }

    @Test
    void deleteRestaurantIfUser() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + REST_ID_1)
                .with(TestUtil.userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(TestUtil.userHttpBasic(admin)))
                .andExpect(content().string(IsEmptyString.emptyOrNullString()));
    }

    @Test
    void updateRestaurantIfAdmin() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + REST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(admin))).andExpect(status().isOk());

        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(REST_ID_1), updated);
    }

    @Test
    void updateRestaurantIfUser() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + REST_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(user))).andExpect(status().isForbidden());
    }

    @Test
    void createRestaurantAdmin() throws Exception {
        Restaurant newR = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newR))
                .with(TestUtil.userHttpBasic(admin))).andExpect(status().isCreated());

        Restaurant created = TestUtil.readFromJson(action, Restaurant.class);
        int newId = created.id();
        newR.setId(newId);
        RESTAURANT_TEST_MATCHER.assertMatch(created, newR);
        RESTAURANT_TEST_MATCHER.assertMatch(repository.getById(newId), newR);
    }

    @Test
    void createRestaurantUser() throws Exception {
        Restaurant newR = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newR))
                .with(TestUtil.userHttpBasic(user))).andExpect(status().isForbidden());
    }

    @Test
    void saveVoteAuth() throws Exception {
        Vote newVote = getNewVoteWithBaseDate();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "vote/" + (REST_ID_1 + 2))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))).andExpect(status().isCreated());//TestUtil.userAuth(UserTestData.user)

        Vote created = TestUtil.readFromJson(action, Vote.class);
        VOTE_TEST_MATCHER.assertMatch(created, newVote);
    }

    @Test
    void saveVoteAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "vote/" + (REST_ID_1 + 2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TEST_MATCHER.contentJson(votes));
    }

    @Test
    void getVotesForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes/" + REST_ID_1)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TEST_MATCHER.contentJson(List.of(vote1)));
    }

    @Test
    void updateVote() throws Exception {
        Vote updated = getUpdatedVote();//2021, 4, 20

        perform(MockMvcRequestBuilders.put(REST_URL + "vote/" + (REST_ID_1+1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(user))).andExpect(status().isOk());

        List<Vote> votes = repository.getVotesForRestaurant(updated.getRestaurantId())
                .stream()
                .filter(e -> e.getDate().equals(updated.getDate()) && e.getUserId() == updated.getUserId())
                .collect(Collectors.toList());
        assertFalse(votes.isEmpty());
    }
}
