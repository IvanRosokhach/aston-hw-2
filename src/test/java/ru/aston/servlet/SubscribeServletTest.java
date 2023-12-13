package ru.aston.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDto;
import ru.aston.service.SubscriptionService;
import ru.aston.service.impl.SubscriptionServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscribeServletTest {

    private HttpServletRequest req;
    private HttpServletResponse response;
    private SubscribeServlet servlet;
    private SubscriptionService service;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        service = mock(SubscriptionServiceImpl.class);
        mapper = new ObjectMapper();
        servlet = new SubscribeServlet(service, mapper);
    }

    @Test
    void doGet() throws IOException {
        String json = getNewUserJson();
        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();

        when(req.getParameter("id")).thenReturn("1");
        when(service.getSubscribers(anyLong())).thenReturn(List.of(dto));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(req, response);

//        Mockito.when(req.getReader()).thenReturn(getReader(json));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(service).getSubscribers(anyLong());
        assertEquals(mapper.writeValueAsString(List.of(dto)), writer.toString());

    }

    @Test
    void doPost() throws IOException {
        StringWriter writer = new StringWriter();
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("author-id")).thenReturn("2");
        when(service.add(anyLong(), anyLong())).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(req, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(service).add(anyLong(), anyLong());
    }

    @Test
    void doDelete() throws IOException {
        StringWriter writer = new StringWriter();
        when(req.getParameter("id")).thenReturn("1");
        when(req.getParameter("author-id")).thenReturn("2");
        when(service.remove(anyLong(), anyLong())).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(service).remove(anyLong(), anyLong());

    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("TestName")
                .login("TestLogin")
                .build();
    }

    private String getNewUserJson() throws JsonProcessingException {
        UserDto userDto = UserDto.builder()
                .name("TestName")
                .login("TestLogin")
                .build();

        return mapper.writeValueAsString(userDto);
    }

}