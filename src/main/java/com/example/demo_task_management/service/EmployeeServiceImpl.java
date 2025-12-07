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
import com.example.demo_task_management.exception.ProcessingException;
import com.example.demo_task_management.exception.ResourceAlreadyExistsException;
import com.example.demo_task_management.exception.ResourceNotFoundException;
import com.example.demo_task_management.model.Employee;
import com.example.demo_task_management.model.mapper.EmployeeMapper;
import com.example.demo_task_management.model.mapper.TaskMapper;
import com.example.demo_task_management.repository.EmployeeRepository;
import com.example.demo_task_management.repository.TaskRepository;
import com.example.demo_task_management.utils.CommonUtils;
import com.example.demo_task_management.utils.Contants;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final EmployeeMapper employeeMapper;
    private final TaskMapper taskMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, TaskRepository taskRepository, EmployeeMapper employeeMapper, TaskMapper taskMapper) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.employeeMapper = employeeMapper;
        this.taskMapper = taskMapper;
    }
    @Override
    public void addEmployee(EmployeeDto employee) {
        if(isEmployeeExist(employee.getEmail())){
            throw new ResourceAlreadyExistsException("Employee Already Exists");
        }
        Employee employeeDao = employeeMapper.toEntity(employee);
        employeeDao.setCreatedTimeStamp(System.currentTimeMillis());
        employeeDao.setUpdatedTimeStamp(System.currentTimeMillis());
        employeeRepository.save(employeeDao);
    }

    @Override
    public List<EmployeeDto> getAllEmployees(String status) {
        List<EmployeeDto> employeeDtoList= new ArrayList<>();
        List<Employee> employeeList=new ArrayList<>();
        if(StringUtils.isNotBlank(status) && !CommonUtils.isValidEnum(Contants.TASK_STATUS.class,status)){
            throw new ProcessingException("Invalid Status use PENDING/IN_PROGRESS/COMPLETED");
        }
        if(StringUtils.isNotBlank(status) && CommonUtils.isValidEnum(Contants.TASK_STATUS.class,status)){
            employeeList=   employeeRepository.findEmployeesByTaskStatus(Contants.TASK_STATUS.valueOf(status));
        }else {
            employeeList=employeeRepository.findAll();
        }

        for(Employee employee:employeeList){
            employeeDtoList.add(employeeMapper.toDto(employee));
        }
        return employeeDtoList;
    }


    @Override
    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public EmployeeStats getEmployeeStats(UUID employeeIdInUUID) {
        Employee employee=employeeRepository.findById(employeeIdInUUID).orElse(null);
        if(employee==null){
            throw new ResourceNotFoundException("Employee doesn't Exists");
        }
        EmployeeStats employeeStats=new EmployeeStats();
        employeeStats.setName(employee.getName());
        if(employee.getTasks()!=null){
            employeeStats.setTotalTasks(employee.getTasks().size());
            AtomicInteger pendingTasks=new AtomicInteger(0);
            AtomicInteger completedTasks=new AtomicInteger(0);
            AtomicInteger inProgressTasks=new AtomicInteger(0);
            employee.getTasks().forEach(task->{
                switch (task.getStatus()){
                    case PENDING:
                        pendingTasks.getAndIncrement();
                        break;
                    case IN_PROGRESS:
                        inProgressTasks.getAndIncrement();
                        break;
                    case COMPLETED:
                        completedTasks.getAndIncrement();
                        break;
                }
            });
            employeeStats.setPendingTasks(pendingTasks.get());
            employeeStats.setCompletedTasks(completedTasks.get());
            employeeStats.setInProgressTasks(inProgressTasks.get());
            employeeStats.setTotalTasks(employee.getTasks().size());
        }
        return employeeStats;
    }

    private boolean isEmployeeExist(String email){
        Employee employee= new Employee();
        employee.setEmail(email);
        if(StringUtils.isBlank(email) || employeeRepository.existsByEmail(email)){
            return true;
        }return false;
    }
}
