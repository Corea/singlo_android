package com.garagestory.singlo.data;

public class LessonAnswer {

	int id;
	int lesson_id;
	int server_id;
	int score1;
	int score2;
	int score3;
	int score4;
	int score5;
	int score6;
	int score7;
	int score8;
	int cause;
	int recommend1;
	int recommend2;
	String sound;
	String created_datetime;

	public LessonAnswer() {
	}

	public LessonAnswer(int lesson_id, int server_id, int score1, int score2,
			int score3, int score4, int score5, int score6, int score7,
			int score8, int cause, int recommend1, int recommend2,
			String sound, String created_datetime) {
		this.lesson_id = lesson_id;
		this.server_id = server_id;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
		this.score4 = score4;
		this.score5 = score5;
		this.score6 = score6;
		this.score7 = score7;
		this.score8 = score8;
		this.cause = cause;
		this.recommend1 = recommend1;
		this.recommend2 = recommend2;
		this.sound = sound;
		this.created_datetime = created_datetime;
	}

	public LessonAnswer(int id, int lesson_id, int server_id, int score1,
			int score2, int score3, int score4, int score5, int score6,
			int score7, int score8, int cause, int recommend1, int recommend2,
			String sound, String created_datetime) {
		this.id = id;
		this.lesson_id = lesson_id;
		this.server_id = server_id;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
		this.score4 = score4;
		this.score5 = score5;
		this.score6 = score6;
		this.score7 = score7;
		this.score8 = score8;
		this.cause = cause;
		this.recommend1 = recommend1;
		this.recommend2 = recommend2;
		this.sound = sound;
		this.created_datetime = created_datetime;
	}

	public int getID() {
		return id;
	}

	public int getLessonID() {
		return lesson_id;
	}

	public int getServerID() {
		return server_id;
	}

	public int getScore1() {
		return score1;
	}

	public int getScore2() {
		return score2;
	}

	public int getScore3() {
		return score3;
	}

	public int getScore4() {
		return score4;
	}

	public int getScore5() {
		return score5;
	}

	public int getScore6() {
		return score6;
	}

	public int getScore7() {
		return score7;
	}

	public int getScore8() {
		return score8;
	}

	public int getCause() {
		return cause;
	}

	public int getRecommend1() {
		return recommend1;
	}

	public int getRecommend2() {
		return recommend2;
	}

	public String getSound() {
		return sound;
	}

	public String getCreatedDatetime() {
		return created_datetime;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setLessonID(int lesson_id) {
		this.lesson_id = lesson_id;
	}

	public void setServerID(int server_id) {
		this.server_id = server_id;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public void setScore3(int score3) {
		this.score3 = score3;
	}

	public void setScore4(int score4) {
		this.score4 = score4;
	}

	public void setScore5(int score5) {
		this.score5 = score5;
	}

	public void setScore6(int score6) {
		this.score6 = score6;
	}

	public void setScore7(int score7) {
		this.score7 = score7;
	}

	public void setScore8(int score8) {
		this.score8 = score8;
	}

	public void setCause(int cause) {
		this.cause = cause;
	}

	public void setRecommend1(int recommend1) {
		this.recommend1 = recommend1;
	}

	public void setRecommend2(int recommend2) {
		this.recommend2 = recommend2;
	}

	public void SetSound(String sound) {
		this.sound = sound;
	}

	public void SetCreatedDatetime(String created_datetime) {
		this.created_datetime = created_datetime;
	}
}
