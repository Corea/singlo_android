package com.garagestory.singlo.data;

public class Professional {

	private int id;
	private int server_id;
	private String name;
	private String certification;
	private int price;
	private String profile;
	private String photo;
	private String url;
	private int like;
	private int active;
	private int status;
	private String status_message;
	private String company;

	private int evaluation_count;
	private double evaluation_score;

	public Professional() {
	}

	public Professional(int id, int server_id, String name,
			String certification, int price, String profile, String photo,
			String url, int like, int active, int status,
			String status_message, int evaluation_count,
			double evaluation_score, String company) {
		this.id = id;
		this.server_id = server_id;
		this.name = name;
		this.certification = certification;
		this.price = price;
		this.profile = profile;
		this.photo = photo;
		this.url = url;
		this.like = like;
		this.active = active;
		this.status = status;
		this.status_message = status_message;
		this.evaluation_count = evaluation_count;
		this.evaluation_score = evaluation_score;
		this.company = company;
	}

	public Professional(int server_id, String name, String certification,
			int price, String profile, String photo, String url, int like,
			int active, int status, String status_message,
			int evaluation_count, double evaluation_score, String company) {
		this.server_id = server_id;
		this.name = name;
		this.certification = certification;
		this.price = price;
		this.profile = profile;
		this.photo = photo;
		this.url = url;
		this.like = like;
		this.active = active;
		this.status = status;
		this.status_message = status_message;
		this.evaluation_count = evaluation_count;
		this.evaluation_score = evaluation_score;
		this.company = company;
	}

	public int getID() {
		return id;
	}

	public int getServerId() {
		return server_id;
	}

	public String getName() {
		return name;
	}

	public String getCertification() {
		return certification;
	}

	public int getPrice() {
		return price;
	}

	public String getProfile() {
		return profile;
	}

	public String getPhoto() {
		return photo;
	}

	public String getUrl() {
		return url;
	}

	public int getLike() {
		return like;
	}

	public int getActive() {
		return active;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return status_message;
	}

	public int getEvaluationCount() {
		return evaluation_count;
	}

	public double getEvaluationScore() {
		return evaluation_score;
	}

	public String getCompany() {
		return company;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setServerId(int server_id) {
		this.server_id = server_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatusMessage(String status_message) {
		this.status_message = status_message;
	}

	public void setEvaluationCount(int evaluation_count) {
		this.evaluation_count = evaluation_count;
	}

	public void setEvaluationScore(double evaluation_score) {
		this.evaluation_score = evaluation_score;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
