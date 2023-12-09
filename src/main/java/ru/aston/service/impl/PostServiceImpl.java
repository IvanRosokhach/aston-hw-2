package ru.aston.service.impl;

import ru.aston.dto.PostDto;
import ru.aston.entity.Post;
import ru.aston.mapper.PostMapper;
import ru.aston.repository.PostRepository;
import ru.aston.repository.impl.PostRepositoryImpl;
import ru.aston.service.PostService;

import java.util.List;

public class PostServiceImpl implements PostService {

    PostRepository postRepository = new PostRepositoryImpl();

    @Override
    public PostDto create(PostDto postDto) {
        Post post = postRepository.create(PostMapper.toPost(postDto));
        return PostMapper.toPostDto(post);
    }

    @Override
    public PostDto findById(long postId) {
        Post post = postRepository.findById(postId);
        return PostMapper.toPostDto(post);
    }

    @Override
    public List<PostDto> findByAuthorId(long authorId) {
        List<Post> posts = postRepository.findByAuthorId(authorId);
        return posts.stream().map(PostMapper::toPostDto).toList();
    }

    @Override
    public void delete(long postId) {
        postRepository.deleteById(postId);
    }

}
