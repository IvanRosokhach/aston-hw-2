package ru.aston.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    long id;
    String name;
    String login;

}
