package ru.javawebinar.topjava.web.meal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Test
    void get() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getNotFound() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNotFound() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        setAuthUserId(USER_ID);
        List<MealTo> expected = MealsUtil.getTos(meals, UserTestData.user.getCaloriesPerDay());
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(expected));
    }

    @Test
    void create() throws Exception {
        setAuthUserId(USER_ID);
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", Matchers.containsString(REST_URL)));

        Meal created = MEAL_MATCHER.readFromJson(action);
        newMeal.setId(created.id());
        MEAL_MATCHER.assertMatch(created, newMeal);
    }

    @Test
    void update() throws Exception {
        setAuthUserId(USER_ID);
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(MEAL_MATCHER.contentJson(updated));
    }

    @Test
    void getBetween() throws Exception {
        setAuthUserId(USER_ID);

        String startDateStr = "2020-01-30";
        String startTimeStr = "09:00";
        String endDateStr = "2020-01-31";
        String endTimeStr = "20:00";

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        List<Meal> mealsByDate = meals.stream()
                .filter(m -> (m.getDateTime().toLocalDate().isEqual(startDate) || m.getDateTime().toLocalDate().isAfter(startDate))
                        && (m.getDateTime().toLocalDate().isEqual(endDate) || m.getDateTime().toLocalDate().isBefore(endDate)))
                .collect(Collectors.toList());

        List<MealTo> expected = MealsUtil.getFilteredTos(mealsByDate, UserTestData.user.getCaloriesPerDay(), startTime, endTime);

        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", startDateStr)
                .param("startTime", startTimeStr)
                .param("endDate", endDateStr)
                .param("endTime", endTimeStr))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(expected));
    }
}