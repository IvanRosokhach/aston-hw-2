package ru.aston.service;

import ru.aston.dto.PostDto;

import java.util.List;

public interface PostService {

    PostDto create(PostDto postDto);

    PostDto findById(long postId);

    List<PostDto> findByAuthorId(long authorId);

    void delete(long postId);

}
