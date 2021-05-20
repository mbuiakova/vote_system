package app.web;

import app.entity.Menu;
import app.repository.restaurant.RestaurantRepository;
import app.testData.TestUtil;
import app.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.util.List;

import static app.testData.RestaurantsTestData.*;
import static app.testData.TestUtil.userHttpBasic;
import static app.testData.UserTestData.admin;
import static app.testData.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/menus/";

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private Clock clock;

    @Test
    void createMenu_ifAdmin() throws Exception {
        Menu newMenu = getNewMenu();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1 + 2) + "/menu" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu.getMenu()))
                .with(userHttpBasic(admin))).andExpect(status().isCreated());

        Menu created = TestUtil.readFromJson(action, Menu.class);
        MENU_TEST_MATCHER.assertMatch(created, newMenu);
    }

    @Test
    void createMenu_ifUser() throws Exception {
        Menu newMenu = getNewMenu();
        perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1 + 2) + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu.getMenu()))
                .with(userHttpBasic(user))).andExpect(status().isForbidden());
    }

    @Test
    void createMenu_ifExist() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + (REST_ID_1) +"/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu1_3.getMenu()))
                .with(userHttpBasic(user))).andExpect(status().isForbidden());//403
    }

    @Test
    void getMenuByDateForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_ID_1 + "/menu")
                .param("date", "2021-04-20")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TEST_MATCHER.contentJson(List.of(menu1_3)));
    }

    @Test
    void getAllMenusForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + (REST_ID_1 +1) +"/menus")
        .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TEST_MATCHER.contentJson(menus_rest_2));
    }

}
