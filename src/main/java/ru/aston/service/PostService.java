package ru.aston.service;

import ru.aston.dto.PostDto;

import java.util.List;

public interface PostService extends BaseService<PostDto> {

    List<PostDto> findByAuthorId(long authorId);

}
