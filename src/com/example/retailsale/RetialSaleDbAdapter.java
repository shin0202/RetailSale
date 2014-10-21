package com.example.retailsale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RetialSaleDbAdapter
{
	private static final String TAG = "RetialSaleDbAdapter";
	private static final String DB_NAME = "retailsale.sqlite";
	private static final String DB_PATH = "/data/data/com.example.retailsale/databases/";
	private static final String DATABASE_NAME = "retailsale.sqlite";
	private static final int DATABASE_VERSION = 1;
	private static final String ADD_CUSTOMER_TABLE = "add_customer";
	private static final String DATA_OPTION_TABLE = "data_option";
	private static final String CREATE_TABLE_STRING = "create table ";
	public static final String ORDER_DESC = " desc";
	/************************************************************************** for add customer */
	public static final String KEY_ADD_CUSTOMER_ID = "_id";
	public static final String KEY_ADD_CUSTOMER_NAME = "customer_name";
	public static final String KEY_ADD_PHONE_NUMBER = "phone_number";
	public static final String KEY_ADD_CELL_PHONE_NUMBER = "cellphone_number";
	public static final String KEY_ADD_COMPANY_PHONE = "company_phone";
	public static final String KEY_ADD_EMAIL = "email";
	public static final String KEY_ADD_SEX = "sex";
	public static final String KEY_ADD_BIRTHDAY = "birthday";
	public static final String KEY_ADD_MSG_FROM = "msg_from";
	public static final String KEY_ADD_TITLE = "title";
	public static final String KEY_ADD_JOB = "job";
	public static final String KEY_ADD_INTRODUCER = "introducer";
	public static final String KEY_ADD_AGE = "age";
	public static final String KEY_ADD_VISIT_DATE_TIME = "visit_date_time";
	public static final String KEY_ADD_CREATE_DATE = "create_date";
	/************************************************************************** for add customer */
	/************************************************************************** for order measure */
	public static final String KEY_ADD_SEND_MSG = "send_msg";
	public static final String KEY_WORK_ALIAS = "work_alias";
	public static final String KEY_STATUS_COMMENT = "status_comment";
	public static final String KEY_STATUS = "status";
	public static final String KEY_WORK = "work";
	public static final String KEY_CONTACT = "contact";
	public static final String KEY_COMMENT = "comment";
	public static final String KEY_SPACE = "space";
	public static final String KEY_BUDGET = "budget";
	public static final String KEY_WORK_DATE_TIME = "work_date_time";
	public static final String KEY_IS_UPLOAD = "is_upload"; // the field to check upload or not
	/************************************************************************** for order measure */
	/************************************************************************** for data option */
	public static final String KEY_DATA_OPTION_ID = "_id";
	public static final String KEY_DATA_OPTION_TYPE = "option_type";
	public static final String KEY_DATA_OPTION_ALIAS = "option_alias";
	public static final String KEY_DATA_OPTION_SERIAL = "option_serial";
	public static final String KEY_DATA_OPTION_NAME = "option_name";
	
	public static final int OPTION_USER_TYPE_IDNEX = 0;
	public static final int OPTION_CUSTOMER_SEX_IDNEX = 1;
	public static final int OPTION_CUSTOMER_TITLE_IDNEX = 2;
	public static final int OPTION_CUSTOMER_INFO_IDNEX = 3;
	public static final int OPTION_CUSTOMER_JOB_IDNEX = 4;
	public static final int OPTION_CUSTOMER_AGE_IDNEX = 5;
	public static final int OPTION_SERVICE_TYPE_IDNEX = 6;
	public static final int OPTION_USER_GROUP_IDNEX = 7;
	public static final int OPTION_RESERVATION_STATUS_IDNEX = 8;
	public static final int OPTION_RESERVATION_BUDGET_IDNEX = 9;
	public static final int OPTION_RESERVATION_SPACE_IDNEX = 10;
	/************************************************************************** for data option */
	public static final int NOTUPLOAD = 0;
	public static final int UPLOAD = 1;
	private static final String ADD_CUSTOMER_CREATE = CREATE_TABLE_STRING + ADD_CUSTOMER_TABLE
			+ "(" + KEY_ADD_CUSTOMER_ID + " INTEGER PRIMARY KEY," + KEY_ADD_CUSTOMER_NAME
			+ " TEXT," + KEY_ADD_PHONE_NUMBER + " TEXT," + KEY_ADD_CELL_PHONE_NUMBER + " TEXT,"
			+ KEY_ADD_COMPANY_PHONE + " TEXT," + KEY_ADD_EMAIL + " TEXT," + KEY_ADD_SEX + " INTEGER,"
			+ KEY_ADD_BIRTHDAY + " TEXT," + KEY_ADD_MSG_FROM + " INTEGER," + KEY_ADD_TITLE
			+ " INTEGER," + KEY_ADD_JOB + " INTEGER," + KEY_ADD_INTRODUCER + " TEXT," + KEY_ADD_AGE
			+ " INTEGER," + KEY_ADD_VISIT_DATE_TIME + " TEXT," + KEY_ADD_CREATE_DATE + " TEXT,"
			+ KEY_ADD_SEND_MSG + " INTEGER," + KEY_WORK_ALIAS + " TEXT,"
			+ KEY_STATUS_COMMENT + " TEXT," + KEY_STATUS + " INTEGER,"
			+ KEY_WORK + " TEXT," + KEY_CONTACT + " TEXT,"
			+ KEY_COMMENT + " TEXT," + KEY_SPACE + " INTEGER," + KEY_BUDGET
			+ " INTEGER," + KEY_WORK_DATE_TIME + " TEXT," + KEY_IS_UPLOAD +  " INTEGER" + ");";
	private static final String DATA_OPTION_CREATE = CREATE_TABLE_STRING + DATA_OPTION_TABLE + "("
			+ KEY_DATA_OPTION_ID + " INTEGER PRIMARY KEY," + KEY_DATA_OPTION_TYPE + " INTEGER,"
			+ KEY_DATA_OPTION_ALIAS + " TEXT," + KEY_DATA_OPTION_SERIAL + " INTEGER,"
			+ KEY_DATA_OPTION_NAME + " TEXT" + ");";
	private Context context = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		Context context = null;

		public DatabaseHelper(Context context) throws IOException
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
//			createDatabase();
		}

		/**
		 * Creates empty database on system and rewrites it with existing
		 * database
		 * 
		 * @throws IOException
		 */
		public void createDatabase() throws IOException
		{
			boolean dbExist = checkDatabase();
			if (dbExist)
			{
				// do nothing, the database already exists;
				Log.e(TAG, "createDatabase(): dbExist");
			}
			else
			{
				Log.e(TAG, "createDatabase(): db is not Exist");
				this.getReadableDatabase();
				try
				{
					copyDatabase();
				}
				catch (IOException e)
				{
					Log.e(TAG, "createDatabase(): Error copying database");
					throw new Error("Error copying database");
				}
			}
		}

		/**
		 * Check to see if a database exists
		 * 
		 * @return true if database exists, false otherwise;
		 */
		private boolean checkDatabase()
		{
			File dbFile = new File(DB_PATH + DB_NAME);
			return dbFile.exists();
		}

		/**
		 * Copes database from assets-folder to system folder, where it can be
		 * accessed and handled. This is done by transferring byte-stream.
		 * 
		 * @throws IOException
		 */
		private void copyDatabase() throws IOException
		{
			// Open your local db as the input stream
			AssetManager assets = this.context.getAssets();
			InputStream myInput = assets.open(DB_NAME);
			String outFileName = DB_PATH + DB_NAME;
			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0)
				myOutput.write(buffer, 0, length);
			// close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

		public SQLiteDatabase openDataBase() throws SQLException
		{
			// Open the database
			String myPath = DB_PATH + DB_NAME;
			return SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(ADD_CUSTOMER_CREATE);
			db.execSQL(DATA_OPTION_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + ADD_CUSTOMER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATA_OPTION_TABLE);
			onCreate(db);
		}
	}

	/** Constructor */
	public RetialSaleDbAdapter(Context context)
	{
		this.context = context;
	}

	public RetialSaleDbAdapter open() throws SQLException
	{
		try
		{
			dbHelper = new DatabaseHelper(context);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		if (dbHelper != null) dbHelper.close();
	}
	
	public boolean isDbOpen() {
	    return db.isOpen();
	}

	/** Get all customer from customer table */
	public Cursor getAllCustomer()
	{
		return db.query(ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
				KEY_ADD_CUSTOMER_NAME, KEY_ADD_PHONE_NUMBER, KEY_ADD_CELL_PHONE_NUMBER,
				KEY_ADD_COMPANY_PHONE, KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY,
				KEY_ADD_MSG_FROM, KEY_ADD_TITLE, KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE,
				KEY_ADD_VISIT_DATE_TIME, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG, KEY_WORK_ALIAS,
				KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK,
				KEY_CONTACT, KEY_COMMENT, KEY_SPACE, KEY_BUDGET,
				KEY_WORK_DATE_TIME, KEY_IS_UPLOAD }, null, null, null, null, KEY_ADD_CREATE_DATE + ORDER_DESC);
	}

	/** Get all option from option table */
	public Cursor getAllOption()
	{
		return db.query(DATA_OPTION_TABLE, new String[] { KEY_DATA_OPTION_ID,
				KEY_DATA_OPTION_TYPE, KEY_DATA_OPTION_ALIAS, KEY_DATA_OPTION_SERIAL,
				KEY_DATA_OPTION_NAME}, null, null, null, null, null);
	}

	/** Insert customer to customer table */
	public long create(String customerName, String phoneNumber, String cellphoneNumer,
			String companyPhone, String email, int sex, String birthday, int msgFrom,
			int title, int job, String introducer, int age, String visitDateTime,
			String createDate, int sendMsg, String workAlias, String statusComment,
			int status, String work, String contact, String comment,
			int space, int budget, String workDateTime, int isUpload)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_ADD_CUSTOMER_NAME, customerName);
		args.put(KEY_ADD_PHONE_NUMBER, phoneNumber);
		args.put(KEY_ADD_CELL_PHONE_NUMBER, cellphoneNumer);
		args.put(KEY_ADD_COMPANY_PHONE, companyPhone);
		args.put(KEY_ADD_EMAIL, email);
		args.put(KEY_ADD_SEX, sex);
		args.put(KEY_ADD_BIRTHDAY, birthday);
		args.put(KEY_ADD_MSG_FROM, msgFrom);
		args.put(KEY_ADD_TITLE, title);
		args.put(KEY_ADD_JOB, job);
		args.put(KEY_ADD_INTRODUCER, introducer);
		args.put(KEY_ADD_AGE, age);
		args.put(KEY_ADD_VISIT_DATE_TIME, visitDateTime);
		args.put(KEY_ADD_CREATE_DATE, createDate);
		args.put(KEY_ADD_SEND_MSG, sendMsg);
		
		args.put(KEY_WORK_ALIAS, workAlias);
		args.put(KEY_STATUS_COMMENT, statusComment);
		args.put(KEY_STATUS, status);
		args.put(KEY_WORK, work);
		args.put(KEY_CONTACT, contact);
		args.put(KEY_COMMENT, comment);
		args.put(KEY_SPACE, space);
		args.put(KEY_BUDGET, budget);
		args.put(KEY_WORK_DATE_TIME, workDateTime);
		args.put(KEY_IS_UPLOAD, isUpload);
		return db.insert(ADD_CUSTOMER_TABLE, null, args);
	}

	/** Insert option to option table */
	public long create(int optionType, String optionAlias, int optionSerial,
			String optionName)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_DATA_OPTION_TYPE, optionType);
		args.put(KEY_DATA_OPTION_ALIAS, optionAlias);
		args.put(KEY_DATA_OPTION_SERIAL, optionSerial);
		args.put(KEY_DATA_OPTION_NAME, optionName);
		return db.insert(DATA_OPTION_TABLE, null, args);
	}

	/** Delete one customer from customer table */
	public boolean deleteCustomer(String rowId)
	{
		if (db != null && db.isOpen())
			return db.delete(ADD_CUSTOMER_TABLE, KEY_ADD_CUSTOMER_ID + "=" + rowId, null) > 0;
		else return false;
	}
	
	/** Delete one option from option table */
	public boolean deleteOption(String rowId)
	{
		if (db != null && db.isOpen())
			return db.delete(DATA_OPTION_TABLE, KEY_DATA_OPTION_ID + "=" + rowId, null) > 0;
		else return false;
	}

	/** Delete all customer from customer table */
	public boolean deleteAllCustomer()
	{
		if (db != null && db.isOpen())
			return db.delete(ADD_CUSTOMER_TABLE, null, null) > 0;
		else return false;
	}
	
	/** Delete all option from option table */
	public boolean deleteAllOption()
	{
		if (db != null && db.isOpen())
			return db.delete(DATA_OPTION_TABLE, null, null) > 0;
		else return false;
	}

	/** Query single customer */
	public Cursor getCustomer(String rowId) throws SQLException
	{
		if (db.isOpen())
		{
			Cursor cursor = db.query(true, ADD_CUSTOMER_TABLE, new String[] {
					KEY_ADD_CUSTOMER_ID, KEY_ADD_CUSTOMER_NAME, KEY_ADD_PHONE_NUMBER,
					KEY_ADD_CELL_PHONE_NUMBER, KEY_ADD_COMPANY_PHONE, KEY_ADD_EMAIL, KEY_ADD_SEX,
					KEY_ADD_BIRTHDAY, KEY_ADD_MSG_FROM, KEY_ADD_TITLE, KEY_ADD_JOB,
					KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_VISIT_DATE_TIME, KEY_ADD_CREATE_DATE,
					KEY_ADD_SEND_MSG, KEY_WORK_ALIAS, KEY_STATUS_COMMENT,
					KEY_STATUS, KEY_WORK, KEY_CONTACT,
					KEY_COMMENT, KEY_SPACE, KEY_BUDGET, KEY_WORK_DATE_TIME, KEY_IS_UPLOAD },
					KEY_ADD_CUSTOMER_ID + "=" + rowId, null, null, null, null, null);
			if (cursor != null) { return cursor; }
			return null;
		}
		else
		{
			return null;
		}
	}
	
	/** Query single option */
	public Cursor getOption(String rowId) throws SQLException
	{
		if (db.isOpen())
		{
			Cursor cursor = db.query(true, DATA_OPTION_TABLE, new String[] { KEY_DATA_OPTION_ID,
					KEY_DATA_OPTION_TYPE, KEY_DATA_OPTION_ALIAS, KEY_DATA_OPTION_SERIAL,
					KEY_DATA_OPTION_NAME }, KEY_DATA_OPTION_ID + "=" + rowId, null, null, null,
					null, null);
			if (cursor != null) { return cursor; }
			return null;
		}
		else
		{
			return null;
		}
	}

	/** Update the customer table */
	public boolean updateCustomer(String rowID, String customerName, String phoneNumber,
			String cellphoneNumer, String companyPhone, String email, int sex, String birthday,
			int msgFrom, int title, int job, String introducer, int age,
			String visitDateTime, String createDate, int sendMsg, String workAlias,
			String statusComment, int status, String work,
			String contact, String comment, int space, int budget, String workDateTime, int isUpload)
	{
		if (db.isOpen())
		{
			ContentValues args = new ContentValues();
			args.put(KEY_ADD_CUSTOMER_NAME, customerName);
			args.put(KEY_ADD_PHONE_NUMBER, phoneNumber);
			args.put(KEY_ADD_CELL_PHONE_NUMBER, cellphoneNumer);
			args.put(KEY_ADD_COMPANY_PHONE, companyPhone);
			args.put(KEY_ADD_EMAIL, email);
			args.put(KEY_ADD_SEX, sex);
			args.put(KEY_ADD_BIRTHDAY, birthday);
			args.put(KEY_ADD_MSG_FROM, msgFrom);
			args.put(KEY_ADD_TITLE, title);
			args.put(KEY_ADD_JOB, job);
			args.put(KEY_ADD_INTRODUCER, introducer);
			args.put(KEY_ADD_AGE, age);
			args.put(KEY_ADD_VISIT_DATE_TIME, visitDateTime);
			args.put(KEY_ADD_CREATE_DATE, createDate);
			args.put(KEY_ADD_SEND_MSG, sendMsg);
			args.put(KEY_WORK_ALIAS, workAlias);
			args.put(KEY_STATUS_COMMENT, statusComment);
			args.put(KEY_STATUS, status);
			args.put(KEY_WORK, work);
			args.put(KEY_CONTACT, contact);
			args.put(KEY_COMMENT, comment);
			args.put(KEY_SPACE, space);
			args.put(KEY_BUDGET, budget);
			args.put(KEY_WORK_DATE_TIME, workDateTime);
			args.put(KEY_IS_UPLOAD, isUpload);
			return db.update(ADD_CUSTOMER_TABLE, args, KEY_ADD_CUSTOMER_ID + "=" + rowID,
					null) > 0;
		}
		else
		{
			return false;
		}
	}
	
	/** Update the option table */
	public boolean updateOption(int rowID, String optionType, String optionAlias, int optionSerial,
			String optionName)
	{
		if (db.isOpen())
		{
			ContentValues args = new ContentValues();
			args.put(KEY_DATA_OPTION_TYPE, optionType);
			args.put(KEY_DATA_OPTION_ALIAS, optionAlias);
			args.put(KEY_DATA_OPTION_SERIAL, optionSerial);
			args.put(KEY_DATA_OPTION_NAME, optionName);
			return db.update(DATA_OPTION_TABLE, args, KEY_DATA_OPTION_ID + "=" + rowID, null) > 0;
		}
		else
		{
			return false;
		}
	}

	/** Query single customer with msgFrom and createDate */
	// For test
	public Cursor getTitleandDate(String msgFrom, String createDate) throws SQLException
	{
		if (db.isOpen())
		{
			Cursor cursor = db.rawQuery("SELECT * FROM mails WHERE " + KEY_ADD_MSG_FROM + " = "
					+ "\"" + msgFrom + "\"" + " AND " + KEY_ADD_CREATE_DATE + " = " + "\""
					+ createDate + "\"", null);
			if (cursor != null)
			{
				cursor.moveToFirst();
			}
			return cursor;
		}
		else
		{
			return null;
		}
	}
}
