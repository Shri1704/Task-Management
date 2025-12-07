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
import com.example.demo_task_management.dto.TaskDto;
import com.example.demo_task_management.exception.ResourceNotFoundException;
import com.example.demo_task_management.model.Employee;
import com.example.demo_task_management.model.Task;
import com.example.demo_task_management.model.mapper.TaskMapper;
import com.example.demo_task_management.repository.TaskRepository;
import com.example.demo_task_management.utils.CommonUtils;
import com.example.demo_task_management.utils.Contants;
import com.example.demo_task_management.exception.ProcessingException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;
    private final TaskMapper taskMapper;
    public TaskServiceImpl(TaskRepository taskRepository,EmployeeService employeeService, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.employeeService = employeeService;
        this.taskMapper = taskMapper;
    }
    @Override
    public void addTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setStatus(Contants.TASK_STATUS.PENDING);
        task.setCreatedTimeStamp(System.currentTimeMillis());
        task.setUpdatedTimeStamp(System.currentTimeMillis());
        taskRepository.save(task);
    }

    @Override
    public List<TaskDto> getAllTasks(String status, String employeeId) {
        if(StringUtils.isNotBlank(status) && !CommonUtils.isValidEnum(Contants.TASK_STATUS.class,status)){
            throw new ProcessingException("Invalid Status use PENDING/IN_PROGRESS/COMPLETED");
        }
        List<Task> taskList= new ArrayList<>();
        if(StringUtils.isNotBlank(employeeId) && StringUtils.isBlank(status)){
            taskList= taskRepository.findAllTasksForEmployee(UUID.fromString(employeeId));
        } else if (StringUtils.isBlank(employeeId) && StringUtils.isNotBlank(status)) {
            taskList=taskRepository.findAllTasksWithStatus(Contants.TASK_STATUS.valueOf(status));
        }else if (StringUtils.isNotBlank(employeeId) && StringUtils.isNotBlank(status)) {
            taskList= taskRepository.findTasksForEmployeeWithOptionalStatus(UUID.fromString(employeeId),Contants.TASK_STATUS.valueOf(status));
        }
        else {
            taskList= taskRepository.findAll();
        }
        List<TaskDto> taskDtoList= new ArrayList<>();
        taskList.forEach(task->{
            taskDtoList.add(taskMapper.toDto(task));
        });
        return taskDtoList;
    }
    private Task getTaskById(UUID taskId){
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public void assignTask(String taskId, String employeeId) {
        Task task=new Task();
        if(taskId==null){
            throw new ProcessingException("Task Id is required");
        }else {
            UUID taskIdInUUID= UUID.fromString(taskId);
            task=getTaskById(taskIdInUUID);
            if(task==null){
                throw new ResourceNotFoundException("Task doesn't Exists");
            }
        }
        if (StringUtils.isNotBlank(employeeId)) {
            UUID employeeIdInUUID= UUID.fromString(employeeId);
            Employee employee=employeeService.getEmployeeById(employeeIdInUUID);
            if(employee==null){
                throw new ResourceNotFoundException("Employee doesn't Exists");
            }else {
                task.setAssignedTo(employee);
                task.setUpdatedTimeStamp(System.currentTimeMillis());
                taskRepository.save(task);
            }
        }else {
            task.setAssignedTo(null);
            task.setUpdatedTimeStamp(System.currentTimeMillis());
            taskRepository.save(task);

        }

    }

    @Override
    public void updateTaskStatus(String taskId, String status, String userId) {
        if(taskId==null){
            throw new ProcessingException("Task Id is required");
        }
        UUID taskIdInUUID= UUID.fromString(taskId);
        Task task=getTaskById(taskIdInUUID);
        if(task==null){
            throw new ResourceNotFoundException("Task doesn't Exists");
        }if(!CommonUtils.isValidEnum(Contants.TASK_STATUS.class,status)){
            throw new ProcessingException("Invalid Status");
        }if (userId==null){
            throw new ProcessingException("User Id is required");
        }else {
            Employee employee=employeeService.getEmployeeById(UUID.fromString(userId));
            if(employee==null){
                throw new ResourceNotFoundException("Employee doesn't Exists");
            }
        }
        task.setStatus(Contants.TASK_STATUS.valueOf(status));
        task.setUpdatedTimeStamp(System.currentTimeMillis());
        taskRepository.save(task);
    }

    @Override
    public void updateTask(String taskId, TaskDto taskDto, String userId) {
        if(taskId==null){
            throw new ProcessingException("Task Id is required");
        }
        UUID taskIdInUUID= UUID.fromString(taskId);
        Task task=getTaskById(taskIdInUUID);
        if(task==null){
            throw new ResourceNotFoundException("Task doesn't Exists");
        }if (userId==null){
            throw new ProcessingException("User Id is required");
        }else {
            Employee employee=employeeService.getEmployeeById(UUID.fromString(userId));
            if(employee==null){
                throw new ResourceNotFoundException("Employee doesn't Exists");
            }
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setUpdatedTimeStamp(System.currentTimeMillis());
            taskRepository.save(task);
        }
    }

    private EmployeeDto convertToDto(Employee employee){
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setDepartment(employee.getDepartment());
        return employeeDto;
    }
}
