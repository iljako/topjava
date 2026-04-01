package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        List<Meal> mealList = MealsUtil.MEALS;
        int caloriesPerDay = MealsUtil.CALORIES_PER_DAY;

        Map<LocalDate, Integer> sumCaloriesPerDay = mealList.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(Meal::getCalories)
                ));

        List<MealTo> mealToList = new ArrayList<>();

        for (Meal meal : mealList) {
            int daySum = sumCaloriesPerDay.get(meal.getDateTime().toLocalDate());
            boolean excess = daySum > caloriesPerDay;

            mealToList.add(new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
        }

        request.setAttribute("meals", mealToList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        //response.sendRedirect("meals.jsp");
    }
}
