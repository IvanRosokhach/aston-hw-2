package ru.aston.mapper;

import ru.aston.dto.PostDto;
import ru.aston.entity.Post;

public class PostMapper {

    public Post fromDto(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .text(postDto.getText())
                .authorId(postDto.getAuthorId())
                .build();
    }

    public PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .authorId(post.getAuthorId())
                .build();
    }

}
