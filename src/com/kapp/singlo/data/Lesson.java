package com.kapp.singlo.data;

public class Lesson {
	
	int id;

	int server_id;
	int user_id;
	Integer teacher_id;
	int lesson_type;
	String video;
	int club_type;
	String question;
	String created_datetime;
	int status;
	String user_name;
	
	
	
	public Lesson() {}
	public Lesson( int id, int server_id, int user_id, Integer teacher_id, int lesson_type, String video, int club_type, String question, String created_datetime, int status, String user_name ) {
		this.id = id;
		this.server_id = server_id;
		this.user_id = user_id;
		this.teacher_id = teacher_id;
		this.lesson_type = lesson_type;
		this.video = video;
		this.club_type = club_type;
		this.question = question;
		this.created_datetime = created_datetime;
		this.status = status;
		this.user_name = user_name;
	}
	public Lesson( int server_id, int user_id, Integer teacher_id, int lesson_type, String video, int club_type, String question, String created_datetime, int status, String user_name ) {
		this.user_id = user_id;
		this.server_id = server_id;
		this.teacher_id = teacher_id;
		this.lesson_type = lesson_type;
		this.video = video;
		this.club_type = club_type;
		this.question = question;
		this.created_datetime = created_datetime;
		this.status = status;
		this.user_name = user_name;
	}
	
	public int getID() {
		return id;
	}
	public int getServerID() {
		return server_id;
	}
	public int getUserID() {
		return user_id;
	}
	public Integer getTeacherID() {
		return teacher_id;
	}
	public int getLessonType() {
		return lesson_type;
	}
	public String getVideo() {
		return video;
	}
	public int getClubType() {
		return club_type;
	}
	public String getQuestion() {
		return question;
	}
	public String getCreatedDatetime() {
		return created_datetime;
	}
	public int getStatus() {
		return status;
	}
	public String getUserName() {
		return user_name;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public void setServerID(int server_id) {
		this.server_id = server_id;
	}
	public void setUserID(int user_id) {
		this.user_id = user_id;
	}
	public void SetTeacherID(Integer teacher_id) {
		this.teacher_id = teacher_id;
	}
	public void SetLessonType(int lesson_type) {
		this.lesson_type = lesson_type;
	}
	public void SetVideo(String video) {
		this.video = video;
	}
	public void SetClubType(int club_type) {
		this.club_type = club_type;
	}
	public void SetQuestion(String question) {
		this.question = question;
	}
	public void SetCreatedDatetime(String created_datetime) {
		this.created_datetime = created_datetime;
	}
	public void SetStatus(int status) {
		this.status = status;
	}
	public void SetUserName(String user_name) {
		this.user_name = user_name;
	}
}