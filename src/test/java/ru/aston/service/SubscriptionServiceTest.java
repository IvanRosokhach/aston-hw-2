package ru.aston.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.SubscriptionRepository;
import ru.aston.service.impl.SubscriptionServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionServiceTest {

    private SubscriptionRepository repository;
    private UserMapper mapper;
    private SubscriptionService service;

    @BeforeEach
    public void setUp() {
        repository = mock(SubscriptionRepository.class);
        mapper = new UserMapper();
        service = new SubscriptionServiceImpl(repository, mapper);
    }

    @Test
    void getSubscribers() {
        UserDto dto = getUserDto();
        User user = mapper.fromDto(dto);

        when(repository.getSubscribers(anyLong())).thenReturn(List.of(user));

        assertEquals(List.of(mapper.toDto(user)), service.getSubscribers(anyLong()));
    }

    @Test
    void add() {
        when(repository.add(anyLong(), anyLong())).thenReturn(true);

        assertTrue(service.add(1, 2));
    }

    @Test
    void remove() {
        when(repository.remove(anyLong(), anyLong())).thenReturn(true);

        assertTrue(service.remove(1, 2));
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("TestName")
                .login("TestLogin")
                .build();
    }

}