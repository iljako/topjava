package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Test
    @Override
    public void createWithException() throws Exception {
        assertThatThrownBy(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new User(null, "User", "  ", "password", Role.USER)))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())))
                .isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())))
                .isInstanceOf(ConstraintViolationException.class);
    }
}