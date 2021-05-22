package app.web;

import app.entity.Vote;
import app.testData.TestUtil;
import app.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static app.testData.RestaurantsTestData.REST_ID_1;
import static app.testData.RestaurantsTestData.getUpdatedVote;
import static app.testData.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"incorrectClock"})
class UpdateVoteControllerTest extends AbstractControllerTest{

    private static final String REST_URL = "/votes/";

    @Test
    void updateVoteAfterTime() throws Exception {
        Vote updated = getUpdatedVote();

        perform(MockMvcRequestBuilders.put(REST_URL + (REST_ID_1 + 1) + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(user))).andExpect(status().isUnprocessableEntity());
    }
}
