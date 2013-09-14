package com.garagestory.singlo.util;

public class Const {

	public static final String scheme = "http";
	public static final String host = "garagestory.cafe24.com";
	public static final int port = 5000;
	
	public static final String url_prefix = scheme + "://" + host + ":" + port;
	
	public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.kapp.singlo";
	public static final String VERSION_URL = url_prefix + "/auth/version_android";
	
	public static final String REGISTER_URL = url_prefix + "/auth/register";
	public static final String LOGIN_URL = url_prefix + "/auth/login";
	public static final String LOGOUT_URL = url_prefix + "/auth/logout";
	public static final String CHANGE_PROFILE_URL = url_prefix + "/auth/profile";
	
	public static final String LESSON_ASK_FAST_URL = url_prefix + "/lesson/ask_fast";
	public static final String LESSON_ASK_SLOW_URL = url_prefix + "/lesson/ask_slow";
	public static final String LESSON_GET_LIST_URL = url_prefix + "/lesson/get_list";
	public static final String LESSON_GET_LIST_USER_URL = url_prefix + "/lesson/get_list_user";
	public static final String LESSON_ANSWER_URL = url_prefix + "/lesson/answer";
	public static final String LESSON_ANSWER_GET_URL = url_prefix + "/lesson/get_answer";
	public static final String LESSON_CAPTURE_GET_URL = url_prefix + "/lesson/get_video_capture";
	public static final String LESSON_EVALUATION_URL = url_prefix + "/lesson/evaluation";	
	
	public static final String TEACHER_GET_LIST_URL = url_prefix + "/teacher/get_list";
	public static final String TEACHER_LIKE_URL = url_prefix + "/teacher/like";
	public static final String TEACHER_LESSON_STATUS_URL = url_prefix + "/teacher/lesson_status";
	public static final String TEACHER_UPDATE_ABSENCE_URL = url_prefix + "/teacher/update_absence";
	
	public static final String VIDEO_URL = url_prefix + "/video/";
	public static final String CAPTURE_URL = url_prefix + "/capture/";
	public static final String PROFILE_URL = url_prefix + "/profile/";

	public static final String PROFILE_NONE_URL = "http://garagestory.cafe24.com/img/none.jpg" ;
	
	public static final String GET_USER_PROFILE = url_prefix + "/auth" + "/get_user_profile";
	public static final String GET_LESSON_REVIEW_EVALUATION = url_prefix + "/lesson"+ "/get_evaluation";
		
	
	public static final String lineEnd = "\r\n";
	public static final String twoHyphens = "--";
	public static final String boundary = "*****";
	
	public static final int STROKE_LINE_FACTOR = 150;
	public static final int RADIUS_CIRCLE_FACTOR = 20;
}
