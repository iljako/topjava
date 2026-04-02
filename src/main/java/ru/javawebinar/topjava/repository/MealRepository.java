package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    void save(Meal meal);

    Meal get(Integer id);

    Collection<Meal> getAll();

    void delete(Integer id);
}