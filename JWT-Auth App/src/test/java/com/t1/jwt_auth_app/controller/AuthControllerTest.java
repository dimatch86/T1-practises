package com.t1.jwt_auth_app.controller;

import com.t1.jwt_auth_app.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractTest {

    @Test
    @DisplayName("Регистрация с валидными данными -> ОК")
    @WithMockUser
    void testRegistration_WhenPostWithValidRequestBody_ThenReturnsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Ivan",
                          "email": "new@mail.ru",
                          "password": "testtest"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Регистрация с невалидными данными -> BAD REQUEST")
    @WithMockUser
    void testRegistration_WhenPostWithInvalidRequestBody_ThenReturnsBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Ivan",
                          "email": "newmail.ru",
                          "password": "testtest"
                        }
                    """))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Логин существующего пользователя с валидными данными -> ОК")
    @WithMockUser
    void testLogin_WhenPostWithValidRequestBody_ThenReturnsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.ru",
                          "password": "user"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@mail.ru"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }

    @Test
    @DisplayName("Логин существующего пользователя с невалидными данными -> BAD REQUEST")
    @WithMockUser
    void testLogin_WhenPostWithInvalidRequestBody_ThenBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "",
                          "password": "userpassword"
                        }
                    """))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Логин существующего пользователя с невалидными паролем -> UNAUTHORIZED")
    @WithMockUser
    void testLogin_WhenPostWithInvalidPassword_ThenUnauthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@mail.ru",
                          "password": "userpass"
                        }
                    """))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(status().isUnauthorized());
    }
}
