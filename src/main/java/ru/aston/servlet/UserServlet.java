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
import static ru.aston.util.ServletUtil.ERROR_PARAMETER_ID;
import static ru.aston.util.ServletUtil.createResponse;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;

    public UserServlet() {
        this.userService = new UserServiceImpl();
        this.mapper = new ObjectMapper();
    }

    public UserServlet(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.mapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getQueryString();
        if (pathInfo == null || pathInfo.isEmpty()) {
            List<UserDto> allUsers = userService.findAll();

            String json = mapper.writeValueAsString(allUsers);
            createResponse(resp, json, SC_OK);

        } else {
            String userId = req.getParameter("id");
            if (!userId.isEmpty()) {
                long id = Long.parseLong(userId);
                UserDto userById = userService.findById(id);

                String json = mapper.writeValueAsString(userById);
                createResponse(resp, json, SC_OK);
            } else {
                createResponse(resp, ERROR_PARAMETER_ID, SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto convertedUser = mapper.readValue(req.getReader(), UserDto.class);
        UserDto userDto = userService.create(convertedUser);

        String json = mapper.writeValueAsString(userDto);
        createResponse(resp, json, SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDto convertedUser = mapper.readValue(req.getReader(), UserDto.class);

        UserDto userDto = userService.update(convertedUser);

        String json = mapper.writeValueAsString(userDto);
        createResponse(resp, json, SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id == null) {
            createResponse(resp, ERROR_PARAMETER_ID, SC_BAD_REQUEST);
        }

        long userId = Long.parseLong(id);
        if (userService.deleteById(userId)) {
            createResponse(resp, "User successfully deleted.", SC_OK);
        } else {
            createResponse(resp, "User for delete not found.", SC_BAD_REQUEST);
        }
    }

}
