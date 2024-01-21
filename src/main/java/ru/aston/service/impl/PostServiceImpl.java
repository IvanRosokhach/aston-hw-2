package ru.aston.service.impl;

import lombok.AllArgsConstructor;
import ru.aston.dto.PostDto;
import ru.aston.entity.Post;
import ru.aston.mapper.PostMapper;
import ru.aston.repository.PostRepository;
import ru.aston.repository.impl.PostRepositoryImpl;
import ru.aston.service.PostService;

import java.util.List;

@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;

    public PostServiceImpl() {
        this.postRepository = new PostRepositoryImpl();
        this.mapper = new PostMapper();
    }

    @Override
    public PostDto create(PostDto postDto) {
        Post post = postRepository.create(mapper.fromDto(postDto));
        return mapper.toDto(post);
    }

    @Override
    public PostDto findById(long postId) {
        Post post = postRepository.findById(postId);
        return mapper.toDto(post);
    }

    @Override
    public List<PostDto> findByAuthorId(long authorId) {
        List<Post> posts = postRepository.findByAuthorId(authorId);
        return posts.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PostDto update(PostDto postDto) {
        Post post = postRepository.update(mapper.fromDto(postDto));
        return mapper.toDto(post);
    }

    @Override
    public boolean deleteById(long postId) {
        return postRepository.deleteById(postId);
    }

}
