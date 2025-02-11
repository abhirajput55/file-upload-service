package com.uploadservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "employee_review")
public class EmployeeReview {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_review_id")
    private Long empReviewId;
	
	@Column(name = "employee_id")
	private Long employeeId;
	
	@Column(name = "review_date")
	private String reviewDate;
	
	@Column(name = "goal")
	private String goal;
	
	@Column(name = "achievement")
	private String achievement;
	
	@Column(name = "rating")
	private int rating;
	
	@Column(name = "feedback")
	private String feedback;
	
	
	public EmployeeReview() {
		super();
	}

	public EmployeeReview(Long employeeId, String reviewDate, String goal, String achievement,
			int rating, String feedback) {
		super();
		this.employeeId = employeeId;
		this.reviewDate = reviewDate;
		this.goal = goal;
		this.achievement = achievement;
		this.rating = rating;
		this.feedback = feedback;
	}

	public Long getEmpReviewId() {
		return empReviewId;
	}

	public void setEmpReviewId(Long empReviewId) {
		this.empReviewId = empReviewId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getAchievement() {
		return achievement;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	
}
