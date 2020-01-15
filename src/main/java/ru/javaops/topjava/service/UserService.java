package ru.javaops.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.to.UserTo;
import ru.javaops.topjava.util.UserUtil;

import java.util.List;

import static ru.javaops.topjava.util.ValidationUtil.checkNotFound;
import static ru.javaops.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        // check works only for JDBC, disabled
        //checkNotFoundWithId(repository.save(user), user.getId());
        repository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void update(UserTo userTo) {
        User user = get(userTo.id());
        repository.save(UserUtil.updateFromTo(user, userTo));
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void enable(int id, boolean enabled) {
        var user = get(id);
        user.setEnabled(enabled);
        repository.save(user); // !! need only for JDBC implementation
    }

    public User getWithMeals(int id) {
        return checkNotFoundWithId(repository.getWithMeals(id), id);
    }
}
