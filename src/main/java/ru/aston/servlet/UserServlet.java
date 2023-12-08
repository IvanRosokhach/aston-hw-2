package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.dto.UserDto;
import ru.aston.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    UserService userService = new UserService();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("id");

        if (userId.isEmpty()) {
            throw new RuntimeException("An empty value cannot be passed.");
        }

        Long id = Long.parseLong(userId);
        UserDto userById = userService.findById(id);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(mapper.writeValueAsString(userById));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        System.out.println("path info = " + pathInfo);

        UserDto convertedUser = mapper.readValue(req.getReader(), UserDto.class);
        UserDto user = userService.create(convertedUser);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter out = resp.getWriter();
        out.print(mapper.writeValueAsString(user));
//        out.write(mapper.writeValueAsString(user));
//        out.flush();
    }

}
