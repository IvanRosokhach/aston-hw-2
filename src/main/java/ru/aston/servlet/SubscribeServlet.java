package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.aston.dto.UserDto;
import ru.aston.service.SubscriptionService;
import ru.aston.service.impl.SubscriptionServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static ru.aston.util.ServletUtil.AUTHOR_ID;
import static ru.aston.util.ServletUtil.ERROR_PARAMETER_ID;
import static ru.aston.util.ServletUtil.ERROR_PARAMETER_ID_AND_AUTHOR_ID;
import static ru.aston.util.ServletUtil.ID;
import static ru.aston.util.ServletUtil.createResponse;

@AllArgsConstructor
@WebServlet("/subscription")
public class SubscribeServlet extends HttpServlet {

    private final SubscriptionService subscribeService;
    private final ObjectMapper mapper;

    public SubscribeServlet() {
        this.subscribeService = new SubscriptionServiceImpl();
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        if (id != null) {
            long userId = Long.parseLong(id);
            List<UserDto> subscribers = subscribeService.getSubscribers(userId);

            String json = mapper.writeValueAsString(subscribers);
            createResponse(resp, json, SC_OK);
        } else {
            createResponse(resp, ERROR_PARAMETER_ID, SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        String id2 = req.getParameter(AUTHOR_ID);
        if (id == null || id2 == null) {
            createResponse(resp, ERROR_PARAMETER_ID_AND_AUTHOR_ID, SC_BAD_REQUEST);
        }

        long userId = Long.parseLong(id);
        long authorId = Long.parseLong(id2);

        if (subscribeService.add(userId, authorId)) {
            createResponse(resp, "Subscribed to the author.", SC_CREATED);
        } else {
            createResponse(resp, "Failed to subscribe to the author.", SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        String id2 = req.getParameter(AUTHOR_ID);
        if (id == null || id2 == null) {
            createResponse(resp, ERROR_PARAMETER_ID_AND_AUTHOR_ID, SC_BAD_REQUEST);
        }

        long userId = Long.parseLong(id);
        long authorId = Long.parseLong(id2);

        if (subscribeService.remove(userId, authorId)) {
            createResponse(resp, "Unsubscribed from the author.", SC_OK);
        } else {
            createResponse(resp, "Failed to unsubscribe from the author.", SC_BAD_REQUEST);
        }
    }

}
