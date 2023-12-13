package ru.aston.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServletUtil {

    public static final String ERROR_PARAMETER_ID_AND_AUTHOR_ID = "Path should contain parameter id={numbers} and author-id={numbers}.";
    public static final String ERROR_PARAMETER_ID_OR_AUTHOR_ID = "Path should contain parameter id={numbers} or author-id={numbers}.";
    public static final String ERROR_PARAMETER_ID = "Path should contain parameter id={numbers}.";
    public static final String CONTENT_TYPE = "application/json";
    public static final String UTF_8 = "UTF-8";

    private ServletUtil() {
    }

    public static void createResponse(HttpServletResponse resp, String body, int status) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(UTF_8);
        resp.setStatus(status);
        resp.getWriter().print(body);
    }

}
