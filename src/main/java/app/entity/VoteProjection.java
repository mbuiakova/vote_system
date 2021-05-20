package app.entity;

import java.time.LocalDate;

public interface VoteProjection {
    int getRestaurantId();

    LocalDate getDate();

    int getUserId();
}
