/*
 * Copyright 2025
 * ParaBlu Systems Private Limited
 * All Rights Reserved
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of ParaBlu Systems Private Limited and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary
 * to ParaBlu Systems Private Limited and its suppliers and may be covered by
 * Indian, US and Foreign Patents, patents in process, and are protected by
 * trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from ParaBlu Systems Private Limited.
 *
 */
package com.example.demo_task_management.controller;

import com.example.demo_task_management.dto.TaskDto;
import com.example.demo_task_management.exception.ProcessingException;
import com.example.demo_task_management.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    TaskService taskService;

    @Test
    public void testAddTask() throws Exception {
        TaskDto taskDto=new TaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Task Description");
        taskService.addTask(taskDto);
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testGetTask() throws Exception {
        mockMvc.perform(get("/task")).andExpect(status().isOk());
    }
    @Test
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/task/12345678-1234-1234-1234-123456789012")).andExpect(status().is4xxClientError());
    }
    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/task").param("status","PENDING")).andExpect(status().isOk());
    }
    @Test
    public void testGetAllTasksWithInvalidStatus() throws Exception {
        when(taskService.getAllTasks("NOTVALID",null))
                .thenThrow(new ProcessingException("Invalid status"));
        mockMvc.perform(get("/task").param("status","NOTVALID")).andExpect(status().isBadRequest());
    }
}
