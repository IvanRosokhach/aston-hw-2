package ru.aston.mapper;

import ru.aston.dto.PostDto;
import ru.aston.entity.Post;

public class PostMapper {

    private PostMapper() {
    }

    public static Post toPost(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .text(postDto.getText())
                .authorId(postDto.getAuthorId())
                .build();
    }

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .authorId(post.getAuthorId())
                .build();
    }

}
