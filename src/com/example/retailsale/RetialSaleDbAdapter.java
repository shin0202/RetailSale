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
	public static final String KEY_ADD_MALE = "male";
	public static final String KEY_ADD_BIRTHDAY = "birthday";
	public static final String KEY_ADD_MSG_FROM = "msg_from";
	public static final String KEY_ADD_TITLE = "title";
	public static final String KEY_ADD_JOB = "job";
	public static final String KEY_ADD_INTRODUCER = "introducer";
	public static final String KEY_ADD_AGE = "age";
	public static final String KEY_ADD_COME_DATE_TIME = "come_date_time";
	public static final String KEY_ADD_CREATE_DATE = "create_date";
	/************************************************************************** for add customer */
	/************************************************************************** for order measure */
	public static final String KEY_ADD_SEND_MSG = "send_msg";
	public static final String KEY_ADD_CASE_NAME = "case_name";
	public static final String KEY_ADD_CANT_DESCRIPTION = "cant_description";
	public static final String KEY_ADD_SALE_PROGRESS = "sale_progress";
	public static final String KEY_ADD_CUSTOMER_ADDRESS = "customer_address";
	public static final String KEY_ADD_CONTACT_ADDRESS = "contact_address";
	public static final String KEY_ADD_COMMENT = "comment";
	public static final String KEY_ADD_REQUEST = "request";
	public static final String KEY_ADD_COST = "cost";
	public static final String KEY_ADD_WORK_DATE_TIME = "work_date_time";
	public static final String KEY_ADD_IS_UPLOAD = "is_upload"; // the field to check upload or not
	/************************************************************************** for order measure */
	/************************************************************************** for data option */
	public static final String KEY_DATA_OPTION_ID = "_id";
	public static final String KEY_DATA_OPTION_TYPE = "option_type";
	public static final String KEY_DATA_OPTION_ALIAS = "option_alias";
	public static final String KEY_DATA_OPTION_SERIAL = "option_serial";
	public static final String KEY_DATA_OPTION_NAME = "option_name";
	/************************************************************************** for data option */
	public static final int NOTNEW = 0;
	public static final int NEW = 1;
	private static final String ADD_CUSTOMER_CREATE = CREATE_TABLE_STRING + ADD_CUSTOMER_TABLE
			+ "(" + KEY_ADD_CUSTOMER_ID + " INTEGER PRIMARY KEY," + KEY_ADD_CUSTOMER_NAME
			+ " TEXT," + KEY_ADD_PHONE_NUMBER + " TEXT," + KEY_ADD_CELL_PHONE_NUMBER + " TEXT,"
			+ KEY_ADD_COMPANY_PHONE + " TEXT," + KEY_ADD_EMAIL + " TEXT," + KEY_ADD_MALE + " TEXT,"
			+ KEY_ADD_BIRTHDAY + " TEXT," + KEY_ADD_MSG_FROM + " TEXT," + KEY_ADD_TITLE
			+ " INTEGER," + KEY_ADD_JOB + " INTEGER," + KEY_ADD_INTRODUCER + " TEXT," + KEY_ADD_AGE
			+ " INTEGER," + KEY_ADD_COME_DATE_TIME + " TEXT," + KEY_ADD_CREATE_DATE + " TEXT,"
			+ KEY_ADD_SEND_MSG + " INTEGER," + KEY_ADD_CASE_NAME + " TEXT,"
			+ KEY_ADD_CANT_DESCRIPTION + " TEXT," + KEY_ADD_SALE_PROGRESS + " INTEGER,"
			+ KEY_ADD_CUSTOMER_ADDRESS + " TEXT," + KEY_ADD_CONTACT_ADDRESS + " TEXT,"
			+ KEY_ADD_COMMENT + " TEXT," + KEY_ADD_REQUEST + " INTEGER," + KEY_ADD_COST
			+ " INTEGER," + KEY_ADD_WORK_DATE_TIME + " TEXT," + KEY_ADD_IS_UPLOAD +  " INTEGER" + ");";
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
			createDatabase();
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
		db = dbHelper.openDataBase();
		return this;
	}

	public void close()
	{
		if (dbHelper != null) dbHelper.close();
	}

	/** Get all customer from customer table */
	public Cursor getAllCustomer()
	{
		return db.query(ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
				KEY_ADD_CUSTOMER_NAME, KEY_ADD_PHONE_NUMBER, KEY_ADD_CELL_PHONE_NUMBER,
				KEY_ADD_COMPANY_PHONE, KEY_ADD_EMAIL, KEY_ADD_MALE, KEY_ADD_BIRTHDAY,
				KEY_ADD_MSG_FROM, KEY_ADD_TITLE, KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE,
				KEY_ADD_COME_DATE_TIME, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG, KEY_ADD_CASE_NAME,
				KEY_ADD_CANT_DESCRIPTION, KEY_ADD_SALE_PROGRESS, KEY_ADD_CUSTOMER_ADDRESS,
				KEY_ADD_CONTACT_ADDRESS, KEY_ADD_COMMENT, KEY_ADD_REQUEST, KEY_ADD_COST,
				KEY_ADD_WORK_DATE_TIME, KEY_ADD_IS_UPLOAD }, null, null, null, null, KEY_ADD_CREATE_DATE + ORDER_DESC);
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
			String companyPhone, String email, String male, String birthday, String msgFrom,
			String title, String job, String introducer, String age, String comeDateTime,
			String createDate, int sendMsg, String caseName, String cantDescription,
			String saleProgress, String customerAddress, String contactAddress, String comment,
			String request, String cost, String workDateTime, int isUpload)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_ADD_CUSTOMER_NAME, customerName);
		args.put(KEY_ADD_PHONE_NUMBER, phoneNumber);
		args.put(KEY_ADD_CELL_PHONE_NUMBER, cellphoneNumer);
		args.put(KEY_ADD_COMPANY_PHONE, companyPhone);
		args.put(KEY_ADD_EMAIL, email);
		args.put(KEY_ADD_MALE, male);
		args.put(KEY_ADD_BIRTHDAY, birthday);
		args.put(KEY_ADD_MSG_FROM, msgFrom);
		args.put(KEY_ADD_TITLE, title);
		args.put(KEY_ADD_JOB, job);
		args.put(KEY_ADD_INTRODUCER, introducer);
		args.put(KEY_ADD_AGE, age);
		args.put(KEY_ADD_COME_DATE_TIME, comeDateTime);
		args.put(KEY_ADD_CREATE_DATE, createDate);
		args.put(KEY_ADD_SEND_MSG, sendMsg);
		args.put(KEY_ADD_CASE_NAME, caseName);
		args.put(KEY_ADD_CANT_DESCRIPTION, cantDescription);
		args.put(KEY_ADD_SALE_PROGRESS, saleProgress);
		args.put(KEY_ADD_CUSTOMER_ADDRESS, customerAddress);
		args.put(KEY_ADD_CONTACT_ADDRESS, contactAddress);
		args.put(KEY_ADD_COMMENT, comment);
		args.put(KEY_ADD_REQUEST, request);
		args.put(KEY_ADD_COST, cost);
		args.put(KEY_ADD_WORK_DATE_TIME, workDateTime);
		args.put(KEY_ADD_IS_UPLOAD, isUpload);
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
					KEY_ADD_CELL_PHONE_NUMBER, KEY_ADD_COMPANY_PHONE, KEY_ADD_EMAIL, KEY_ADD_MALE,
					KEY_ADD_BIRTHDAY, KEY_ADD_MSG_FROM, KEY_ADD_TITLE, KEY_ADD_JOB,
					KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_COME_DATE_TIME, KEY_ADD_CREATE_DATE,
					KEY_ADD_SEND_MSG, KEY_ADD_CASE_NAME, KEY_ADD_CANT_DESCRIPTION,
					KEY_ADD_SALE_PROGRESS, KEY_ADD_CUSTOMER_ADDRESS, KEY_ADD_CONTACT_ADDRESS,
					KEY_ADD_COMMENT, KEY_ADD_REQUEST, KEY_ADD_COST, KEY_ADD_WORK_DATE_TIME, KEY_ADD_IS_UPLOAD },
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
			String cellphoneNumer, String companyPhone, String email, String male, String birthday,
			String msgFrom, String title, String job, String introducer, String age,
			String comeDateTime, String createDate, int sendMsg, String caseName,
			String cantDescription, String saleProgress, String customerAddress,
			String contactAddress, String comment, String request, String cost, String workDateTime, int isUpload)
	{
		if (db.isOpen())
		{
			ContentValues args = new ContentValues();
			args.put(KEY_ADD_CUSTOMER_NAME, customerName);
			args.put(KEY_ADD_PHONE_NUMBER, phoneNumber);
			args.put(KEY_ADD_CELL_PHONE_NUMBER, cellphoneNumer);
			args.put(KEY_ADD_COMPANY_PHONE, companyPhone);
			args.put(KEY_ADD_EMAIL, email);
			args.put(KEY_ADD_MALE, male);
			args.put(KEY_ADD_BIRTHDAY, birthday);
			args.put(KEY_ADD_MSG_FROM, msgFrom);
			args.put(KEY_ADD_TITLE, title);
			args.put(KEY_ADD_JOB, job);
			args.put(KEY_ADD_INTRODUCER, introducer);
			args.put(KEY_ADD_AGE, age);
			args.put(KEY_ADD_COME_DATE_TIME, comeDateTime);
			args.put(KEY_ADD_CREATE_DATE, createDate);
			args.put(KEY_ADD_SEND_MSG, sendMsg);
			args.put(KEY_ADD_CASE_NAME, caseName);
			args.put(KEY_ADD_CANT_DESCRIPTION, cantDescription);
			args.put(KEY_ADD_SALE_PROGRESS, saleProgress);
			args.put(KEY_ADD_CUSTOMER_ADDRESS, customerAddress);
			args.put(KEY_ADD_CONTACT_ADDRESS, contactAddress);
			args.put(KEY_ADD_COMMENT, comment);
			args.put(KEY_ADD_REQUEST, request);
			args.put(KEY_ADD_COST, cost);
			args.put(KEY_ADD_WORK_DATE_TIME, workDateTime);
			args.put(KEY_ADD_IS_UPLOAD, isUpload);
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
