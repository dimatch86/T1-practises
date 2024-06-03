package com.t1.jwt_auth_app.controller;

import com.t1.jwt_auth_app.AbstractTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractTest {

    @Test
    @DisplayName("Запрос информации о текущем пользователе авторизованным пользователем -> ОК")
    @WithUserDetails(value = "user@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCurrentUserInfo_whenUserAuthorized_thenReturnsOkWithData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@mail.ru"));
    }

    @Test
    @DisplayName("Запрос информации о текущем пользователе неавторизованным пользователем -> UNAUTHORIZED")
    void testCurrentUserInfo_whenUserUnauthorized_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/info"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Запрос списка всех пользователей пользователем с ролью ADMIN -> ОК")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testListUsers_whenUserWithRoleAdmin_thenReturnsOkWithData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Запрос списка всех пользователей пользователем с ролью USER -> FORBIDDEN")
    @WithUserDetails(value = "user@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testListUsers_whenUserWithRoleUser_thenReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Добавление роли авторизованным пользователем с ролью ADMIN -> ОК")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addNewRoleTest_whenUserAuthenticatedAndUserIsAdmin_thenReturnsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add-role/1")
                        .param("type", "ROLE_ADMIN")
                        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Добавление роли авторизованным пользователем с ролью USER -> FORBIDDEN")
    @WithUserDetails(value = "user@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addNewRoleTest_whenUserAuthenticatedAndUserIsUser_thenReturnsForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add-role/1")
                        .param("type", "ROLE_ADMIN")
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Попытка добавления роли с пустым параметром -> BAD_REQUEST")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void addNewRoleTest_whenParameterIsMissed_thenReturnsBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add-role/1")
                        .param("type", "")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Удаление роли авторизованным пользователем с ролью ADMIN -> ОК")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void removeRoleTest_whenUserAuthenticatedAndUserIsAdmin_thenReturnsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/remove-role/1")
                        .param("type", "ROLE_ADMIN")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Удаление единственной роли -> NOT_MODIFIED")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void removeRoleTest_whenOneRoleRemained_thenReturnsNotModified() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/remove-role/2")
                        .param("type", "ROLE_ADMIN")
                )
                .andDo(print())
                .andExpect(status().isNotModified());
    }

    @Test
    @DisplayName("Обновление авторизованным пользователем с валидными данными -> ОК")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUpdateUser_whenUserIsAdminWithValidRequestBody_thenReturnsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Ivan",
                          "email": "new@mail.ru",
                          "password": "new_password"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление авторизованным пользователем с невалидными данными -> BAD_REQUEST")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUpdateUser_whenUserIsAdminWithInvalidRequestBody_thenReturnsBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Ivan",
                          "email": "newmail.ru",
                          "password": "new_password"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Некорректный ввод email"));
    }

    @Test
    @DisplayName("Обновление авторизованным пользователем с ролью USER -> FORBIDDEN")
    @WithUserDetails(value = "user@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUpdateUser_whenUserIsUserWithValidRequestBody_thenReturnsForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Ivan",
                          "email": "new@mail.ru",
                          "password": "new_password"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление пользователем с ролью ADMIN -> NO_CONTENT")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void removeUserTest_whenAdminDeletes_thenReturnsNoContent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/2"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Удаление пользователем с ролью USER -> FORBIDDEN")
    @WithUserDetails(value = "user@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void removeUserTest_whenUserDeletes_thenReturnsForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление самого себя -> NOT_MODIFIED")
    @WithUserDetails(value = "admin@mail.ru", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void removeUserTest_whenAdminDeletesHimself_thenReturnsNotModified() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/1"))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(jsonPath("$.error")
                        .value("Вы не можете удалить себя"));
    }
}
