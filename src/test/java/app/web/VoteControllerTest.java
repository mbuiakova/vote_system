package app.web;

import app.entity.Vote;
import app.repository.restaurant.RestaurantRepository;
import app.testData.TestUtil;
import app.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static app.testData.RestaurantsTestData.*;
import static app.testData.TestUtil.userHttpBasic;
import static app.testData.UserTestData.user;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"baseDateClock"})
class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/votes/";

    @Autowired
    private RestaurantRepository repository;

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
        perform(MockMvcRequestBuilders.get(REST_URL + REST_ID_1 + "/votes")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TEST_MATCHER.contentJson(List.of(vote1)));
    }

    @Test
    void updateVoteBeforeTime() throws Exception {
        Vote updated = getUpdatedVote();//2021, 4, 20

        perform(MockMvcRequestBuilders.put(REST_URL + (REST_ID_1 + 1) + "/vote")
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
