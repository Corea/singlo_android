package com.kapp.singlo.data;

public class Professional {

	int id;
	int server_id;
	String name;
	String certification;
	String lesson;
	int price;
	String profile;
	String photo;
	String url;
	int like;
	
	public Professional() { }
	
	public Professional(int id, int server_id, String name, String certification, String lesson, int price, String profile, String photo, String url, int like) {
		this.id = id;
		this.server_id = server_id;
		this.name = name;
		this.certification = certification;
		this.lesson = lesson;
		this.price = price;
		this.profile = profile;
		this.photo = photo;
		this.url = url;
		this.like = like;
	}
	public Professional(int server_id, String name, String certification, String lesson, int price, String profile, String photo, String url, int like) {
		this.server_id = server_id;
		this.name = name;
		this.certification = certification;
		this.lesson = lesson;
		this.price = price;
		this.profile = profile;
		this.photo = photo;
		this.url = url;
		this.like = like;
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
	public String getLesson() {
		return lesson;
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
	public void setLesson(String lesson) {
		this.lesson = lesson;
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
}
