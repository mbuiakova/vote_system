package app.testData;

import app.entity.AbstractBaseEntity;
import app.entity.Menu;
import app.entity.Restaurant;
import app.entity.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RestaurantsTestData {

    public static final TestMatcher<Restaurant> RESTAURANT_TEST_MATCHER = TestMatcher.usingEqualsComparator(Restaurant.class);

    public static final TestMatcher<Vote> VOTE_TEST_MATCHER = TestMatcher.usingEqualsComparator(Vote.class);

    public static final LocalDateTime baseDate = LocalDateTime.of(2021, 4, 20, 10, 0, 0);
    //public static final LocalDateTime datetimeBefore = LocalDateTime.of(2021, 4, 25, 10, 59, 0);
    public static final LocalDateTime datetimeAfter = LocalDateTime.of(2021, 4, 20, 11, 1, 0);

    public static final int REST_ID_1 = AbstractBaseEntity.START_SEQ + 2;//3
    public static final int NOT_FOUND = 100003;

    public static final Restaurant rest_1 = new Restaurant(REST_ID_1, "Dominos", List.of(new Menu(LocalDate.of(2021, 4, 20), "item, item2, item3, drink, dessert")));
    public static final Restaurant rest_2 = new Restaurant(REST_ID_1 +1, "TrackFood", List.of(new Menu(LocalDate.of(2021, 4, 20), "pizza, pizza, pizza, cola, sugar"), new Menu(LocalDate.of(2021, 04, 21), "bread, salad, cheese, cola")));
    public static final Restaurant rest_3 = new Restaurant(REST_ID_1 + 2, "PapaPizza");

    public static final List<Restaurant> restaurants = List.of(rest_1, rest_2, rest_3);

    public static final Vote vote1 = new Vote(REST_ID_1, LocalDate.of(2021, 4, 20), UserTestData.USER_ID);
    public static final Vote vote2 = new Vote(REST_ID_1 + 1, LocalDate.of(2021, 4, 20), UserTestData.ADMIN_ID);

    public static final List<Vote> votes = List.of(vote1, vote2);

    public static Restaurant getNew(){
        return new Restaurant(null, "New restaurant");
    }

    public static Restaurant getUpdated(){
        return new Restaurant(REST_ID_1, rest_1.getName() + " Updated");
    }

    public static Vote getNewVote(){
        return new Vote(rest_3.id(), baseDate.toLocalDate(), UserTestData.USER_ID);
    }

    public static Vote getUpdatedVote(){
        return new Vote(REST_ID_1 +1, vote1.getDate(), vote1.getUserId());
    }

}
