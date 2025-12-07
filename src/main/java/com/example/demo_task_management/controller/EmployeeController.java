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
import com.example.demo_task_management.dto.EmployeeStats;
import com.example.demo_task_management.exception.ProcessingException;
import com.example.demo_task_management.model.Employee;
import com.example.demo_task_management.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping("")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeDto employee) {
        employeeService.addEmployee(employee);
        return new ResponseEntity<>("Employee Added Successfully", HttpStatus.CREATED);
    }
    @GetMapping("")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees( @RequestParam(required = false) String status){
        return new ResponseEntity<>(employeeService.getAllEmployees(status),HttpStatus.OK);
    }
    @GetMapping("/{employeeId}/stats")
    public ResponseEntity<EmployeeStats> getEmployeeStats(@PathVariable String employeeId){
        UUID employeeIdInUUID= UUID.fromString(employeeId);
        return  new ResponseEntity<>(employeeService.getEmployeeStats(employeeIdInUUID),HttpStatus.OK);
    }
}
