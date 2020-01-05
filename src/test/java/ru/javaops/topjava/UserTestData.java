package ru.javaops.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javaops.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_USER, Role.ROLE_ADMIN);

    public static User createNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
    }

    public static User createUpdated() {
        var updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setRoles(Set.of(Role.ROLE_ADMIN));
        return updated;
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "meals");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "meals").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return result -> assertMatch(TestUtil.readListFromJsonMvcResult(result, User.class), expected);
    }

    public static ResultMatcher contentJson(User expected) {
        return result -> assertMatch(TestUtil.readFromJsonMvcResult(result, User.class), expected);
    }
}