package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get id={} for userId={}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create for userId={}", userId);
        ValidationUtil.checkIsNew(meal);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update id={} for userId={}", id, userId);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete id={} for userId={}", id, userId);
        service.delete(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for userId={}", userId);
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        List<Meal> meals = service.getAll(userId);
        return MealsUtil.getTos(meals, caloriesPerDay);
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        log.info("getAllFiltered start={}, end={}, userId={}", startDate, endDate, userId);
        return MealsUtil.getTos(service.getAllFiltered(userId, startDate, endDate), caloriesPerDay);
    }
}