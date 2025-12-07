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

import com.example.demo_task_management.dto.EmployeeDto;
import com.example.demo_task_management.exception.ProcessingException;
import com.example.demo_task_management.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    EmployeeService employeeService;

    @Test
    public void testAddEmployee() throws Exception {
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setName("John");
        employeeDto.setEmail("john@example.com");
        employeeDto.setDepartment("IT");
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employeeDto)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testGetAllEmployeesWithoutFilters() throws Exception {
        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllEmployeeWithFilters() throws Exception {
        mockMvc.perform(get("/employee?status=PENDING"))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetAllEmployeeWithInvalidFilterValue() throws Exception {
        when(employeeService.getAllEmployees("NOTVALID"))
                .thenThrow(new ProcessingException("Invalid status"));

        mockMvc.perform(get("/employee")
                        .param("status", "NOTVALID"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testGetEmployeeStatsForEmployeeDoesNotExists() throws Exception {
        mockMvc.perform(get("/employee/stats/12345678-1234-1234-1234-123456789012"))
                .andExpect(status().isNotFound());
    }
}
