package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext springContext;
    private MealRestController controller;

    @Override
    public void init() {
        springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = springContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        if (springContext != null) {
            springContext.close();
        }
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = SecurityUtil.authUserId();
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            controller.create(meal);
        } else {
            log.info("Update {}", meal);
            controller.update(meal, Integer.parseInt(id));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int userId = SecurityUtil.authUserId();

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                String startDateParam = request.getParameter("startDate");
                String startTimeParam = request.getParameter("startTime");
                String endDateParam = request.getParameter("endDate");
                String endTimeParam = request.getParameter("endTime");
                List<MealTo> meals;

                if (startDateParam != null || startTimeParam != null ||
                        endDateParam != null || endTimeParam != null) {

                    LocalDate startDate = (startDateParam != null && !startDateParam.isEmpty()) ?
                            LocalDate.parse(startDateParam) : null;
                    LocalTime startTime = (startTimeParam != null && !startTimeParam.isEmpty()) ?
                            LocalTime.parse(startTimeParam) : null;
                    LocalDate endDate = (endDateParam != null && !endDateParam.isEmpty()) ?
                            LocalDate.parse(endDateParam) : null;
                    LocalTime endTime = (endTimeParam != null && !endTimeParam.isEmpty()) ?
                            LocalTime.parse(endTimeParam) : null;
                    log.info("getAll Filtered");
                    meals = controller.getAllFiltered(startDate, startTime, endDate, endTime);
                } else {
                    log.info("getAll");
                    meals = controller.getAll();
                }
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
