package ru.aston;

import ru.aston.dto.PostDto;
import ru.aston.dto.UserDto;
import ru.aston.entity.Post;
import ru.aston.entity.User;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class TestObjectsBuilder {

    public static User getUser() {
        return User.builder()
                .name("TestName")
                .login("TestLogin")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("TestName")
                .login("TestLogin")
                .build();
    }

    public static Post getPost() {
        return Post.builder()
                .text("TestText")
                .authorId(1)
                .build();
    }

    public static PostDto getPostDto() {
        return PostDto.builder()
                .id(1)
                .text("TestText")
                .authorId(1)
                .build();
    }

    public static BufferedReader getReader(String s) {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes())));
    }

}
