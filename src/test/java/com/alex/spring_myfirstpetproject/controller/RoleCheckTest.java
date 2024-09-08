package com.alex.spring_myfirstpetproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoleCheckTest {

    @Test
    void testCheckAccessWithValidRole() {
        Authentication authentication = mock(Authentication.class);


        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = List.of(authority);

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        assertTrue(RoleCheck.checkAccess(authentication, "ROLE_USER"));
    }

    @Test
    void testCheckAccessWithInvalidRole() {
        // Создаем мок-объект Authentication
        Authentication authentication = mock(Authentication.class);

        // Создаем мок-объект GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        Collection<GrantedAuthority> authorities = List.of(authority);

        // Настраиваем поведение мока
        when(authentication.getAuthorities()).thenReturn((Collection)authorities);

        // Проверяем, что метод возвращает false для неправильной роли
        assertFalse(RoleCheck.checkAccess(authentication, "ROLE_USER"));
    }

    @Test
    void testCheckAccessWithEmptyAuthorities() {
        // Создаем мок-объект Authentication
        Authentication authentication = mock(Authentication.class);

        // Настраиваем пустую коллекцию авторитетов
        when(authentication.getAuthorities()).thenReturn(List.of());

        // Проверяем, что метод возвращает false, если нет авторитетов
        assertFalse(RoleCheck.checkAccess(authentication, "ROLE_USER"));
    }
}