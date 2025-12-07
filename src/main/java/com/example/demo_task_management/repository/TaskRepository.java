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
package com.example.demo_task_management.repository;

import com.example.demo_task_management.model.Task;
import com.example.demo_task_management.utils.Contants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :employeeId " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Task> findTasksForEmployeeWithOptionalStatus(
            @Param("employeeId") UUID employeeId,
            @Param("status") Contants.TASK_STATUS status);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :employeeId")
    List<Task> findAllTasksForEmployee(UUID employeeId);
    @Query("SELECT t FROM Task t WHERE t.status= :status")
    List<Task> findAllTasksWithStatus(Contants.TASK_STATUS status);

}
