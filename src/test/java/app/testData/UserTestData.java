package app.testData;

import app.entity.User;

import java.util.List;

public class UserTestData {

    public static final TestMatcher<User> USER_TEST_MATCHER = TestMatcher.usingEqualsComparator(User.class);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int USER_NOT_FOUND = 10258;

    public static final User user = new User(USER_ID, "User Userovich", "user@domain.com", "123456", false);
    public static final User admin = new User(ADMIN_ID, "Admin Adminovich", "admin@domain.com", "654321", true);
    //public static final User user2 = new User(USER_ID + 2, "AAAA aaaaa", "user1@domain.com", "123456A", false);

    public static final List<User> users = List.of(user, admin);

    public static User getNewUser(){
        return new User(null, "John Smith", "lala@email.com", "lalala", false);
    }

    public static User getUpdatedUser(){
        User updated = new User(user);
        updated.setEmail("updated@email.com");
        updated.setPassword("updated");
        updated.setName("Updated updated");
        updated.setAdmin(true);
        return updated;
    }

}
