package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private static final Pattern ADMIN_USERS_ID_PATTERN =
            Pattern.compile(".*/admin/users/(\\d+)");

    @Autowired(required = false)
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank() || userService == null) {
            return true;
        }

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return true;
        }

        String lowerEmail = email.toLowerCase();
        User existingUser;

        try {
            existingUser = userService.getByEmail(lowerEmail);
        } catch (NotFoundException e) {
            return true;
        }

        Integer targetUserId = getTargetUserId(attributes.getRequest());

        return targetUserId != null && targetUserId.equals(existingUser.getId());
    }

    private Integer getTargetUserId(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri.contains("/profile/register")) {
            return null;
        }

        if (uri.endsWith("/profile") || uri.endsWith("/profile/")) {
            AuthorizedUser authorizedUser = SecurityUtil.safeGet();
            return authorizedUser != null ? authorizedUser.getId() : null;
        }

        Matcher matcher = ADMIN_USERS_ID_PATTERN.matcher(uri);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        }

        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) {
            try {
                return Integer.valueOf(id);
            } catch (NumberFormatException ignored) {
            }
        }

        return null;
    }
}