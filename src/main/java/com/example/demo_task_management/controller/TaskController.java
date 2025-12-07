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
import com.example.demo_task_management.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("")
    public ResponseEntity<String> addTask(@RequestBody TaskDto taskDto){
        taskService.addTask(taskDto);
        return new ResponseEntity<>("TASK Added Successfully", HttpStatus.CREATED);
    }
    @GetMapping("")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam(required = false) String status, @RequestParam(required = false) String employeeId){
       return new ResponseEntity<>(taskService.getAllTasks(status,employeeId),HttpStatus.OK);
    }
    @PutMapping(value = {
            "/{taskId}/employee",
            "/{taskId}/employee/{employeeId}"})
    public ResponseEntity<String> assignTask(@PathVariable String taskId , @PathVariable(required = false) String employeeId ){
        taskService.assignTask(taskId,employeeId);
        return new ResponseEntity<>("Task assigned successfully",HttpStatus.OK);
    }
    @PutMapping("/{taskId}/status/{status}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable String taskId, @PathVariable String status, @RequestParam String userId){
        taskService.updateTaskStatus(taskId,status,userId);
        return new ResponseEntity<>("Task Status Updated Successfully",HttpStatus.OK);
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@RequestBody TaskDto taskDto, @PathVariable String taskId, @RequestParam String userId){
        taskService.updateTask(taskId,taskDto,userId);
        return new ResponseEntity<>("Task Updated Successfully",HttpStatus.OK);
    }
}
