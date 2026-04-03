package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private MealRepository repo;

    @Override
    public void init() throws ServletException {
        super.init();
        repo = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null || "list".equals(action)) {
            List<MealTo> meals = MealsUtil.getMealsWithExcess(
                    new ArrayList<>(repo.getAll()),
                    MealsUtil.CALORIES_PER_DAY
            );

            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, resp);

        } else if ("create".equals(action) || "edit".equals(action)) {
            Integer id = "edit".equals(action) ? Integer.parseInt(request.getParameter("id")) : null;
            request.setAttribute("meal", id != null ? repo.get(id) : null);
            request.getRequestDispatcher("/meal-form.jsp").forward(request, resp);

        } else if ("delete".equals(action)) {
            repo.delete(Integer.parseInt(request.getParameter("id")));
            resp.sendRedirect("meals");
        } else {
            request.setAttribute("meals", repo.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = parseId(request.getParameter("id"));
        LocalDateTime dt = LocalDateTime.parse(request.getParameter("dateTime"), FMT);
        String desc = request.getParameter("description");
        int cal = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(id, dt, desc, cal);
        Meal saved = repo.save(meal);

        resp.sendRedirect("meals");
    }

    private Integer parseId(String s) {
        return (s == null || s.isEmpty()) ? null : Integer.parseInt(s);
    }
}
