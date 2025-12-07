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
package com.example.demo_task_management.dto;

import com.example.demo_task_management.utils.Contants;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {
    private UUID id;
    private String title;
    private String description;
    private Contants.TASK_STATUS status;
    private String assignedEmployee;
    private long createdTimeStamp;
    private long updatedTimeStamp;
}
