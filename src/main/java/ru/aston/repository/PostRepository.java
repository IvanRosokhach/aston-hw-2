package ru.aston.repository;

import ru.aston.entity.Post;

import java.util.List;

public interface PostRepository extends BaseRepository<Post> {

    List<Post> findByAuthorId(long authorId);

}
