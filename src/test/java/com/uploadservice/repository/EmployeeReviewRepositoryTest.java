package com.uploadservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.uploadservice.entity.EmployeeReview;

@DataJpaTest  
@ActiveProfiles("test")
class EmployeeReviewRepositoryTest {

    @Autowired
    private EmployeeReviewRepository employeeReviewRepository;

    private EmployeeReview employeeReview;

    @BeforeEach
    void setUp() {
       
        employeeReview = new EmployeeReview();
        employeeReview.setEmployeeId(101L);
        employeeReview.setReviewDate("2024-02-10");
        employeeReview.setGoal("Improve team collaboration");
        employeeReview.setAchievement("Led successful project");
        employeeReview.setRating(5);
        employeeReview.setFeedback("Excellent performance!");

        employeeReviewRepository.save(employeeReview);
    }

    @Test
    void testExistsByEmployeeIdAndReviewDate_Found() {
        // Check if the method returns true for an existing record
        boolean exists = employeeReviewRepository.existsByEmployeeIdAndReviewDate(101L, "2024-02-10");
        assertTrue(exists, "EmployeeReview record should exist.");
    }

    @Test
    void testExistsByEmployeeIdAndReviewDate_NotFound() {
        // Check if the method returns false for a non-existing record
        boolean exists = employeeReviewRepository.existsByEmployeeIdAndReviewDate(102L, "2024-02-11");
        assertFalse(exists, "EmployeeReview record should not exist.");
    }
}
