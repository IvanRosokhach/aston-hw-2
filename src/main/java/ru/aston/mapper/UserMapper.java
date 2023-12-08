package ru.aston.mapper;

import ru.aston.dto.UserDto;
import ru.aston.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .login(userDto.getLogin())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .build();
    }

}
