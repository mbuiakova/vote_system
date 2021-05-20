package app.entity;

import java.time.LocalDate;

public interface MenuProjection {
    LocalDate getDate();

    String getMenu();
}
