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

import com.example.demo_task_management.utils.Contants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void testEmployeeExist(){
        assertThat(employeeRepository.existsByEmail("test@example.com")).isFalse();
    }

    @Test
    public void testFindEmployeesByTaskStatus(){
        assertThat(employeeRepository.findEmployeesByTaskStatus(Contants.TASK_STATUS.PENDING))
                .isNotNull();
    }
}
