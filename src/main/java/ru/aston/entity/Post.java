package ru.aston.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {
    private long id;
    private String text;
    private long authorId;
}
