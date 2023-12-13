package ru.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.dto.PostDto;
import ru.aston.service.PostService;
import ru.aston.service.impl.PostServiceImpl;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static ru.aston.util.ServletUtil.ERROR_PARAMETER_ID;
import static ru.aston.util.ServletUtil.ERROR_PARAMETER_ID_OR_AUTHOR_ID;
import static ru.aston.util.ServletUtil.createResponse;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

    private final PostService postService;
    private final ObjectMapper mapper;

    public PostServlet() {
        this.postService = new PostServiceImpl();
        this.mapper = new ObjectMapper();
    }

    public PostServlet(PostService postService, ObjectMapper mapper) {
        this.postService = postService;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String authorId = req.getParameter("author-id");
        String postId = req.getParameter("id");

        if (authorId != null && postId == null) {
            long id = Long.parseLong(authorId);
            List<PostDto> allPosts = postService.findByAuthorId(id);

            String json = mapper.writeValueAsString(allPosts);
            createResponse(resp, json, SC_OK);

        } else if (authorId == null && postId != null) {
            long id = Long.parseLong(postId);
            PostDto postById = postService.findById(id);

            String json = mapper.writeValueAsString(postById);
            createResponse(resp, json, SC_OK);

        } else {
            createResponse(resp, ERROR_PARAMETER_ID_OR_AUTHOR_ID, SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PostDto convertedPost = mapper.readValue(req.getReader(), PostDto.class);
        PostDto postDto = postService.create(convertedPost);

        String json = mapper.writeValueAsString(postDto);
        createResponse(resp, json, SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id == null) {
            createResponse(resp, ERROR_PARAMETER_ID, SC_BAD_REQUEST);
        }

        long postId = Long.parseLong(id);
        if (postService.deleteById(postId)) {
            createResponse(resp, "Post successfully deleted.", SC_OK);
        } else {
            createResponse(resp, "Post for delete not found.", SC_BAD_REQUEST);
        }
    }

}
