package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1, USER_ID));
    }

    @Test
    public void create() {
        Meal created = getNew();
        Meal newMeal = service.create(created, USER_ID);
        Assert.assertNotNull(newMeal);
        Assert.assertNotNull(newMeal.getId());
        assertMatch(newMeal, created);
    }

    @Test
    public void get() {
        Meal actual = service.get(MEAL1_ID, USER_ID);
        assertMatch(actual, MEAL1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(1, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.update(new Meal(1, LocalDate.now().atTime(10,00), "", 100), USER_ID));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2026, 1, 30),
                        LocalDate.of(2026, 1, 31),
                        USER_ID),
                MEALS_FILTERED);
    }

    @Test
    public void getBetweenInclusiveNullDates() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}