package ru.aston.matveenko_ym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aston.matveenko_ym.dto.UserDto;
import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Yury Matveenko");
        user.setEmail("korn1984@narod.ru");
        user.setAge(40);
        user.setCreatedAt(LocalDateTime.now());

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Yury Matveenko"))
                .andExpect(jsonPath("$[0].email").value("korn1984@narod.ru"))
                .andExpect(jsonPath("$[0].age").value(40))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto user = new UserDto();
        user.setName("Bogdan Matveenko");
        user.setEmail("bogdi2012@narod.ru");
        user.setAge(12);
        user.setCreatedAt(LocalDateTime.now());

        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bogdan Matveenko"))
                .andExpect(jsonPath("$.email").value("bogdi2012@narod.ru"))
                .andExpect(jsonPath("$.age").value(12))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testGetUserById() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Bogdan Matveenko");
        user.setEmail("bogdi2012@narod.ru");
        user.setAge(12);
        user.setCreatedAt(LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bogdan Matveenko"))
                .andExpect(jsonPath("$.email").value("bogdi2012@narod.ru"))
                .andExpect(jsonPath("$.age").value(12))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Bogdan Matveenko");
        user.setEmail("bogdi2012@narod.ru");
        user.setAge(12);
        user.setCreatedAt(LocalDateTime.now());

        UserDto updatedUser = new UserDto();
        updatedUser.setId(1L);
        updatedUser.setName("Yury Matveenko");
        updatedUser.setEmail("korn1984@narod.ru");
        updatedUser.setAge(40);
        updatedUser.setCreatedAt(LocalDateTime.now());

        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Yury Matveenko"))
                .andExpect(jsonPath("$.email").value("korn1984@narod.ru"))
                .andExpect(jsonPath("$.age").value(40))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/user/delete/1"))
                .andExpect(status().isNoContent());
    }
}