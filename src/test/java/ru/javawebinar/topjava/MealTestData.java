package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;
    public static final int MEAL4_ID = START_SEQ + 6;
    public static final int MEAL5_ID = START_SEQ + 7;
    public static final int MEAL6_ID = START_SEQ + 8;
    public static final int MEAL7_ID = START_SEQ + 9;

    public static final Meal MEAL1 = new Meal(MEAL1_ID, LocalDate.of(2026, Month.JANUARY, 30).atTime(10, 0), "Завтрак", 500);
    public static final Meal MEAL2 = new Meal(MEAL2_ID, LocalDate.of(2026, Month.JANUARY, 30).atTime(13, 0), "Обед", 1000);
    public static final Meal MEAL3 = new Meal(MEAL3_ID, LocalDate.of(2026, Month.JANUARY, 30).atTime(20, 0), "Ужин", 500);
    public static final Meal MEAL4 = new Meal(MEAL4_ID, LocalDate.of(2026, Month.JANUARY, 31).atTime(0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL5 = new Meal(MEAL5_ID, LocalDate.of(2026, Month.JANUARY, 31).atTime(10, 0), "Завтрак", 1000);
    public static final Meal MEAL6 = new Meal(MEAL6_ID, LocalDate.of(2026, Month.JANUARY, 31).atTime(13, 0), "Обед", 500);
    public static final Meal MEAL7 = new Meal(MEAL7_ID, LocalDate.of(2026, Month.JANUARY, 31).atTime(20, 0), "Ужин", 410);

    public static final List<Meal> MEALS = Collections.unmodifiableList(Arrays.asList(
            MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1
    ));

    public static final List<Meal> MEALS_FILTERED = Collections.unmodifiableList(Arrays.asList(
            MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1
    ));

    public static Meal getNew() {
        return new Meal(null, LocalDate.of(2026, Month.JANUARY, 1).atTime(18, 0), "Новый прием пищи", 300);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, MEAL1.getDateTime(), "Обновленный завтрак", 400);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}