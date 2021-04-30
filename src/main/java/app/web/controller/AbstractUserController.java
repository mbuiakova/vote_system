package app.web.controller;

import app.entity.User;
import app.repository.user.UserRepository;
import app.to.UserTo;
import app.util.UserUtil;
import app.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepository repository;

    public List<User> getAll(){
        log.info("getAll");
        return repository.getAll();
    }

    public User create(User user){
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return repository.save(user);
    }

    public User create(UserTo userTo){
        log.info("create {}", userTo);
        ValidationUtil.checkNew(userTo);
        return repository.save(UserUtil.createNewFromTo(userTo));
    }

    public User getById(int id){
        log.info("get {}", id);
        return repository.get(id);
    }

    public void delete(int id){
        log.info("delete {}", id);
        repository.delete(id);
    }

    public void update(User user){
        log.info("update {} with id={}", user, user.getId());
        repository.save(user);
    }

    public void update(UserTo userTo){
        log.info("update {}", userTo);
        repository.save(UserUtil.createNewFromTo(userTo));
    }

    public User getByEmail(String email){
        log.info("getByEmail {}", email);
        return repository.getByEmail(email);
    }


}
