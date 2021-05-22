package app;

import app.testData.RestaurantsTestData;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

public class CustomClock {

    private final static LocalDate LOCAL_DATE = RestaurantsTestData.BASE_DATE_TIME.toLocalDate();

    public static Clock getBaseDateClock() {
        return Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    public static Clock getIncorrectClock() {
        return Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).plusHours(12).toInstant(), ZoneId.systemDefault());
    }

    public static Clock getNextDayClock() {
        return Clock.fixed(LOCAL_DATE.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
}
