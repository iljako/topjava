package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) return true;

        var authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null && email.equalsIgnoreCase(authorizedUser.getUserTo().getEmail())) {
            return true;
        }

        try {
            userService.getByEmail(email.toLowerCase());
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}