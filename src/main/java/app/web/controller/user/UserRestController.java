package app.web.controller.user;

import app.entity.User;
import app.to.UserTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import app.util.AuthorizedUser;

import javax.validation.Valid;
import java.net.URI;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends AbstractUserController {
    static final String REST_URL = "/profile";

    @GetMapping
    public User get(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser){
        return super.getById(authUser.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser){
        super.delete(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo){
        User created = super.create(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal @ApiIgnore AuthorizedUser authUser) throws BindException {
        validateBeforeUpdate(userTo, authUser.getId());
        super.update(userTo);
    }
}
