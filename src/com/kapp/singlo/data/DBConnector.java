package com.kapp.singlo.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnector extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "singlo";

	// Professional table name
	public static final String TABLE_PROFESSIONAL = "professional";
	public static final String TABLE_LESSON = "lesson";
	public static final String TABLE_LESSON_ANSWER = "lesson_answer";
	public static final String TABLE_LESSON_ANSWER_IMAGE = "lesson_answer_image";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_SERVER_ID = "server_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CERTIFICATION = "certification";
	private static final String KEY_PRICE = "price";
	private static final String KEY_PROFILE = "profile";
	private static final String KEY_PHOTO = "photo";
	private static final String KEY_URL = "url";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_TEACHER_ID = "teacher_id";	
	private static final String KEY_LESSON_TYPE = "lesson_type";
	private static final String KEY_VIDEO = "video";
	private static final String KEY_CLUB_TYPE = "club_type";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_CREATED_DATETIME = "created_datetime";
	private static final String KEY_STATUS = "status";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_LESSON_ID = "lesson_id";
	private static final String KEY_SCORE1 = "score1";
	private static final String KEY_SCORE2 = "score2";
	private static final String KEY_SCORE3 = "score3";
	private static final String KEY_SCORE4 = "score4";
	private static final String KEY_SCORE5 = "score5";
	private static final String KEY_SCORE6 = "score6";
	private static final String KEY_SCORE7 = "score7";
	private static final String KEY_SCORE8 = "score8";
	private static final String KEY_RECOMMEND1 = "recommend1";
	private static final String KEY_RECOMMEND2 = "recommend2";
	private static final String KEY_CAUSE = "cause";
	private static final String KEY_SOUND = "sound";
	private static final String KEY_ANSWER_ID = "answer_id";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_LINE = "line";
	private static final String KEY_LIKE = "like";
	private static final String KEY_ACTIVE = "active";
	private static final String KEY_STATUS_MESSAGE = "status_message";
	private static final String KEY_EVALUATION_COUNT = "evaluation_count";
	private static final String KEY_EVALUATION_SCORE = "evaluation_score";
	private static final String KEY_COMPANY = "company";
	private static final String KEY_TIMING = "timing";
	private static final String KEY_THUMNAIL = "thumnail";

	public DBConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PROFESSIONAL_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_PROFESSIONAL + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_SERVER_ID + " INTEGER," + KEY_NAME + " TEXT,"
				+ KEY_CERTIFICATION + " TEXT," + KEY_PRICE + " INTEGER,"
				+ KEY_PROFILE + " TEXT," + KEY_PHOTO + " TEXT," + KEY_URL
				+ " TEXT," + KEY_LIKE + " INTEGER," + KEY_ACTIVE + " INTEGER,"
				+ KEY_STATUS + " INTEGER," + KEY_STATUS_MESSAGE + " TEXT,"
				+ KEY_EVALUATION_COUNT + " INTEGER," + KEY_EVALUATION_SCORE
				+ " REAL," + KEY_COMPANY + " TEXT" + ")";
		db.execSQL(CREATE_PROFESSIONAL_TABLE);

		String CREATE_LESSON_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_LESSON + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_SERVER_ID + " INTEGER," + KEY_USER_ID + " INTEGER,"
				+ KEY_TEACHER_ID + " INTEGER," + KEY_LESSON_TYPE + " INTEGER,"
				+ KEY_VIDEO + " TEXT," + KEY_CLUB_TYPE + " INTEGER, "
				+ KEY_QUESTION + " TEXT," + KEY_CREATED_DATETIME + " TEXT,"
				+ KEY_STATUS + " INTEGER," + KEY_USER_NAME + " TEXT," 
				+ KEY_THUMNAIL + " TEXT" + ")";
		db.execSQL(CREATE_LESSON_TABLE);

		String CREATE_LESSON_ANSWER_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_LESSON_ANSWER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_LESSON_ID + " INTEGER," + KEY_SERVER_ID + " INTEGER,"
				+ KEY_SCORE1 + " INTEGER," + KEY_SCORE2 + " INTEGER,"
				+ KEY_SCORE3 + " INTEGER," + KEY_SCORE4 + " INTEGER,"
				+ KEY_SCORE5 + " INTEGER," + KEY_SCORE6 + " INTEGER,"
				+ KEY_SCORE7 + " INTEGER," + KEY_SCORE8 + " INTEGER,"
				+ KEY_CAUSE + " INTEGER," + KEY_RECOMMEND1 + " INTEGER,"
				+ KEY_RECOMMEND2 + " INTEGER," + KEY_SOUND + " TEXT,"
				+ KEY_CREATED_DATETIME + " TEXT" + ")";
		db.execSQL(CREATE_LESSON_ANSWER_TABLE);

		String CREATE_LESSON_ANSWER_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_LESSON_ANSWER_IMAGE + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_ANSWER_ID + " INTEGER,"
				+ KEY_SERVER_ID + " INTEGER," + KEY_IMAGE + " TEXT," + KEY_LINE
				+ " TEXT," + KEY_TIMING + " INTEGER" + ")";
		db.execSQL(CREATE_LESSON_ANSWER_IMAGE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSIONAL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON_ANSWER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON_ANSWER_IMAGE);

		// Create tables again
		onCreate(db);
	}

	public void RemoveAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		onUpgrade(db, 0, 0);
		db.close();
	}

	public void removeProfessionalAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSIONAL);
		onCreate(db);
		db.close();
	}

	public void removeLessonAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON_ANSWER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON_ANSWER_IMAGE);
		onCreate(db);
		db.close();
	}

	public void addProfessional(Professional professional) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_SERVER_ID, professional.getServerId());
		values.put(KEY_NAME, professional.getName());
		values.put(KEY_CERTIFICATION, professional.getCertification());
		values.put(KEY_PRICE, professional.getPrice());
		values.put(KEY_PROFILE, professional.getProfile());
		values.put(KEY_PHOTO, professional.getPhoto());
		values.put(KEY_URL, professional.getUrl());
		values.put(KEY_LIKE, professional.getLike());
		values.put(KEY_ACTIVE, professional.getActive());
		values.put(KEY_STATUS, professional.getStatus());
		values.put(KEY_STATUS_MESSAGE, professional.getStatusMessage());
		values.put(KEY_EVALUATION_COUNT, professional.getEvaluationCount());
		values.put(KEY_EVALUATION_SCORE, professional.getEvaluationScore());
		values.put(KEY_COMPANY, professional.getCompany());

		// Inserting Row
		db.insert(TABLE_PROFESSIONAL, null, values);
		db.close(); // Closing database connection
	}

	public Professional getProfessional(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PROFESSIONAL, new String[] { KEY_ID,
				KEY_SERVER_ID, KEY_NAME, KEY_CERTIFICATION, KEY_PRICE,
				KEY_PROFILE, KEY_PHOTO, KEY_URL, KEY_LIKE, KEY_ACTIVE,
				KEY_STATUS, KEY_STATUS_MESSAGE, KEY_EVALUATION_COUNT,
				KEY_EVALUATION_SCORE, KEY_COMPANY }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			Professional professional = new Professional(cursor.getInt(0),
					cursor.getInt(1), cursor.getString(2), cursor.getString(3),
					cursor.getInt(4), cursor.getString(5), cursor.getString(6),
					cursor.getString(7), cursor.getInt(8), cursor.getInt(9),
					cursor.getInt(10), cursor.getString(11), cursor.getInt(12),
					cursor.getDouble(13), cursor.getString(14));
			db.close();
			return professional;
		}
		db.close();
		return null;
	}

	public Professional getProfessionalByServerID(int server_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PROFESSIONAL, new String[] { KEY_ID,
				KEY_SERVER_ID, KEY_NAME, KEY_CERTIFICATION, KEY_PRICE,
				KEY_PROFILE, KEY_PHOTO, KEY_URL, KEY_LIKE, KEY_ACTIVE,
				KEY_STATUS, KEY_STATUS_MESSAGE, KEY_EVALUATION_COUNT,
				KEY_EVALUATION_SCORE, KEY_COMPANY }, KEY_SERVER_ID + "=?",
				new String[] { String.valueOf(server_id) }, null, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();

			Professional professional = new Professional(cursor.getInt(0),
					cursor.getInt(1), cursor.getString(2), cursor.getString(3),
					cursor.getInt(4), cursor.getString(5), cursor.getString(6),
					cursor.getString(7), cursor.getInt(8), cursor.getInt(9),
					cursor.getInt(10), cursor.getString(11), cursor.getInt(12),
					cursor.getDouble(13), cursor.getString(14));

			db.close();
			return professional;
		}

		db.close();

		return null;
	}

	public List<Professional> getAllProfessional() {
		List<Professional> professionalList = new ArrayList<Professional>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PROFESSIONAL
				+ " ORDER BY " + KEY_NAME + " ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Professional professional = new Professional(cursor.getInt(0),
						cursor.getInt(1), cursor.getString(2),
						cursor.getString(3), cursor.getInt(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getInt(8),
						cursor.getInt(9), cursor.getInt(10),
						cursor.getString(11), cursor.getInt(12),
						cursor.getDouble(13), cursor.getString(14));

				professionalList.add(professional);
			} while (cursor.moveToNext());
		}

		db.close();
		return professionalList;
	}

	public int updateProfessional(Professional professional) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_NAME, professional.getName());
		values.put(KEY_SERVER_ID, professional.getServerId());
		values.put(KEY_CERTIFICATION, professional.getCertification());
		values.put(KEY_PRICE, professional.getPrice());
		values.put(KEY_PROFILE, professional.getProfile());
		values.put(KEY_PHOTO, professional.getPhoto());
		values.put(KEY_URL, professional.getUrl());
		values.put(KEY_LIKE, professional.getLike());
		values.put(KEY_ACTIVE, professional.getActive());
		values.put(KEY_STATUS, professional.getStatus());
		values.put(KEY_STATUS_MESSAGE, professional.getStatusMessage());
		values.put(KEY_EVALUATION_COUNT, professional.getEvaluationCount());
		values.put(KEY_EVALUATION_SCORE, professional.getEvaluationScore());
		values.put(KEY_COMPANY, professional.getCompany());

		// updating row
		int result = db.update(TABLE_PROFESSIONAL, values, KEY_ID + " = ?",
				new String[] { String.valueOf(professional.getID()) });

		db.close();
		return result;
	}

	/*
	 * // Contact 정보 삭제하기 public void deleteContact(Professional contact) {
	 * SQLiteDatabase db = this.getWritableDatabase(); db.delete(TABLE_CONTACTS,
	 * KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	 * db.close(); }
	 */

	public int getProfessionalCount() {
		String countQuery = "SELECT * FROM " + TABLE_PROFESSIONAL
				+ " ORDER BY " + KEY_SERVER_ID + " DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();

		// return count
		return cursor.getCount();
	}

	public void addLesson(Lesson lesson) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_SERVER_ID, lesson.getServerID());
		values.put(KEY_USER_ID, lesson.getUserID());
		values.put(KEY_TEACHER_ID, lesson.getTeacherID());
		values.put(KEY_LESSON_TYPE, lesson.getLessonType());		
		values.put(KEY_VIDEO, lesson.getVideo());
		values.put(KEY_CLUB_TYPE, lesson.getClubType());
		values.put(KEY_QUESTION, lesson.getQuestion());
		values.put(KEY_CREATED_DATETIME, lesson.getCreatedDatetime());
		values.put(KEY_STATUS, lesson.getStatus());
		values.put(KEY_USER_NAME, lesson.getUserName());
		values.put(KEY_THUMNAIL, lesson.getThumnail());

		// Inserting Row
		db.insert(TABLE_LESSON, null, values);
		db.close();
	}

	public int updateLesson(Lesson lesson) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_SERVER_ID, lesson.getServerID());
		values.put(KEY_USER_ID, lesson.getUserID());
		values.put(KEY_TEACHER_ID, lesson.getTeacherID());
		values.put(KEY_LESSON_TYPE, lesson.getLessonType());
		values.put(KEY_VIDEO, lesson.getVideo());
		values.put(KEY_CLUB_TYPE, lesson.getClubType());
		values.put(KEY_QUESTION, lesson.getQuestion());
		values.put(KEY_CREATED_DATETIME, lesson.getCreatedDatetime());
		values.put(KEY_STATUS, lesson.getStatus());
		values.put(KEY_USER_NAME, lesson.getUserName());
		values.put(KEY_THUMNAIL, lesson.getThumnail());
		
		// updating row
		int result = db.update(TABLE_LESSON, values, KEY_ID + " = ?",
				new String[] { String.valueOf(lesson.getID()) });

		db.close();
		return result;
	}

	public Lesson getLesson(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_LESSON, new String[] { KEY_ID,
				KEY_SERVER_ID, KEY_USER_ID, KEY_TEACHER_ID, KEY_LESSON_TYPE,
				KEY_VIDEO, KEY_CLUB_TYPE, KEY_QUESTION, KEY_CREATED_DATETIME,
				KEY_STATUS, KEY_USER_NAME, KEY_THUMNAIL }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			Lesson lesson = new Lesson(cursor.getInt(0), cursor.getInt(1),
					cursor.getInt(2), (Integer) cursor.getInt(3),
					cursor.getInt(4), cursor.getString(5), cursor.getInt(6),
					cursor.getString(7), cursor.getString(8), cursor.getInt(9),
					cursor.getString(10), cursor.getString(11));
			db.close();
			return lesson;
		}
		db.close();
		return null;
	}

	public ArrayList<Lesson> getAllLesson() {
		ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_LESSON + " ORDER BY "
				+ KEY_SERVER_ID + " DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Lesson lesson = new Lesson(cursor.getInt(0), cursor.getInt(1),
						cursor.getInt(2), (Integer) cursor.getInt(3),
						cursor.getInt(4), cursor.getString(5),
						cursor.getInt(6), cursor.getString(7),
						cursor.getString(8), cursor.getInt(9),
						cursor.getString(10), cursor.getString(11));
				lessonList.add(lesson);
			} while (cursor.moveToNext());
		}

		db.close();
		return lessonList;
	}

	public void addLessonAnswer(LessonAnswer lessonAnswer) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_LESSON_ID, lessonAnswer.getLessonID());
		values.put(KEY_SERVER_ID, lessonAnswer.getServerID());
		values.put(KEY_SCORE1, lessonAnswer.getScore1());
		values.put(KEY_SCORE2, lessonAnswer.getScore2());
		values.put(KEY_SCORE3, lessonAnswer.getScore3());
		values.put(KEY_SCORE4, lessonAnswer.getScore4());
		values.put(KEY_SCORE5, lessonAnswer.getScore5());
		values.put(KEY_SCORE6, lessonAnswer.getScore6());
		values.put(KEY_SCORE7, lessonAnswer.getScore7());
		values.put(KEY_SCORE8, lessonAnswer.getScore8());
		values.put(KEY_CAUSE, lessonAnswer.getCause());
		values.put(KEY_RECOMMEND1, lessonAnswer.getRecommend1());
		values.put(KEY_RECOMMEND2, lessonAnswer.getRecommend2());
		values.put(KEY_SOUND, lessonAnswer.getSound());
		values.put(KEY_CREATED_DATETIME, lessonAnswer.getCreatedDatetime());

		// Inserting Row
		db.insert(TABLE_LESSON_ANSWER, null, values);
		db.close();
	}

	public LessonAnswer getLessonAnswerByLesson(Lesson lesson) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_LESSON_ANSWER, new String[] { KEY_ID,
				KEY_LESSON_ID, KEY_SERVER_ID, KEY_SCORE1, KEY_SCORE2,
				KEY_SCORE3, KEY_SCORE4, KEY_SCORE5, KEY_SCORE6, KEY_SCORE7,
				KEY_SCORE8, KEY_CAUSE, KEY_RECOMMEND1, KEY_RECOMMEND2,
				KEY_SOUND, KEY_CREATED_DATETIME }, KEY_LESSON_ID + "=?",
				new String[] { String.valueOf(lesson.getServerID()) }, null,
				null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			LessonAnswer lessonAnswer = new LessonAnswer(cursor.getInt(0),
					cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
					cursor.getInt(4), cursor.getInt(5), cursor.getInt(6),
					cursor.getInt(7), cursor.getInt(8), cursor.getInt(9),
					cursor.getInt(10), cursor.getInt(11), cursor.getInt(12),
					cursor.getInt(13), cursor.getString(14),
					cursor.getString(15));
			db.close();
			return lessonAnswer;
		}
		db.close();
		return null;
	}

	public void addLessonAnswerImage(LessonAnswerImage lessonAnswerImage) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_ANSWER_ID, lessonAnswerImage.getAnswerID());
		values.put(KEY_SERVER_ID, lessonAnswerImage.getServerID());
		values.put(KEY_IMAGE, lessonAnswerImage.getImage());
		values.put(KEY_LINE, lessonAnswerImage.getLine());
		values.put(KEY_TIMING, lessonAnswerImage.getTiming());

		// Inserting Row
		db.insert(TABLE_LESSON_ANSWER_IMAGE, null, values);
		db.close();
	}

	public List<LessonAnswerImage> getAllLessonAnswerImageByLessonAnswer(
			LessonAnswer lessonAnswer) {
		List<LessonAnswerImage> lessonAnswerImageList = new ArrayList<LessonAnswerImage>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LESSON_ANSWER_IMAGE, new String[] {
				KEY_ID, KEY_ANSWER_ID, KEY_SERVER_ID, KEY_IMAGE, KEY_LINE,
				KEY_TIMING }, KEY_ANSWER_ID + "=?",
				new String[] { String.valueOf(lessonAnswer.getServerID()) },
				null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				LessonAnswerImage lessonAnswerImage = new LessonAnswerImage(
						cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getLong(5));
				lessonAnswerImageList.add(lessonAnswerImage);
			} while (cursor.moveToNext());
		}

		db.close();
		return lessonAnswerImageList;
	}

	public Boolean checkExistQuery(String query) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		Boolean result = false;

		if (cursor.getCount() != 0) {
			result = true;
		}

		db.close();
		return result;
	}

}
