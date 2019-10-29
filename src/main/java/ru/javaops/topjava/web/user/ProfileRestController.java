package ru.javaops.topjava.web.user;

import ru.javaops.topjava.model.User;

import static ru.javaops.topjava.web.SecurityUtil.authUserId;

public class ProfileRestController extends AbstractUserController {
    public User get() {
        return super.get(authUserId());
    }

    public void delete() {
        super.delete(authUserId());
    }

    public void update(User user) {
        super.update(user, authUserId());
    }
}
