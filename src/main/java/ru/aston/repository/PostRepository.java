package ru.aston.repository;

import ru.aston.entity.Post;

import java.util.List;

public interface PostRepository {

    Post create(Post post);

    Post findById(long postId);

    List<Post> findByAuthorId(long authorId);

    void deleteById(long postId);

}
