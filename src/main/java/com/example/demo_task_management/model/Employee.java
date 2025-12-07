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
package com.example.demo_task_management.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "char(36)")
    private UUID id;
    private String name;
    @Column(nullable = false)
    private String email;
    private String department;
    @Column(nullable = false, updatable = false)
    private long createdTimeStamp;
    @Column(nullable = false)
    private long updatedTimeStamp;
    @OneToMany(
            mappedBy = "assignedTo",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<Task> tasks = new ArrayList<>();
}
