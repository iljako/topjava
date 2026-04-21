package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> map = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        save(new User(null, "User", "user@yandex.ru", "password", Role.USER));
        save(new User(null, "Admin", "admin@gmail.com", "password", Role.ADMIN));
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.getAndIncrement());
            map.put(user.getId(), user);
            return user;
        }
        return map.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        return map.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return map.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return map.values().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return map.values().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }
}
