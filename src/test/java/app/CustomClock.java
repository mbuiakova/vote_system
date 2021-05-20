package app;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomClock {

    public Clock clock;

    private final static LocalDateTime LOCAL_DATE = LocalDateTime.of(2021, 4, 20, 10, 0, 0);

    public CustomClock(Clock clock) {
        this.clock = Clock.fixed(LOCAL_DATE.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    public static Clock getClock() {
        return Clock.fixed(LOCAL_DATE.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
}
