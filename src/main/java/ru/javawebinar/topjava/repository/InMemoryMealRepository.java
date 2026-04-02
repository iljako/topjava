package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMealRepository implements MealRepository {

    private static final Map<Integer, Meal> map = new ConcurrentHashMap<>();
    private static int nextId = 1;

    private static InMemoryMealRepository instance;

    private InMemoryMealRepository() {
        for (Meal meal : MealsUtil.mealsList) {
            save(meal);
        }
    }

    public static synchronized InMemoryMealRepository getInstance() {
        if (instance == null) instance = new InMemoryMealRepository();
        return instance;
    }


    @Override
    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(nextId++);
        }
        map.put(meal.getId(), meal);
    }

    @Override
    public Meal get(Integer id) {
        return map.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return map.values();
    }

    @Override
    public void delete(Integer id) {
        map.remove(id);
    }
}