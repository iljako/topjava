package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        if (repository.save(meal, userId) == null) {
            throw new NotFoundException("Not found meal with id=" + meal.getId());
        }
    }

    public void delete(int id, int userId) {
        if (!repository.delete(id, userId)) {
            throw new NotFoundException("Not found meal with id=" + id);
        }
    }

    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        if (meal == null) {
            throw new NotFoundException("Not found meal with id=" + id);
        }
        return meal;
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getAllFiltered(int userId, LocalDate start, LocalDate end) {
        return repository.getAllFiltered(userId, start, end);
    }
}