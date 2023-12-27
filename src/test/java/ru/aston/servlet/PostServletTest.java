package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import ru.aston.dto.PostDto;
import ru.aston.service.PostService;
import ru.aston.service.impl.PostServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.aston.TestObjectsBuilder.getPostDto;
import static ru.aston.TestObjectsBuilder.getReader;
import static ru.aston.util.ServletUtil.AUTHOR_ID;
import static ru.aston.util.ServletUtil.ID;

class PostServletTest {

    private HttpServletRequest req;
    private HttpServletResponse response;
    private PostServlet servlet;
    private PostService service;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        service = mock(PostServiceImpl.class);
        mapper = new ObjectMapper();
        servlet = new PostServlet(service, mapper);
    }

    @Test
    void doGet_whenParameterAuthorIdExist_thenOk() throws IOException {
        PostDto dto = getPostDto();
        StringWriter writer = new StringWriter();

        when(req.getParameter(AUTHOR_ID)).thenReturn("1");
        when(req.getParameter(ID)).thenReturn(null);
        when(service.findByAuthorId(anyLong())).thenReturn(List.of(dto));
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(service).findByAuthorId(anyLong());
        assertEquals(mapper.writeValueAsString(List.of(dto)), writer.toString());
    }

    @Test
    void doGet_whenParameterIdExist_thenOk() throws IOException {
        PostDto dto = getPostDto();
        StringWriter writer = new StringWriter();

        when(req.getParameter(AUTHOR_ID)).thenReturn(null);
        when(req.getParameter(ID)).thenReturn("1");
        when(service.findById(anyLong())).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(service).findById(anyLong());
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void doPost() throws IOException {
        String json = mapper.writeValueAsString(getPostDto());
        PostDto dto = getPostDto();
        StringWriter writer = new StringWriter();

        when(req.getReader()).thenReturn(getReader(json));
        when(service.create(any(PostDto.class))).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));


        servlet.doPost(req, response);


        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(service).create(mapper.readValue(json, PostDto.class));
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void doPut() throws IOException {
        PostDto dto = getPostDto();
        StringWriter writer = new StringWriter();

        when(req.getReader()).thenReturn(getReader(mapper.writeValueAsString(dto)));
        when(service.update(ArgumentMatchers.any(PostDto.class))).thenReturn(dto);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPut(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(mapper.writeValueAsString(dto), writer.toString());
    }

    @Test
    void doDelete() throws IOException {
        StringWriter writer = new StringWriter();
        when(req.getParameter(ID)).thenReturn("1");
        when(service.deleteById(anyLong())).thenReturn(true);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doDelete(req, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

}