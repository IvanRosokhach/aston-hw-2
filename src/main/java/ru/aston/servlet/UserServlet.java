package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.dto.UserDto;
import ru.aston.service.UserService;
import ru.aston.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    UserService userService = new UserServiceImpl();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getQueryString();

        if (pathInfo == null || pathInfo.isEmpty()) {
            List<UserDto> allUsers = userService.findAll();

            String json = mapper.writeValueAsString(allUsers);
            createResponse(resp, json, SC_OK);

        } else {
            String userId = req.getParameter("id");
            if (userId.isEmpty()) {
                throw new RuntimeException("An empty value cannot be passed.");
            }

            long id = Long.parseLong(userId);
            UserDto userById = userService.findById(id);

            String json = mapper.writeValueAsString(userById);
            createResponse(resp, json, SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto convertedUser = mapper.readValue(req.getReader(), UserDto.class);
        UserDto user = userService.create(convertedUser);

        String json = mapper.writeValueAsString(user);
        createResponse(resp, json, SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto convertedUser = mapper.readValue(req.getInputStream(), UserDto.class);

        UserDto userDTO = userService.update(convertedUser);

        String json = mapper.writeValueAsString(userDTO);
        createResponse(resp, json, SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getQueryString();
        if (path != null) {
            String idString = path.substring(3);

            try {
                int userId = Integer.parseInt(idString);
                userService.delete(userId);
                createResponse(resp, "User delete done.", SC_OK);

            } catch (NumberFormatException e) {
                createResponse(resp, "Path should contain id=numbers.", SC_BAD_REQUEST);
            }
        }
    }

    private static void createResponse(HttpServletResponse resp, String body, int status) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        resp.getWriter().print(body);
    }

}
