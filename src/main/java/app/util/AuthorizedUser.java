package app.util;

import app.entity.User;
import app.to.UserTo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User{

    //@Serial java15
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(Collection<? extends GrantedAuthority> authorities, User user) {
        super(user.getName(), user.getPassword(), authorities);
        this.userTo = UserUtil.asTo(user);
    }

    public AuthorizedUser(User user) {
        super(user.getName(), user.getPassword(),true, true, true, true, AuthorizedUser.getRoles(user));
        this.userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.id();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }

    public static Set<Role> getRoles(User user) {
        Set<Role> roles = new HashSet<Role>();
        roles.add(user.isAdmin() ? new Role(true) : new Role(false));
        return roles;
    }
}
