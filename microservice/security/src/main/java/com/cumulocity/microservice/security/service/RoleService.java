package com.cumulocity.microservice.security.service;

import lombok.SneakyThrows;

import java.util.List;

public interface RoleService {
    @SneakyThrows
    List<String> getUserRoles();
}
