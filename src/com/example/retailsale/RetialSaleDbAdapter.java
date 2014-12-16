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
import android.database.sqlite.SQLiteException;
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

    private static final String USER_TABLE = "user";

    private static final String APP_USER_TABLE = "app_user";

    private static final String CREATE_TABLE_STRING = "create table ";

    public static final String ORDER_DESC = " desc";

    /************************************************************************** for add customer (16) */
    public static final String KEY_ADD_CUSTOMER_ID = "_id";

    public static final String KEY_ADD_CUSTOMER_NAME = "customer_name";

    public static final String KEY_ADD_HOME = "home";

    public static final String KEY_ADD_MOBILE = "mobile";

    public static final String KEY_ADD_COMPANY = "company";

    public static final String KEY_ADD_EMAIL = "email";

    public static final String KEY_ADD_SEX = "sex";

    public static final String KEY_ADD_BIRTHDAY = "birthday";

    public static final String KEY_ADD_INFO = "info";

    public static final String KEY_ADD_TITLE = "title";

    public static final String KEY_ADD_JOB = "job";

    public static final String KEY_ADD_INTRODUCER = "introducer";

    public static final String KEY_ADD_AGE = "age";

    public static final String KEY_ADD_MEMO = "memo";

    public static final String KEY_ADD_VISIT_DATE = "visit_date";

    public static final String KEY_ADD_CREATOR = "creator";

    public static final String KEY_ADD_CREATOR_GROUP = "creator_group";

    public static final String KEY_ADD_CREATE_DATE = "create_date";

    /************************************************************************** for add customer */
    /************************************************************************** for order measure(8 or 9) */
    public static final String KEY_ADD_SEND_MSG = "send_msg";

    public static final String KEY_WORK_ALIAS = "work_alias";

    public static final String KEY_STATUS_COMMENT = "status_comment";

    public static final String KEY_STATUS = "status";

    public static final String KEY_WORK = "work";

    public static final String KEY_WROK_POSTCODE = "work_postcode";

    public static final String KEY_CONTACT = "contact";

    public static final String KEY_CONTACT_POSTCODE = "contact_postcode";

    public static final String KEY_SPACE = "space";

    public static final String KEY_BUDGET = "budget";

    public static final String KEY_RESERVATION_DATE = "reservation_date";

    public static final String KEY_REPAIR_ITEM = "repair_item";

    public static final String KEY_AREA = "area";

    public static final String KEY_IS_UPLOAD = "is_upload"; // the field to
                                                            // check upload or
                                                            // not

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
    /************************************************************************** for group */
    public static final String KEY_USER_ID = "_id";

    public static final String KEY_USER_SERIAL = "user_serial";

    public static final String KEY_USER_NAME = "user_name";

    public static final String KEY_USER_GROUP = "user_group";

    public static final String KEY_USER_TYPE = "user_type";

    public static final String KEY_USER_GROUP_NAMING = "user_group_naming";

    public static final String KEY_USER_TYPE_NAMING = "user_type_naming";

    /************************************************************************** for group */

    /************************************************************************** for app user */
    public static final String KEY_APP_USER_ID = "_id";

    public static final String KEY_APP_USER_ACCOUNT = "user_account";

    public static final String KEY_APP_USER_PASSWORD = "user_password";

    public static final String KEY_APP_USER_SERIAL = "user_serial";

    public static final String KEY_APP_USER_GROUP = "user_group";

    /************************************************************************** for app user */
    public static final int NOTUPLOAD = 0;

    public static final int UPLOAD = 1;

    private static final String ADD_CUSTOMER_CREATE = CREATE_TABLE_STRING + ADD_CUSTOMER_TABLE
            + "(" + KEY_ADD_CUSTOMER_ID + " INTEGER PRIMARY KEY," + KEY_ADD_CUSTOMER_NAME
            + " TEXT," + KEY_ADD_HOME + " TEXT," + KEY_ADD_MOBILE + " TEXT," + KEY_ADD_COMPANY
            + " TEXT," + KEY_ADD_EMAIL + " TEXT," + KEY_ADD_SEX + " INTEGER," + KEY_ADD_BIRTHDAY
            + " TEXT," + KEY_ADD_INFO + " INTEGER," + KEY_ADD_TITLE + " INTEGER," + KEY_ADD_JOB
            + " INTEGER," + KEY_ADD_INTRODUCER + " TEXT," + KEY_ADD_AGE + " INTEGER,"
            + KEY_ADD_MEMO + " TEXT," + KEY_ADD_VISIT_DATE + " TEXT," + KEY_ADD_CREATOR
            + " INTEGER," + KEY_ADD_CREATOR_GROUP + " INTEGER," + KEY_ADD_CREATE_DATE + " TEXT,"
            + KEY_ADD_SEND_MSG + " INTEGER," + KEY_WORK_ALIAS + " TEXT," + KEY_STATUS_COMMENT
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_WORK + " TEXT," + KEY_WROK_POSTCODE
            + " TEXT," + KEY_CONTACT + " TEXT," + KEY_CONTACT_POSTCODE + " TEXT," + KEY_SPACE
            + " INTEGER," + KEY_BUDGET + " INTEGER," + KEY_RESERVATION_DATE + " TEXT,"
            + KEY_REPAIR_ITEM + " INTEGER," + KEY_AREA + " INTEGER," + KEY_IS_UPLOAD + " INTEGER"
            + ");";

    private static final String DATA_OPTION_CREATE = CREATE_TABLE_STRING + DATA_OPTION_TABLE + "("
            + KEY_DATA_OPTION_ID + " INTEGER PRIMARY KEY," + KEY_DATA_OPTION_TYPE + " INTEGER,"
            + KEY_DATA_OPTION_ALIAS + " TEXT," + KEY_DATA_OPTION_SERIAL + " INTEGER,"
            + KEY_DATA_OPTION_NAME + " TEXT" + ");";

    private static final String USER_CREATE = CREATE_TABLE_STRING + USER_TABLE + "(" + KEY_USER_ID
            + " INTEGER PRIMARY KEY," + KEY_USER_SERIAL + " INTEGER," + KEY_USER_NAME + " TEXT,"
            + KEY_USER_GROUP + " INTEGER," + KEY_USER_TYPE + " INTEGER," + KEY_USER_GROUP_NAMING
            + " TEXT," + KEY_USER_TYPE_NAMING + " TEXT" + ");";

    private static final String APP_USER_CREATE = CREATE_TABLE_STRING + APP_USER_TABLE + "("
            + KEY_APP_USER_ID + " INTEGER PRIMARY KEY," + KEY_APP_USER_ACCOUNT + " TEXT,"
            + KEY_APP_USER_PASSWORD + " TEXT," + KEY_APP_USER_SERIAL + " INTEGER,"
            + KEY_APP_USER_GROUP + " INTEGER" + ");";

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
            // createDatabase();
        }

        /**
         * Creates empty database on system and rewrites it with existing
         * database
         * 
         * @throws IOException
         */
        @SuppressWarnings("unused")
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

        @SuppressWarnings("unused")
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
            db.execSQL(USER_CREATE);
            db.execSQL(APP_USER_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

            db.execSQL("DROP TABLE IF EXISTS " + ADD_CUSTOMER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATA_OPTION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + APP_USER_TABLE);
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

    public boolean isDbOpen()
    {

        if (db != null)
            return db.isOpen();
        else return false;
    }

    /** Get all customer from customer table */
    public Cursor getAllCustomer()
    {

        try
        {
            return db.query(ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
                    KEY_ADD_CUSTOMER_NAME, KEY_ADD_HOME, KEY_ADD_MOBILE, KEY_ADD_COMPANY,
                    KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY, KEY_ADD_INFO, KEY_ADD_TITLE,
                    KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_MEMO, KEY_ADD_VISIT_DATE,
                    KEY_ADD_CREATOR, KEY_ADD_CREATOR_GROUP, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG,
                    KEY_WORK_ALIAS, KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK, KEY_WROK_POSTCODE,
                    KEY_CONTACT, KEY_CONTACT_POSTCODE, KEY_SPACE, KEY_BUDGET, KEY_RESERVATION_DATE,
                    KEY_REPAIR_ITEM, KEY_AREA, KEY_IS_UPLOAD }, null, null, null, null,
                    KEY_ADD_CREATE_DATE + ORDER_DESC);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** Get all option from option table */
    public Cursor getAllOption()
    {

        try
        {
            return db.query(DATA_OPTION_TABLE, new String[] { KEY_DATA_OPTION_ID,
                    KEY_DATA_OPTION_TYPE, KEY_DATA_OPTION_ALIAS, KEY_DATA_OPTION_SERIAL,
                    KEY_DATA_OPTION_NAME }, null, null, null, null, null);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** Get all user from user table */
    public Cursor getAllUser()
    {

        try
        {
            return db.query(USER_TABLE, new String[] { KEY_USER_ID, KEY_USER_SERIAL, KEY_USER_NAME,
                    KEY_USER_GROUP, KEY_USER_TYPE, KEY_USER_GROUP_NAMING, KEY_USER_TYPE_NAMING },
                    null, null, null, null, null);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** Get all app user from user table */
    public Cursor getAllAppUser()
    {

        try
        {
            return db.query(APP_USER_TABLE, new String[] { KEY_APP_USER_ID, KEY_APP_USER_ACCOUNT,
                    KEY_APP_USER_PASSWORD, KEY_APP_USER_SERIAL, KEY_APP_USER_GROUP }, null, null,
                    null, null, null);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** Insert customer to customer table */
    public long create(String customerName, String home, String mobile, String company,
            String email, int sex, String birthday, int info, int title, int job,
            String introducer, int age, String memo, String visitDate, int creator,
            int creatorGroup, String createDate, int sendMsg, String workAlias,
            String statusComment, int status, String work, String workPostcode, String contact,
            String contactPostcode, int space, int budget, String reservationDate, int repairItem,
            int area, int isUpload)
    {

        ContentValues args = new ContentValues();
        args.put(KEY_ADD_CUSTOMER_NAME, customerName);
        args.put(KEY_ADD_HOME, home);
        args.put(KEY_ADD_MOBILE, mobile);
        args.put(KEY_ADD_COMPANY, company);
        args.put(KEY_ADD_EMAIL, email);
        args.put(KEY_ADD_SEX, sex);
        args.put(KEY_ADD_BIRTHDAY, birthday);
        args.put(KEY_ADD_INFO, info);
        args.put(KEY_ADD_TITLE, title);
        args.put(KEY_ADD_JOB, job);
        args.put(KEY_ADD_INTRODUCER, introducer);
        args.put(KEY_ADD_AGE, age);
        args.put(KEY_ADD_MEMO, memo);
        args.put(KEY_ADD_VISIT_DATE, visitDate);
        args.put(KEY_ADD_CREATOR, creator);
        args.put(KEY_ADD_CREATOR_GROUP, creatorGroup);
        args.put(KEY_ADD_CREATE_DATE, createDate);
        args.put(KEY_ADD_SEND_MSG, sendMsg);
        args.put(KEY_WORK_ALIAS, workAlias);
        args.put(KEY_STATUS_COMMENT, statusComment);
        args.put(KEY_STATUS, status);
        args.put(KEY_WORK, work);
        args.put(KEY_WROK_POSTCODE, workPostcode);
        args.put(KEY_CONTACT, contact);
        args.put(KEY_CONTACT_POSTCODE, contactPostcode);
        args.put(KEY_SPACE, space);
        args.put(KEY_BUDGET, budget);
        args.put(KEY_RESERVATION_DATE, reservationDate);
        args.put(KEY_REPAIR_ITEM, repairItem);
        args.put(KEY_AREA, area);
        args.put(KEY_IS_UPLOAD, isUpload);
        return db.insert(ADD_CUSTOMER_TABLE, null, args);
    }

    /** Insert option to option table */
    public long create(int optionType, String optionAlias, int optionSerial, String optionName)
    {

        ContentValues args = new ContentValues();
        args.put(KEY_DATA_OPTION_TYPE, optionType);
        args.put(KEY_DATA_OPTION_ALIAS, optionAlias);
        args.put(KEY_DATA_OPTION_SERIAL, optionSerial);
        args.put(KEY_DATA_OPTION_NAME, optionName);
        return db.insert(DATA_OPTION_TABLE, null, args);
    }

    /** Insert user to user table */
    public long create(int userSerial, String userName, int userGroup, int userType,
            String userGroupNm, String userTypeNm)
    {

        ContentValues args = new ContentValues();
        args.put(KEY_USER_SERIAL, userSerial);
        args.put(KEY_USER_NAME, userName);
        args.put(KEY_USER_GROUP, userGroup);
        args.put(KEY_USER_TYPE, userType);
        args.put(KEY_USER_GROUP_NAMING, userGroupNm);
        args.put(KEY_USER_TYPE_NAMING, userTypeNm);
        return db.insert(USER_TABLE, null, args);
    }

    /** Insert app user to app user table */
    public long create(String userAccount, String userPassword, int userSerial, int userGroup)
    {

        ContentValues args = new ContentValues();
        args.put(KEY_APP_USER_ACCOUNT, userAccount);
        args.put(KEY_APP_USER_PASSWORD, userPassword);
        args.put(KEY_APP_USER_SERIAL, userSerial);
        args.put(KEY_APP_USER_GROUP, userGroup);

        return db.insert(APP_USER_TABLE, null, args);
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

    /** Delete one user from user table */
    public boolean deleteUser(String rowId)
    {

        if (db != null && db.isOpen())
            return db.delete(USER_TABLE, KEY_USER_ID + "=" + rowId, null) > 0;
        else return false;
    }

    /** Delete one app user from app user table */
    public boolean deleteAppUser(String rowId)
    {

        if (db != null && db.isOpen())
            return db.delete(APP_USER_TABLE, KEY_APP_USER_ID + "=" + rowId, null) > 0;
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

    /** Delete all user from user table */
    public boolean deleteAllUser()
    {

        if (db != null && db.isOpen())
            return db.delete(USER_TABLE, null, null) > 0;
        else return false;
    }

    /** Delete all app user from app user table */
    public boolean deleteAllAppUser()
    {

        if (db != null && db.isOpen())
            return db.delete(APP_USER_TABLE, null, null) > 0;
        else return false;
    }

    /** Query single customer */
    public Cursor getCustomer(String rowId) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
                    KEY_ADD_CUSTOMER_NAME, KEY_ADD_HOME, KEY_ADD_MOBILE, KEY_ADD_COMPANY,
                    KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY, KEY_ADD_INFO, KEY_ADD_TITLE,
                    KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_MEMO, KEY_ADD_VISIT_DATE,
                    KEY_ADD_CREATOR, KEY_ADD_CREATOR_GROUP, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG,
                    KEY_WORK_ALIAS, KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK, KEY_WROK_POSTCODE,
                    KEY_CONTACT, KEY_CONTACT_POSTCODE, KEY_SPACE, KEY_BUDGET, KEY_RESERVATION_DATE,
                    KEY_REPAIR_ITEM, KEY_AREA, KEY_IS_UPLOAD }, KEY_ADD_CUSTOMER_ID + "=" + rowId,
                    null, null, null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query customer by creator */
    public Cursor getCustomerByCreator(int creator) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
                    KEY_ADD_CUSTOMER_NAME, KEY_ADD_HOME, KEY_ADD_MOBILE, KEY_ADD_COMPANY,
                    KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY, KEY_ADD_INFO, KEY_ADD_TITLE,
                    KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_MEMO, KEY_ADD_VISIT_DATE,
                    KEY_ADD_CREATOR, KEY_ADD_CREATOR_GROUP, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG,
                    KEY_WORK_ALIAS, KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK, KEY_WROK_POSTCODE,
                    KEY_CONTACT, KEY_CONTACT_POSTCODE, KEY_SPACE, KEY_BUDGET, KEY_RESERVATION_DATE,
                    KEY_REPAIR_ITEM, KEY_AREA, KEY_IS_UPLOAD }, KEY_ADD_CREATOR + "=" + creator,
                    null, null, null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query customer by creator and not upload */
    public Cursor getCustomerByCreatorNotUpload(int creator) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
                    KEY_ADD_CUSTOMER_NAME, KEY_ADD_HOME, KEY_ADD_MOBILE, KEY_ADD_COMPANY,
                    KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY, KEY_ADD_INFO, KEY_ADD_TITLE,
                    KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_MEMO, KEY_ADD_VISIT_DATE,
                    KEY_ADD_CREATOR, KEY_ADD_CREATOR_GROUP, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG,
                    KEY_WORK_ALIAS, KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK, KEY_WROK_POSTCODE,
                    KEY_CONTACT, KEY_CONTACT_POSTCODE, KEY_SPACE, KEY_BUDGET, KEY_RESERVATION_DATE,
                    KEY_REPAIR_ITEM, KEY_AREA, KEY_IS_UPLOAD }, KEY_ADD_CREATOR + "=" + creator
                    + " AND " + KEY_IS_UPLOAD + "=" + NOTUPLOAD, null, null, null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query all customer are not upload */
    public Cursor getCustomerNotUpload() throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, ADD_CUSTOMER_TABLE, new String[] { KEY_ADD_CUSTOMER_ID,
                    KEY_ADD_CUSTOMER_NAME, KEY_ADD_HOME, KEY_ADD_MOBILE, KEY_ADD_COMPANY,
                    KEY_ADD_EMAIL, KEY_ADD_SEX, KEY_ADD_BIRTHDAY, KEY_ADD_INFO, KEY_ADD_TITLE,
                    KEY_ADD_JOB, KEY_ADD_INTRODUCER, KEY_ADD_AGE, KEY_ADD_MEMO, KEY_ADD_VISIT_DATE,
                    KEY_ADD_CREATOR, KEY_ADD_CREATOR_GROUP, KEY_ADD_CREATE_DATE, KEY_ADD_SEND_MSG,
                    KEY_WORK_ALIAS, KEY_STATUS_COMMENT, KEY_STATUS, KEY_WORK, KEY_WROK_POSTCODE,
                    KEY_CONTACT, KEY_CONTACT_POSTCODE, KEY_SPACE, KEY_BUDGET, KEY_RESERVATION_DATE,
                    KEY_REPAIR_ITEM, KEY_AREA, KEY_IS_UPLOAD }, KEY_IS_UPLOAD + "=" + NOTUPLOAD,
                    null, null, null, null, null);
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

    /** Query user group option */
    public Cursor getOptionByOptionSerial(int optionSerial) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, DATA_OPTION_TABLE, new String[] { KEY_DATA_OPTION_ID,
                    KEY_DATA_OPTION_TYPE, KEY_DATA_OPTION_ALIAS, KEY_DATA_OPTION_SERIAL,
                    KEY_DATA_OPTION_NAME }, KEY_DATA_OPTION_SERIAL + "=" + optionSerial, null,
                    null, null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query user group option */
    public Cursor getOptionGroupByOptionAlias() throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, DATA_OPTION_TABLE, new String[] { KEY_DATA_OPTION_ID,
                    KEY_DATA_OPTION_TYPE, KEY_DATA_OPTION_ALIAS, KEY_DATA_OPTION_SERIAL,
                    KEY_DATA_OPTION_NAME }, null, null, KEY_DATA_OPTION_ALIAS, null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query user by creator */
    public Cursor getUserByCreator(int creator) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, USER_TABLE, new String[] { KEY_USER_ID, KEY_USER_SERIAL,
                    KEY_USER_NAME, KEY_USER_GROUP, KEY_USER_TYPE, KEY_USER_GROUP_NAMING,
                    KEY_USER_TYPE_NAMING }, KEY_USER_GROUP + "=" + creator, null, null, null, null,
                    null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Query app user by account and password */
    public Cursor getUserByAccountAndPassword(String account, String password) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.query(true, APP_USER_TABLE, new String[] { KEY_APP_USER_ID,
                    KEY_APP_USER_ACCOUNT, KEY_APP_USER_PASSWORD, KEY_APP_USER_SERIAL,
                    KEY_APP_USER_GROUP }, KEY_APP_USER_ACCOUNT + "=" + "\"" + account + "\""
                    + " AND " + KEY_APP_USER_PASSWORD + "=" + "\"" + password + "\"", null, null,
                    null, null, null);
            if (cursor != null) { return cursor; }
            return null;
        }
        else
        {
            return null;
        }
    }

    /** Update the customer table */
    public boolean updateCustomer(String rowID, String customerName, String home, String mobile,
            String company, String email, int sex, String birthday, int info, int title, int job,
            String introducer, int age, String memo, String visitDate, int creator,
            int creatorGroup, String createDate, int sendMsg, String workAlias,
            String statusComment, int status, String work, String workPostcode, String contact,
            String contactPostCode, int space, int budget, String workDate, int repairItem,
            int area, int isUpload)
    {

        if (db.isOpen())
        {
            ContentValues args = new ContentValues();
            args.put(KEY_ADD_CUSTOMER_NAME, customerName);
            args.put(KEY_ADD_HOME, home);
            args.put(KEY_ADD_MOBILE, mobile);
            args.put(KEY_ADD_COMPANY, company);
            args.put(KEY_ADD_EMAIL, email);
            args.put(KEY_ADD_SEX, sex);
            args.put(KEY_ADD_BIRTHDAY, birthday);
            args.put(KEY_ADD_INFO, info);
            args.put(KEY_ADD_TITLE, title);
            args.put(KEY_ADD_JOB, job);
            args.put(KEY_ADD_INTRODUCER, introducer);
            args.put(KEY_ADD_AGE, age);
            args.put(KEY_ADD_MEMO, memo);
            args.put(KEY_ADD_VISIT_DATE, visitDate);
            args.put(KEY_ADD_CREATOR, creator);
            args.put(KEY_ADD_CREATOR_GROUP, creatorGroup);
            args.put(KEY_ADD_CREATE_DATE, createDate);
            args.put(KEY_ADD_SEND_MSG, sendMsg);
            args.put(KEY_WORK_ALIAS, workAlias);
            args.put(KEY_STATUS_COMMENT, statusComment);
            args.put(KEY_STATUS, status);
            args.put(KEY_WORK, work);
            args.put(KEY_CONTACT, contact);
            args.put(KEY_SPACE, space);
            args.put(KEY_BUDGET, budget);
            args.put(KEY_RESERVATION_DATE, workDate);
            args.put(KEY_IS_UPLOAD, isUpload);
            return db.update(ADD_CUSTOMER_TABLE, args, KEY_ADD_CUSTOMER_ID + "=" + rowID, null) > 0;
        }
        else
        {
            return false;
        }
    }

    /** Update the customer table(only isUploaded) */
    public boolean updateCustomer(long rowID, int isUpload)
    {

        if (db.isOpen())
        {
            ContentValues args = new ContentValues();
            args.put(KEY_IS_UPLOAD, isUpload);
            return db.update(ADD_CUSTOMER_TABLE, args, KEY_ADD_CUSTOMER_ID + "=" + rowID, null) > 0;
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

    /** Update the user table */
    public boolean updateUser(int rowID, int userSerial, String userName, int userGroup,
            int userType, String userGroupNm, String userTypeNm)
    {

        if (db.isOpen())
        {
            ContentValues args = new ContentValues();
            args.put(KEY_USER_SERIAL, userSerial);
            args.put(KEY_USER_NAME, userName);
            args.put(KEY_USER_GROUP, userGroup);
            args.put(KEY_USER_TYPE, userType);
            args.put(KEY_USER_GROUP_NAMING, userGroupNm);
            args.put(KEY_USER_TYPE_NAMING, userTypeNm);
            return db.update(USER_TABLE, args, KEY_USER_ID + "=" + rowID, null) > 0;
        }
        else
        {
            return false;
        }
    }

    /** Query single customer with msgFrom and createDate */
    // For test
    public Cursor getInfoAndCreateDate(String info, String createDate) throws SQLException
    {

        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery("SELECT * FROM mails WHERE " + KEY_ADD_INFO + " = " + "\""
                    + info + "\"" + " AND " + KEY_ADD_CREATE_DATE + " = " + "\"" + createDate
                    + "\"", null);
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
