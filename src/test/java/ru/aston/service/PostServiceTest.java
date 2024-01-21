package ru.aston.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.PostDto;
import ru.aston.entity.Post;
import ru.aston.mapper.PostMapper;
import ru.aston.repository.PostRepository;
import ru.aston.service.impl.PostServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.aston.TestObjectsBuilder.getPostDto;

class PostServiceTest {

    private PostRepository repository;
    private PostMapper mapper;
    private PostService service;

    @BeforeEach
    public void setUp() {
        repository = mock(PostRepository.class);
        mapper = new PostMapper();
        service = new PostServiceImpl(repository, mapper);
    }

    @Test
    void create() {
        PostDto dto = getPostDto();
        Post post = mapper.fromDto(dto);

        when(repository.create(any(Post.class))).thenReturn(post);

        assertEquals(mapper.toDto(post), service.create(dto));
    }

    @Test
    void findById() {
        PostDto dto = getPostDto();
        Post post = mapper.fromDto(dto);

        when(repository.findById(anyLong())).thenReturn(post);

        assertEquals(mapper.toDto(post), service.findById(post.getId()));
        verify(repository).findById(anyLong());
    }

    @Test
    void findByAuthorId() {
        PostDto dto = getPostDto();
        Post post = mapper.fromDto(dto);

        when(repository.findByAuthorId(anyLong())).thenReturn(List.of(post));

        assertEquals(List.of(mapper.toDto(post)), service.findByAuthorId(post.getId()));
        verify(repository).findByAuthorId(anyLong());
    }

    @Test
    void update() {
        PostDto dto = getPostDto();
        Post post = mapper.fromDto(dto);

        when(repository.update(any(Post.class))).thenReturn(post);

        assertEquals(mapper.toDto(post), service.update(dto));
    }

    @Test
    void deleteById() {
        when(repository.deleteById(anyLong())).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

}