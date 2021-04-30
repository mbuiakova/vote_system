package app.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate date;
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
}
