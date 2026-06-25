package ru.javawebinar.topjava.web.meal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService mealService;

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

        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
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

        Meal actual = mealService.get(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actual, updated);
    }

    @Test
    void getBetween() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", "2020-01-30")
                .param("startTime", "09:00")
                .param("endDate", "2020-01-31")
                .param("endTime", "20:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(MEAL_TOS_BETWEEN_1));
    }

    @Test
    void getBetweenAllNull() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + "between"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(ALL_MEAL_TOS));
    }

    @Test
    void getBetweenStartDateOnly() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", "2020-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(ALL_MEAL_TOS));
    }

    @Test
    void getBetweenWithEmptyStrings() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + "between")
                .param("startDate", "")
                .param("startTime", "")
                .param("endDate", "")
                .param("endTime", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(ALL_MEAL_TOS));
    }
}