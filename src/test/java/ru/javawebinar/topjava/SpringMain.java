package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class SpringMain {
    private static final Logger log = getLogger(SpringMain.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocations("spring/spring-app.xml", "spring/spring-db.xml");
        context.getEnvironment().setActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION, Profiles.getActiveDbProfile());
        context.refresh();

        try (context) {
            log.info("Bean definition names: " + Arrays.toString(context.getBeanDefinitionNames()));

            AdminRestController adminUserController = context.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealController = context.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess =
                    mealController.getBetween(
                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);
            log.info(mealController.getBetween(null, null, null, null).toString());
        }
    }
}
