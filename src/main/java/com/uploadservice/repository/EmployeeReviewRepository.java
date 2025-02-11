package com.uploadservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uploadservice.entity.EmployeeReview;

public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, Long> {

	boolean existsByEmployeeIdAndReviewDate(Long employeeId, String reviewDate);
}
