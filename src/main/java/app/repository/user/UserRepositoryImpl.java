package app.repository.user;

import app.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJPA repository;

    public UserRepositoryImpl(UserRepositoryJPA repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User save(User user) {
       return repository.save(user);
    }

    @Override
    public User get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    @Override
    public User getByEmail(String email){
        return repository.getByEmail(email);
    }

}
