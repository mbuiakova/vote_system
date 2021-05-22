package app.web.controller.user;

import app.HasId;
import app.entity.User;
import app.repository.user.UserRepository;
import app.service.UserService;
import app.to.UserTo;
import app.util.UserUtil;
import app.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected UserService service;
    @Autowired
    UserRepository repository;
    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public List<User> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return repository.save(user);
    }

    public User create(UserTo userTo) {
        log.info("create {}", userTo);
        ValidationUtil.checkNew(userTo);
        return repository.save(UserUtil.createNewFromTo(userTo));
    }

    public User getById(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.delete(id);
    }

    public void update(User user) throws BindException {
        log.info("update {} with id={}", user, user.getId());
        ValidationUtil.assureIdConsistent(user, user.id());
        repository.save(user);
    }

    public void update(UserTo userTo) throws BindException {
        log.info("update {}", userTo);
        ValidationUtil.assureIdConsistent(userTo, userTo.id());
        repository.save(UserUtil.createNewFromTo(userTo));
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.getByEmail(email);
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        ValidationUtil.assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, validator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }


}
