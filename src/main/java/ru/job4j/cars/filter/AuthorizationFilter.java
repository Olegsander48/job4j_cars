package ru.job4j.cars.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;

import java.io.IOException;

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();
        if (!isAlwaysForbidden(uri)) {
            chain.doFilter(request, response);
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getLogin() == null || user.getPassword() == null) {
            var loginPageUrl = request.getContextPath() + "/users/login";
            response.sendRedirect(loginPageUrl);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isAlwaysForbidden(String uri) {
        return uri.startsWith("/posts/edit")
                || uri.startsWith("/posts/delete")
                || uri.startsWith("/posts/create");
    }
}
