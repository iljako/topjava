package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import javax.validation.ConstraintViolationException;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
    @Test
    @Override
    public void createWithException() throws Exception {
        assertThatThrownBy(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "  ", 300), USER_ID))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new Meal(null, null, "Description", 300), USER_ID))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 9), USER_ID))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID))
                .isInstanceOf(ConstraintViolationException.class);
    }
}