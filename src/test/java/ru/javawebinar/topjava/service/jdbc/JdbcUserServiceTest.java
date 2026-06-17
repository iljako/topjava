package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Arrays;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private Environment env;

    @Override
    @Test
    public void createWithException() throws Exception {
        Assume.assumeFalse("Не поддерживается в профиле JDBC",
                Arrays.asList(env.getActiveProfiles()).contains(JDBC));
        super.createWithException();
    }
}