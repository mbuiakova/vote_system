package app.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Vote implements Serializable, VoteProjection {

    private static final long serialVersionUID = 1L;

    private int restaurantId;
    @NotNull
    private LocalDate date;
    private int userId;

    public Vote() {
    }

    public Vote(int restaurantId, LocalDate date, int userId) {
        this.restaurantId = restaurantId;
        this.date = date;
        this.userId = userId;
    }

    public Vote(Vote v){
        this.restaurantId = v.getRestaurantId();
        this.date = v.getDate();
        this.userId = v.getUserId();
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    @Override
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int rest_id) {
        this.restaurantId = rest_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return restaurantId == vote.restaurantId && userId == vote.userId && Objects.equals(date, vote.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, date, userId);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "restaurantId=" + restaurantId +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}
