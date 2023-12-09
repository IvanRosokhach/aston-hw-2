package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.dto.UserDto;
import ru.aston.service.SubscriptionService;
import ru.aston.service.impl.SubscriptionServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@WebServlet("/subscription")
public class SubscribeServlet extends HttpServlet {

    SubscriptionService subscribeService = new SubscriptionServiceImpl();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id != null) {
            long userId = Long.parseLong(id);
            List<UserDto> subscribers = subscribeService.getSubscribers(userId);

            String json = mapper.writeValueAsString(subscribers);
            createResponse(resp, json, SC_CREATED);
        } else {
            throw new RuntimeException("An empty value cannot be passed.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String id2 = req.getParameter("author-id");
        if (id != null && id2 != null) {
            long userId = Long.parseLong(id);
            long authorId = Long.parseLong(id2);
            subscribeService.add(userId, authorId);
            createResponse(resp, "subscribed to the author", SC_OK);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String id2 = req.getParameter("author-id");
        if (id != null && id2 != null) {
            long userId = Long.parseLong(id);
            long authorId = Long.parseLong(id2);
            subscribeService.remove(userId, authorId);
            createResponse(resp, "unsubscribed from the author", SC_OK);
        }
    }

    private static void createResponse(HttpServletResponse resp, String body, int status) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        resp.getWriter().print(body);
    }

}
