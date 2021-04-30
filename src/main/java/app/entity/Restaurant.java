package app.entity;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {

    @CollectionTable(name = "menu_history", joinColumns = @JoinColumn(name = "restaurant_id"))
    //@Column(name = "menu")
    @Columns(columns = {
            @Column(name = "date"), @Column(name = "menu")})
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @BatchSize(size = 50)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Menu> menus;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, Collection<Menu> menus) {
        super(id, name);
        setMenus(menus);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Collection<Menu> menus) {
        this.menus = CollectionUtils.isEmpty(menus) ? new ArrayList<>() : List.copyOf(menus);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
