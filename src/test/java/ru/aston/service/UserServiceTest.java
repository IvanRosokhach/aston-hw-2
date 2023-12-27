package ru.aston.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.UserRepository;
import ru.aston.service.impl.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.aston.TestObjectsBuilder.getUserDto;

class UserServiceTest {

    private UserRepository repository;
    private UserMapper mapper;
    private UserService service;

    @BeforeEach
    public void setUp() {
        repository = mock(UserRepository.class);
        mapper = new UserMapper();
        service = new UserServiceImpl(repository, mapper);
    }

    @Test
    void create() {
        UserDto dto = getUserDto();
        User user = mapper.fromDto(dto);

        when(repository.create(any(User.class))).thenReturn(user);

        assertEquals(mapper.toDto(user), service.create(dto));
    }

    @Test
    void findById() {
        UserDto dto = getUserDto();
        User user = mapper.fromDto(dto);

        when(repository.findById(anyLong())).thenReturn(user);

        assertEquals(mapper.toDto(user), service.findById(user.getId()));
        verify(repository).findById(anyLong());
    }

    @Test
    void findAll() {
        UserDto dto = getUserDto();
        User user = mapper.fromDto(dto);

        when(repository.findAll()).thenReturn(List.of(user));

        assertEquals(List.of(mapper.toDto(user)), service.findAll());
        verify(repository).findAll();
    }

    @Test
    void update() {
        UserDto dto = getUserDto();
        User user = mapper.fromDto(dto);

        when(repository.update(any(User.class))).thenReturn(user);

        assertEquals(mapper.toDto(user), service.update(dto));
    }

    @Test
    void deleteById() {
        when(repository.deleteById(anyLong())).thenReturn(true);

        assertTrue(service.deleteById(1));
    }

}