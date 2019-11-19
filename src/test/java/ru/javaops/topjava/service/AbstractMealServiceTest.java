package ru.javaops.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.topjava.model.Meal;
import ru.javaops.topjava.util.exeption.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static ru.javaops.topjava.MealTestData.*;
import static ru.javaops.topjava.UserTestData.ADMIN_ID;
import static ru.javaops.topjava.UserTestData.USER_ID;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {
    @Autowired
    protected MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(0, USER_ID);
    }

    @Test
    public void create() {
        var newMeal = createNew();
        var created = service.create(newMeal, USER_ID);
        int newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() {
        var actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotOwn() {
        thrown.expect(NotFoundException.class);
        service.get(ADMIN_MEAL_ID, USER_ID);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        service.get(0, USER_ID);
    }

    @Test
    public void update() {
        var updated = createUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotOwn() {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        var meals = service.getAll(USER_ID);
        assertMatch(meals, MEALS);
    }

    @Test
    public void getBetween() {
        var meals = service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID
        );
        assertMatch(meals, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() {
        assertMatch(service.getBetweenDates(null, null, USER_ID), MEALS);
    }

    @Test
    public void testValidation() {
        var dateTime = of(2015, Month.JUNE, 1, 18, 0);
        testCreateValidation(new Meal(null, dateTime, "  ", 300));
        testCreateValidation(new Meal(null, null, "description", 300));
        testCreateValidation(new Meal(null, dateTime, "description", 9));
        testCreateValidation(new Meal(null, dateTime, "description", 5001));
    }

    private void testCreateValidation(Meal meal) {
        validateRootCause(() -> service.create(meal, USER_ID), ConstraintViolationException.class);
    }
}