package app.web;

import app.entity.Vote;
import app.testData.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static app.testData.RestaurantsTestData.*;
import static app.testData.TestUtil.userHttpBasic;
import static app.testData.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"nextDayClock"})
class SaveVoteControllerTest extends AbstractControllerTest{

    private static final String REST_URL = "/votes/";

    @Test
    void saveVoteAnonymous() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1 + 2) + "/vote")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void saveVoteWithoutMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1 + 3) + "/vote")
                .with(userHttpBasic(user))).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void saveVoteAuth() throws Exception {
        Vote newVote = getNewVoteWithBaseDate();
        newVote.setRestaurantId(REST_ID_1 + 1);
        newVote.setDate(newVote.getDate().plusDays(1));
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1 + 1) + "/vote")
                .with(userHttpBasic(user))).andExpect(status().isCreated());

        Vote created = TestUtil.readFromJson(action, Vote.class);
        VOTE_TEST_MATCHER.assertMatch(created, newVote);
    }
}
