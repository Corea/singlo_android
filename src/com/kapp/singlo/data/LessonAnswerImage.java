package com.kapp.singlo.data;

public class LessonAnswerImage {
	int id;
	int answer_id;
	int server_id;
	String image;
	String line;

	public LessonAnswerImage() {
	}

	public LessonAnswerImage(int answer_id, int server_id, String image,
			String line) {
		this.answer_id = answer_id;
		this.server_id = server_id;
		this.image = image;
		this.line = line;
	}

	public LessonAnswerImage(int id, int answer_id, int server_id,
			String image, String line) {
		this.id = id;
		this.answer_id = answer_id;
		this.server_id = server_id;
		this.image = image;
		this.line = line;
	}

	public int getID() {
		return id;
	}

	public int getAnswerID() {
		return answer_id;
	}

	public int getServerID() {
		return server_id;
	}

	public String getImage() {
		return image;
	}

	public String getLine() {
		return line;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setAnswerID(int answer_id) {
		this.answer_id = answer_id;
	}

	public void setServerID(int server_id) {
		this.server_id = server_id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
