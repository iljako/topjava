package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {

    private static final Map<Integer, Meal> map = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public InMemoryMealRepository() {
        for (Meal meal : MealsUtil.mealsList) {
            save(meal);
        }
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            int id = nextId.getAndIncrement();
            meal.setId(id);
        } else {
            if (!map.containsKey(meal.getId())) {
                throw new IllegalArgumentException("Meal with id=" + meal.getId() + " not found for update");
            }
        }

        map.put(meal.getId(), meal);
        return  meal;
    }

    @Override
    public Meal get(int id) {
        return map.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return map.values();
    }

    @Override
    public void delete(int id) {
        map.remove(id);
    }
}