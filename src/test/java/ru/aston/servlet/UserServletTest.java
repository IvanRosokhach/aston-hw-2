package ru.aston.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import ru.aston.dto.UserDto;
import ru.aston.service.UserService;
import ru.aston.service.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
class UserServletTest {

    //    @Mock
    private HttpServletRequest req;
    //    @Mock
    private HttpServletResponse response;
    private UserServlet servlet;
    //    @Mock
    private UserService service;
    private ObjectMapper mapper;

    @BeforeEach
    void setMocks() {


        req = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        service = mock(UserServiceImpl.class);
        mapper = new ObjectMapper();
        servlet = new UserServlet(service, mapper);
//        servlet = new UserServlet(service);
    }

    @Test
    void createUser() throws IOException {
        String json = getNewUserJson();
        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();

        when(req.getReader()).thenReturn(getReader(json));
        when(service.create(any(UserDto.class))).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));


        servlet.doPost(req, response);


        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(service).create(mapper.readValue(json, UserDto.class));
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void getAllUsers() throws IOException {
        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();

        when(req.getQueryString()).thenReturn(null);
        when(service.findAll()).thenReturn(List.of(dto));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));


//        Mockito.when(req.getParameter("id")).thenReturn(dto.getId().toString());
//        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(writer));
//        Mockito.when(service.findById(ArgumentMatchers.anyLong())).thenReturn(dto);

        servlet.doGet(req, response);


        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(mapper.writeValueAsString(List.of(dto)), writer.toString());

    }

    @Test
    void getUserById() throws IOException {
        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();

        when(req.getQueryString()).thenReturn("id=1");
        when(req.getParameter("id")).thenReturn("1");
//        Mockito.when(req.getParameter("firstName")).thenReturn(dto.getFirstName());
//        Mockito.when(req.getParameter("lastName")).thenReturn(dto.getLastName());
        when(service.findById(anyLong())).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
//        Mockito.when(service.findByName(dto.getFirstName(), dto.getLastName())).thenReturn(List.of(dto));

        servlet.doGet(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void updateUser() throws IOException {
        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();

        when(req.getReader()).thenReturn(getReader(mapper.writeValueAsString(dto)));
        when(service.update(ArgumentMatchers.any(UserDto.class))).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPut(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void removeUser() throws IOException {
//        UserDto dto = getUserDto();
        StringWriter writer = new StringWriter();
        when(req.getParameter("id")).thenReturn("1");
        when(service.deleteById(anyLong())).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    private BufferedReader getReader(String s) {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
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