package app.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class Menu implements Serializable, MenuProjection {

    private static final long serialVersionUID = 1L;

    @NotNull
    private LocalDate date;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 1024)
    private String menu;

    public Menu() {
    }

    public Menu(LocalDate date, String menu) {
        this.date = date;
        this.menu = menu;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate dateTime) {
        this.date = dateTime;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu1 = (Menu) o;
        return Objects.equals(date, menu1.date) && Objects.equals(menu, menu1.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, menu);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                ", menu='" + menu + '\'' +
                '}';
    }
}
