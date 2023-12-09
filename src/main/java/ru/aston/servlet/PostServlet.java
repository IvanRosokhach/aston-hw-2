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

@WebServlet("/post")
public class PostServlet extends HttpServlet {

    PostService postService = new PostServiceImpl();
    ObjectMapper mapper = new ObjectMapper();

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
            throw new RuntimeException("An empty or two value cannot be passed.");
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
        String path = req.getQueryString();
        if (path != null) {
            String idString = path.substring(3);

            try {
                int postId = Integer.parseInt(idString);
                postService.delete(postId);
                createResponse(resp, "Post delete done.", SC_OK);

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
