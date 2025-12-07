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
package com.example.demo_task_management.service;

import com.example.demo_task_management.dto.EmployeeDto;
import com.example.demo_task_management.dto.EmployeeStats;
import com.example.demo_task_management.model.Employee;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    void addEmployee(EmployeeDto employee);
    List<EmployeeDto> getAllEmployees(String status);
    Employee getEmployeeById(UUID id);

    EmployeeStats getEmployeeStats(UUID employeeIdInUUID);
}
