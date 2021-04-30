package app.repository.user;

import app.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User save(User user);

    User get(int id);

    boolean delete(int id);

    User getByEmail(String email);

}
