package ru.aston.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {
    long id;
    String text;
    long authorId;
}
