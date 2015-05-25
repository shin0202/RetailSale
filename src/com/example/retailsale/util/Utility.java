package com.example.retailsale.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import com.example.retailsale.UploadReceiver;
import com.example.retailsale.fragment.SynchronizationFragment;
import com.example.retailsale.manager.HttpManager;
import com.example.retailsale.manager.dataoption.DataOption;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;

public class Utility
{
    private static final String TAG = "Utility";
    private static final int BOUNDS = 10;
    private static final String FILL_ZERO = "0";
    public static final String FILL_DASH = "-";
    public static final String LINE_FEED = "\n";
    public static final String DATE_STRING = "T";
    public static final String THUMB_PATH = ".thumb/";
    public static final String THUMB_PATH_FOR_SHOW = ".thumb";
//	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/retail/";
    public static final String FILE_PATH = "/sdcard/retail/";
    public static final String FILE_PATH_2 = "/sdcard/retail";
    public static final String LOG_FILE_PATH = "/sdcard/retail2/log.txt";
    public static final String LOG_FOLDER_PATH = "/sdcard/retail2/";
//    public static final String DEFAULT_BASE64_FOLDER_PATH = "/sdcard/retail/";
    public static final String REPLACE_SERVER_FOLDER = "C:\\Project\\_code\\testFolder";
    public static final String DEFAULT_PHOTO_ZIP = "/sdcard/retail.zip";
    public static final String REPLACE_STRING = ".jpg.txt";
    public static final String REPLACE_B_STRING = ".JPG.txt";
    public static final String REPLACE_JPEG_STRING = ".jpg";
    public static final String REPLACE_TXT_STRING = ".txt";
    public static final String SPACE_STRING = "";
    public static final int SHOW_WAITING_DIALOG = 999;
    public static final int DISMISS_WAITING_DIALOG = -999;
    public static final int SUCCESS = 1;
    public static final int FAILED = 0;
    public static final int FAILED_UPLOAD = -123;
    public static final String DEFAULT_VALUE_STRING = "-1";
    public static final int DEFAULT_ZERO_VALUE = 0;
    public static final int DEFAULT_NEGATIVE_VALUE = -1;
    public static final int NO_DATA = -1000;
    public static final long DEFAULT_ROW_ID = -1;
    
    private static File logFile;
    private static BufferedWriter outputContent;
    
    private static PendingIntent pendingIntent;
    
    private static final int ALARM_GAP = 1000 * 60 * 60 * 8;

    public class JSONTag
    {
        public static final String CONTENT_TYPE = "Content-type";
        public static final String FATCA_INFO = "FATCA-INFO";
    }

    public class AddCustomerJsonTag
    {
        public static final String CUSTOMER_ACCOUNT = "customerAccount";
        public static final String CUSTOMER_NAME = "custometName";
        public static final String CUSTOMER_Mobile = "customerMobile";
        public static final String CUSTOMER_HOME = "customerHome";
        public static final String CUSTOMER_COMPANY = "customerCompany";
        public static final String CUSTOMER_SEX = "customerSex";
        public static final String CUSTOMER_TITLE = "customerTitle";
        public static final String CUSTOMER_MAIL = "customerMail";
        public static final String CUSTOMER_VISIT_DATE = "customerVisitDate";
        public static final String CUSTOMER_INFO = "customerInfo";
        public static final String CUSTOMER_INTRODUCER = "customerIntroducer";
        public static final String CUSTOMER_JOB = "customerJob";
        public static final String CUSTOMER_AGE = "customerAge";
        public static final String CUSTOMER_MEMO = "customerMemo";
        public static final String CUSTOMER_BIRTH = "customerBirth";
        public static final String CREATOR = "creator";
        public static final String CREATOR_GROUP = "creatorGroup";
        public static final String CREATE_TIME = "createTime";

        public static final String CUSTOMER_RESERVATION = "customerReservation";

        public static final String RESERVATION_SERIAL = "ReservationSerial";
        public static final String CUSTOMER_SERIAL = "customerSerial";
        public static final String RESERVATION_DATE = "ReservationDate";
        public static final String RESERVATION_WORK = "ReservationWork";
        public static final String WORK_POSTCODE = "WorkPostcode";
        public static final String RESERVATION_WORK_ALIAS = "ReservationWorkAlias";
        public static final String RESERVATION_CONTACT = "ReservationContact";
        public static final String CONTACT_POSTCODE = "ContactPostcode";
        public static final String RESERVATION_SPACE = "ReservationSpace";
        public static final String RESERVATION_STATUS = "ReservationStatus";
        public static final String RESERVATION_UPDATE_TIME = "ReservationUpateTime";
        public static final String RESERVATION_STATUS_COMMENT = "ReservationStatusComment";
        public static final String RESERVATION_BUDGET = "ReservationBudget";
        public static final String RESERVATION_REPAIR_ITEM = "ReservationRepairItem";
        public static final String RESERVATION_AREA = "ReservationArea";
        public static final String RESERVATION_DATA_SERIAL = "ReservationDataSerial";
    }

    public class HeaderContent
    {
        public static final String CONTENT_TYPE = "application/json;charset=utf-8";
    }

    public class LoginField
    {
        public static final String APP_DATA = "app_data";
        public static final String DATA = "data";
        public static final String IP_DATA = "ip_data";
        public static final String ID = "id";
        public static final String PASSWORD = "password";
        public static final String USER_SERIAL = "user_serial";
        public static final String USER_GROUP = "user_group";
        public static final String LOGIN_KEY = "login_key";
        public static final String SERVER_IP = "server_ip";
        public static final String APP_ACCOUNT = "app_account";
        public static final String APP_PASSWORD = "app_password";
        public static final String APP_GROUP = "app_group";
    }
    
    public class County
    {
        public static final int NONE = 0;
        public static final int KEELUNG_CITY = 1;
        public static final int TAIPEI_CITY = 2;
        public static final int NEW_TAIPEI_CITY = 3;
        public static final int YILAN_COUNTY = 4;
        public static final int HSINCHU_CITY = 5;
        public static final int HSINCHU_COUNTY = 6;
        public static final int TAOYUAN_COUNTY = 7;
        public static final int MIAOLI_COUNTY = 8;
        public static final int TAICHUNG_CITY = 9;
        public static final int CHANGHUA_COUNTY = 10;
        
        public static final int NANTOU_COUNTY = 11;
        public static final int CHIAYI_CITY = 12;
        public static final int CHIAYI_COUNTY = 13;
        public static final int YUNLIN_COUNTY = 14;
        public static final int TAINAN_CITY = 15;
        public static final int KAOHSIUNG_CITY = 16;
        public static final int PINGDONG_COUNTY = 17;
        public static final int TAIDONG_COUNTY = 18;
        public static final int HUALIAN_COUNTY = 19;
        public static final int KINMEN_COUNTY = 20;
        
        public static final int LIANJIANG_COUNTY = 21;
        public static final int PENGHU_COUNTY = 22;
        public static final int SOUTH_SEA_ISLANDS = 23;
    }
    
    public class AlarmTime
    {
        public static final String ALARM_TIME = "alarm_time";
        public static final String ALARM_TIME_1 = "alarm_time_1";
        public static final String ALARM_TIME_2 = "alarm_time_2";
        public static final String ALARM_TIME_3 = "alarm_time_3";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for home
    public static boolean isPhoneValid(String number)
    {
        boolean isValid = false;

        String expression = "[0-9]{2,4}-[0-9]{5,8}";
        CharSequence inputStr = number;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for company
    public static boolean isCompanyPhoneValid(String number)
    {
        boolean isValid = false;
        String expression;
        if (number.contains("#"))
        {
            expression = "[0-9]{2,4}-[0-9]{5,8}#[0-9]{1,4}";
        }
        else
        {
            expression = "[0-9]{2,4}-[0-9]{5,8}";
        }

        CharSequence inputStr = number;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for cellphone
    public static boolean isCellphoneValid(String number)
    {
        boolean isValid = false;

//		String expression = "[0-9]{4}-[0-9]{6}";
        String expression = "[0-9]{10}";
        CharSequence inputStr = number;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isBirthdayValid(String inDate)
    {

        if (inDate == null) return false;

        //set the format to use as a constructor argument
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (inDate.trim().length() != dateFormat.toPattern().length()) return false;

        dateFormat.setLenient(false);

        try
        {
            //parse the inDate parameter
            dateFormat.parse(inDate.trim());
        }
        catch (ParseException pe)
        {
            return false;
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isEmailValid(String email)
    {
        boolean isValid = false;

        String expression = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String covertDateToString(int year, int month, int day)
    {
        Log.d(TAG, "year: " + year + " month: " + month + " day: " + day);
        StringBuilder dateTimeString = new StringBuilder();

        dateTimeString.append(year).append(FILL_DASH);

        if (month < BOUNDS)
        {
            dateTimeString.append(FILL_ZERO);
        }
        dateTimeString.append(month).append(FILL_DASH);

        if (day < BOUNDS)
        {
            dateTimeString.append(FILL_ZERO);
        }
        dateTimeString.append(day).append(DATE_STRING);

        return dateTimeString.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String covertTimeToString(int hour, int minute)
    {
        Log.d(TAG, " hour: " + hour + " minute: " + minute);
        StringBuilder timeString = new StringBuilder();

        if (hour < BOUNDS)
        {
            timeString.append(FILL_ZERO);
        }
        timeString.append(hour).append(":");

        if (minute < BOUNDS)
        {
            timeString.append(FILL_ZERO);
        }
        timeString.append(minute);

        return timeString.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Integer[] parseDateTime(String dateTime)
    {
//        String testDate = "1986-12-11T15:30:20";
        Integer[] dateTimeArray = new Integer[6];
        
        // parse year to array index 0
        dateTimeArray[0] = Integer.parseInt(dateTime.substring(0, 4));
        
        // parse month, day, hour, minute, second to array index 1 ~ 5
        for (int i = 0; i < 5; i++)
        {
            dateTimeArray[i + 1] = Integer.parseInt(dateTime.substring(5 + i * 3, 7 + i * 3));
        }
        
        Log.d(TAG, "year : " + dateTimeArray[0] + " month : " + dateTimeArray[1] + " day : " + dateTimeArray[2]
                + " hour : " + dateTimeArray[3] + " minute : " + dateTimeArray[4] + " second : " + dateTimeArray[5]);
        
        return dateTimeArray;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getCurrentDateTime()
    {
        String dateTime = Utility.SPACE_STRING;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateTime = dateFormat.format(date).toString();

        return dateTime;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getCurrentYear()
    {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);      // The current year as an int
        return year;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getDays(String strDate)
    {
//        String strDate = "2012-02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
        Calendar calendar = new GregorianCalendar(); 
        Date date1 = null;
        try
        {
            date1 = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        } 
        calendar.setTime(date1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String covertDateStringToServer(String date)
    {
        return date.replace(" ", DATE_STRING);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void createFolder(String path)
    {
        if (path != null)
        {
            File directories = new File(path);
            boolean result = directories.mkdirs();

            Log.d(TAG, "directories is not exist and created ? " + result);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean openLogFile(String path)
    {
        if (!checkStorage())
        {
            return false;
        }
        
        try
        {
            logFile = new File(path);
            if (!logFile.exists())
            {
                logFile.createNewFile();
            }
            outputContent = new BufferedWriter(new FileWriter(logFile, true));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean clearLogContent()
    {
        return closeLogFile() && setBufferedWriter(false) && writeLogData(SPACE_STRING) && closeLogFile()
                && openLogFile(LOG_FILE_PATH);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean setBufferedWriter(boolean isAppend)
    {
        try
        {
            outputContent = new BufferedWriter(new FileWriter(logFile, isAppend));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean closeLogFile()
    {
        try
        {
            outputContent.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean writeLogData(String data)
    {
        Log.d(TAG, "data === " + data);
        try
        {
            outputContent.write(data);
            outputContent.newLine();
            outputContent.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String readFile(String path)
    {
        BufferedReader reader;
        StringBuilder readContent = new StringBuilder();
        try
        {
            reader = new BufferedReader(new FileReader(path));
            String line = Utility.SPACE_STRING;
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    readContent.append(line).append("\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return readContent.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int writeFile(String path, String data, boolean isText)
    {
        if (!checkStorage())
        {
            return 0;
        }
        
        try
        {
            File fakeFile = new File(path);
            if (!fakeFile.exists())
            {
                fakeFile.createNewFile();
            }
            BufferedWriter output;
            if (isText)
            {
                output = new BufferedWriter(new FileWriter(fakeFile, isText));
            }
            else
            {
                output = new BufferedWriter(new FileWriter(fakeFile));
            }
            
            output.write(data);
            
            if (isText)
            {
                output.newLine();
            }
            
            output.flush();
            output.close();
            return 1;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return Utility.DEFAULT_ZERO_VALUE;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Utility.DEFAULT_ZERO_VALUE;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // write external
    public void writeToSDcard(String filename, String data)
    {

        String path = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(path + "/movietime");
        if (!dir.exists())
        {
            dir.mkdir();
        }

        try
        {
            File file = new File(path + "/movietime/" + filename);
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data.getBytes());
            fout.flush();
            fout.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.d(TAG, "Write to SDCARD!");
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // read external
    public String readFromSDcard(String filename)
    {

        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/movietime/" + filename);
        StringBuilder sb = new StringBuilder();
        try
        {
            FileInputStream fin = new FileInputStream(file);
            byte[] data = new byte[fin.available()];
            while (fin.read(data) != -1)
            {
                sb.append(new String(data));
            }
            fin.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // write internal
    public void writeDataToFile(String filename, String data, Activity activity)
    {

        try
        {
            // Context.MODE_XXX
            FileOutputStream fout = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            fout.write(data.getBytes());
            fout.flush();
            fout.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // read internal
    public String readDataFromFile(String filename, Activity activity)
    {

        String result = null;
        try
        {
            StringBuilder sb = new StringBuilder();
            FileInputStream fin = activity.openFileInput(filename);
            byte[] data = new byte[fin.available()];
            while (fin.read(data) != -1)
            {
                sb.append(new String(data));
            }
            fin.close();
            result = sb.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean checkStorage()
    {
        String state = Environment.getExternalStorageState();
        Log.d(TAG, "The system state : " + state);
        
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static byte[] decodeBase64(String content)
    {
        byte[] data = Base64.decode(content, Base64.DEFAULT);
        try
        {
            String text = new String(data, "UTF-16");
            Log.d(TAG, "decode string is " + text);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String encodeBase64(String content)
    {
        byte[] data = null;
        try
        {
            data = content.getBytes("UTF-16");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, "encode string is " + base64);

        return base64;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String encodeBase64(Bitmap bm, int quality)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos); // quality default is 100
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d(TAG, " imageEncoded is " + imageEncoded);

        return imageEncoded;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String encodeBase64FromPath(String path)
    {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
//    	byte[] b = baos.toByteArray(); 
        byte[] data = baos.toByteArray();
//        try {
//            data = content.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, "encode string is " + base64);

        return base64;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String generateMD5String(String source)
    {
        // test string
//        String testString = "catch12";
//        String resultString = Utility.SPACE_STRING;
//        
//        resultString = testMD52(testString);
//        Log.d(TAG, "resultString === " + resultString);

        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(source.getBytes("UTF-8"));
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++)
            {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return Utility.SPACE_STRING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Bitmap covertByteArrayToBitmap(byte[] data)
    {
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

        return bm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getFactaInfoHeader(String logType, String userNo, String userName, String userHostAddress,
            String actionName)
    {
        // "{\"LogType\":\"Operation\",\"UserNo\":\"095050\",\"UserName\":\"\",\"UserHostAddress\":\"127.0.0.1\",
        // \"ActionName\":\"http://fatcaweb/FATCA/FATCA/\"}"
        StringBuilder header = new StringBuilder().append("{\"LogType\":\"").append(logType).append("\",\"UserNo\":\"")
                .append(userNo).append("\",\"UserName\":\"").append(userName).append("\",\"UserHostAddress\":\"")
                .append(userHostAddress).append("\",\"ActionName\":\"").append(actionName).append("\"}");

        return header.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getFactaInfoHeader(String logType, String userNo, String userName, String userHostAddress,
            String actionName, String loginKey)
    {
        // "{\"LogType\":\"Operation\",\"UserNo\":\"095050\",\"UserName\":\"\",\"UserHostAddress\":\"127.0.0.1\",
        // \"ActionName\":\"http://fatcaweb/FATCA/FATCA/\",\"LoginKey\":\"\"}"
        StringBuilder header = new StringBuilder().append("{\"LogType\":\"").append(logType).append("\",\"UserNo\":\"")
                .append(userNo).append("\",\"UserName\":\"").append(userName).append("\",\"UserHostAddress\":\"")
                .append(userHostAddress).append("\",\"ActionName\":\"").append(actionName).append("\",\"LoginKey\":\"")
                .append(loginKey).append("\"}");

        return header.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void saveData(Context context, String id, String password, int userSerial, int userGroup,
            String loginKey)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        settings.edit().putString(Utility.LoginField.ID, id).putString(Utility.LoginField.PASSWORD, password)
                .putInt(Utility.LoginField.USER_SERIAL, userSerial).putInt(Utility.LoginField.USER_GROUP, userGroup)
                .putString(Utility.LoginField.LOGIN_KEY, loginKey).commit();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void saveAlarmTimeData(Context context, int time1, int time2, int time3)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.AlarmTime.ALARM_TIME, Utility.DEFAULT_ZERO_VALUE);
        settings.edit().putInt(Utility.AlarmTime.ALARM_TIME_1, time1).putInt(Utility.AlarmTime.ALARM_TIME_2, time2)
                .putInt(Utility.AlarmTime.ALARM_TIME_3, time3).commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean hadLogin(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        String id = settings.getString(Utility.LoginField.ID, "");

        if (id != null && !id.equals(Utility.SPACE_STRING))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getCreator(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        int creator = settings.getInt(Utility.LoginField.USER_SERIAL, -1);

        Log.d(TAG, "creator is " + creator);

        return creator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getCreatorGroup(Activity actiivty)
    {
        SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        int creatorGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);

        Log.d(TAG, "creator group is " + creatorGroup);

        return creatorGroup;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getAppCreatorGroup(Activity actiivty)
    {
        SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.APP_DATA, Utility.DEFAULT_ZERO_VALUE);
        int creatorGroup = settings.getInt(Utility.LoginField.APP_GROUP, -1);

        Log.d(TAG, "app creator group is " + creatorGroup);

        return creatorGroup;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getLoginKey(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        String loginKey = settings.getString(Utility.LoginField.LOGIN_KEY, "");

        Log.d(TAG, "loginKey is " + loginKey);

        return loginKey;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getAlarmTime(Context context, String tag)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.AlarmTime.ALARM_TIME, Utility.DEFAULT_ZERO_VALUE);
        int time = settings.getInt(tag, 0);

        Log.d(TAG, "time is " + time);

        return time;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean removeDirectory(File directory)
    {
        if (directory == null) return false;
        if (!directory.exists()) return true;
        if (!directory.isDirectory()) return false;
        String[] list = directory.list();
        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null)
        {
            for (int i = 0; i < list.length; i++)
            {
                File entry = new File(directory, list[i]);
                if (entry.isDirectory())
                {
                    if (!removeDirectory(entry)) return false;
                }
                else
                {
                    if (!entry.delete()) return false;
                }
            }
        }
        return directory.delete();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean removeFile(File file)
    {
        if (file.exists())
        {
            return file.delete();
        }
        
        return false;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void handleFileInfo(GsonFileInfo fileInfo, Handler handler)
    {
        if (fileInfo != null)
        {
            String fileName = fileInfo.getValue().get(0).getFileName();
            String filePath = fileInfo.getValue().get(0).getPath();
            String fileStream = fileInfo.getValue().get(0).getFileStream();
            
            Log.d(TAG, "fileName : " + fileName + " filePath : " + filePath);

            // 1. get file path
            String path = filePath.replace(Utility.REPLACE_SERVER_FOLDER, Utility.FILE_PATH_2).replace("\\", "/");
            
            Log.d(TAG, "path  is  ~~~~~~~~~~~~~~~~~~~~ " + path);

//            // 2. create the folder from file path
//            Utility.createFolder(path.toString());

            // 3. generate the MD5 string from file name
            String md5FileName = Utility.generateMD5String(fileName);
            Log.d(TAG, "md5FileName  is  ~~~~~~~~~~~~~~~~~~~~ " + md5FileName);

            // 4. To mix md5 file name and file data to "newData", then write data to file path(newFileName)
            StringBuilder newFileName = new StringBuilder().append(path.toString()).append("/").append(fileName)
                    .append(REPLACE_TXT_STRING);

            Log.d(TAG, "newFileName  is  ~~~~~~~~~~~~~~~~~~~~ " + newFileName.toString());

            Log.d(TAG, "*************************************************************** ");

            StringBuilder newData = new StringBuilder().append(md5FileName).append(fileStream);

            Log.d(TAG, "newData  is  ~~~~~~~~~~~~~~~~~~~~ " + newData.toString());

            int status = Utility.writeFile(newFileName.toString(), newData.toString(), false);
            
            // 5. to generate thumbnail
            generateThumbnail(fileName, path + "/" + Utility.THUMB_PATH, fileStream);

            Message msg = new Message();
            msg.what = SynchronizationFragment.SelectedItem.DOWNLOAD_PICTURE;
            msg.obj = fileName;
            msg.arg1 = status;

            handler.sendMessage(msg);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void generateThumbnail(final String fileName, final String filePath, final String fileStream)
    {
        Bitmap bm = null;
        String baseThumbnail;
        StringBuilder newFileName = new StringBuilder().append(filePath.toString()).append(fileName);

        try
        {
            bm = resizeFromByteArray(Utility.decodeBase64(fileStream));
            int width = bm.getWidth();
            int height = bm.getHeight();
            int newWidth = 120;
            int newHeight = 120;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            
            baseThumbnail = Utility.encodeBase64(bm, 100);
            
            Log.d(TAG, "newFileName : " + newFileName.toString());
            
            Utility.writeFile(newFileName.toString(), baseThumbnail.toString(), false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bm != null)
            {
                bm.recycle();
                bm = null;
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static BitmapFactory.Options getCustomsizeOptions()
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        ///14400 = 120 * 120
        options.inSampleSize = computeSampleSize(options, 14400);
        return options;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Bitmap resizeFromByteArray(byte[] paramArray)
    {
        return BitmapFactory.decodeByteArray(paramArray, 0, paramArray.length,  getCustomsizeOptions());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Bitmap resizeFromFile(File paramFile)
    {
        return BitmapFactory.decodeFile(paramFile.getAbsolutePath(), getCustomsizeOptions());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Bitmap resizeFromResourceId(Context paramContext, int paramInt)
    {
        return BitmapFactory.decodeStream(paramContext.getResources().openRawResource(paramInt), null,
                getCustomsizeOptions());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    protected static int computeSampleSize(final BitmapFactory.Options options, final int maxNumOfPixels)
    {
        return computeSampleSize(options, -1, maxNumOfPixels);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int computeSampleSize(final BitmapFactory.Options options, int minSideLength,
            final int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        }
        else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int computeInitialSampleSize(final BitmapFactory.Options options, final int minSideLength,
            final int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        }
        else if (minSideLength == -1)
        {
            return lowerBound;
        }
        else
        {
            return upperBound;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Coult not get package name: " + e);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void checkAndroidVersion(Context context)
    {
        int version = Build.VERSION.SDK_INT;
        if (version < 8)
        {
            throw new UnsupportedOperationException("Device must be at least " + "API Level 8 (instead of " + version
                    + ")");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isAndroidVersion(Context context)
    {
        int version = Build.VERSION.SDK_INT;
        if (version < 16)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Boolean isInternetAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isWifiAvailable(WifiManager wifiManager)
    {
        if (wifiManager != null)
        {
            return wifiManager.isWifiEnabled();
        }
        else
        {
            return false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getIP(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(Utility.LoginField.IP_DATA, DEFAULT_ZERO_VALUE);
        String ip = settings.getString(Utility.LoginField.SERVER_IP, HttpManager.IP);

        Log.d(TAG, "ip : " + ip);
        
        return ip;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setDefaultIP(Activity activity)
    {
        SharedPreferences settings = activity.getSharedPreferences(Utility.LoginField.IP_DATA, DEFAULT_ZERO_VALUE);
        String ipContent = settings.getString(Utility.LoginField.SERVER_IP, SPACE_STRING);
        if (ipContent.equals(SPACE_STRING))
        {
            Log.d(TAG, "To set default ip " + HttpManager.IP);
            settings.edit().putString(Utility.LoginField.SERVER_IP, HttpManager.IP).commit();
        }
        else
        {
            Log.d(TAG, "The ip had exist, no need to reset ");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setIP(Activity activity, String ip)
    {
        SharedPreferences settings = activity.getSharedPreferences(Utility.LoginField.IP_DATA, DEFAULT_ZERO_VALUE);
        String originalIP = settings.getString(Utility.LoginField.SERVER_IP, SPACE_STRING);
        Log.d(TAG, "originalIP : " + originalIP + " ip : " + ip);
        settings.edit().putString(Utility.LoginField.SERVER_IP, ip).commit();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setAlarmTime(Context context, int hour, int min, int requestCode)
    {
        Log.d(TAG, "setAlarmTime " + hour + ":" + min);
        Intent intent = new Intent(context, UploadReceiver.class);
        intent.setAction("upload");
        pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        long firstTime = SystemClock.elapsedRealtime(); // device working time (include sleep time)
        long systemTime = System.currentTimeMillis();

        String timezoneID = TimeZone.getDefault().getID();
        Log.d(TAG, "timezone ID:" + timezoneID);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone(timezoneID));

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // to set the time of every day
        long selectTime = calendar.getTimeInMillis();

        // if current time greater than setting time, then start from second day's setting time
        if (systemTime > selectTime)
        {
            Log.d(TAG, "current time greater than setting time, select time:");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }

        // The gap between current time and setting time.
        long time = selectTime - systemTime;
        firstTime += time;

        // To register alarm
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, ALARM_GAP, pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.d(TAG, "alarmManager selectTime:" + selectTime + " systemTime: " + systemTime + " firstTime: " + firstTime);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setAlarmTime(Context context)
    {   
        int time1 = getAlarmTime(context, AlarmTime.ALARM_TIME_1);
        int time2 = getAlarmTime(context, AlarmTime.ALARM_TIME_2);
        int time3 = getAlarmTime(context, AlarmTime.ALARM_TIME_3);
        
        if (time1 > 0)
        {
            setAlarmTime(context, time1, 0, 0);
            setAlarmTime(context, time2, 0, 1);
            setAlarmTime(context, time3, 0, 2);
        }
        else
        {
            for (int i = 0; i < 3; i++)
            {
                // am 8 : 00, pm 3  : 00, pm 10:00
                setAlarmTime(context, 8 + i * 7, 0, i);
            }
            
            saveAlarmTimeData(context, 8, 15, 22);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setAlarmTimeFromSettings(Context context, int time1, int time2, int time3)
    {   
        setAlarmTime(context, time1, 0, 0);
        setAlarmTime(context, time2, 0, 1);
        setAlarmTime(context, time3, 0, 2);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean hasAppRunning(Context context)
    {
        String packageName = "com.example.retailsale";

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

        if (tasksInfo.size() > 0)
        {    
            Log.d(TAG, "package name : " + tasksInfo.get(0).topActivity.getPackageName());

            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean[] convertListToArray(final List<Boolean> booleanList)
    {
        final boolean[] array = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList)
        {
            array[index++] = object;
        }
        return array;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] splitStringToArray(String data, String keyword)
    {
        String[] tokens = data.split(keyword);
        for (String token : tokens)
        {
            Log.d(TAG, "token === " + token);
        }
        
        return tokens;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] splitAddressToArray(String zipCode, String address)
    {
        String[] addressArray;
        
        if (zipCode.equals("290")) // 宜蘭縣 釣魚台列嶼
        {
            addressArray = splitAddressToArray(address, 3, 5);
        }
        else if (zipCode.equals("300") || zipCode.equals("400") || zipCode.equals("401")
                || zipCode.equals("402") || zipCode.equals("403") || zipCode.equals("404")
                || zipCode.equals("600") || zipCode.equals("701") || zipCode.equals("702")
                || zipCode.equals("704")) // 新竹市 東區 北區, 台中市 中區 東區 南區 西區 北區, 嘉義市 東區 西區, 台南市 東區 南區 北區
        {
            addressArray = splitAddressToArray(address, 3, 2);
        }
        else if (zipCode.equals("849") || zipCode.equals("963")) // 高雄市 那瑪夏區, 台東縣 太麻里鄉
        {
            addressArray = splitAddressToArray(address, 3, 4);
        }
        else if (zipCode.equals("817") || zipCode.equals("819")) // 南海諸島 東沙 南沙
        {
            addressArray = splitAddressToArray(address, 4, 2);
        }
        else
        {
            addressArray = splitAddressToArray(address, 3, 3);
        }
        
        return addressArray;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] splitAddressToArray(String address, int countyLength, int cityLength)
    {
        String[] addressArray = new String[3];
        
//        String address = "桃園縣八德市xxx村xx號";
//        
//        Log.d(TAG, "county === " + test.substring(0, 3));
//        Log.d(TAG, "city === " + test.substring(3, 6));
//        Log.d(TAG, "address === " + test.substring(6, test.length()));
        
        // county
        addressArray[0] = address.substring(0, countyLength);
        
        // city
        addressArray[1] = address.substring(countyLength, countyLength + cityLength);
        
        // address
        addressArray[2] = address.substring(countyLength + cityLength, address.length());
        
        return addressArray;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getPositionFromListByKeyword(List<String> data, String keyword)
    {
        for (int i = 0; i < data.size(); i++)
        {
            if (keyword.equals(data.get(i)))
            {
                return i;
            }
        }
        
        return 0;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getPositionFromListByOptSerial(List<DataOption> dataList, int optSerial)
    {
        int position = 0;
        
        for (int i = 0; i < dataList.size(); i++)
        {
            if (dataList.get(i).getOptSerial() == optSerial){
                position = i;
                return position;
            }
        }
        
        return position;
    }
    
    public static void unzip(Context context, String zipFilePath)
    {
        Log.d(TAG, "Unzip: " + zipFilePath);
        // 取得SD卡目錄，並以 App package 作為子目錄
//        File outDir = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
//        // 以下與一般 Java 程式作法無二
//        if (!outDir.exists())
//        {
//            // 要有 android.permission.WRITE_EXTERNAL_STORAGE 權限
//            outDir.mkdir();
//        }
//        
//        if (!outDir.exists())
//        {
//            Log.e(TAG, "Can't create this path: " + outDir.getAbsolutePath());
//            return;
//        }
//        Log.d(TAG, "Ouput path: " + outDir.getAbsolutePath());
        InputStream is = null;
        BufferedInputStream bi = null;
        BufferedOutputStream bo = null;
        try
        {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
            ZipEntry entry;
            File outFile;
            // loop zip 檔案裡所有目錄與檔案
            while (entryEnum.hasMoreElements())
            {
                entry = entryEnum.nextElement();
//                outFile = new File(outDir, entry.getName());
                outFile = new File("/sdcard", entry.getName());
                if (entry.isDirectory())
                {
                    Log.d(TAG, "Add a folder: " + outFile.getAbsolutePath());
                    outFile.mkdir();
                    if (!outFile.exists())
                    {
                        Log.e(TAG, "Can't create this path: " + outFile.getAbsolutePath());
                        return;
                    }
                }
                else
                {
                    Log.d(TAG, "Add a file: " + outFile.getAbsolutePath());
                    is = zipFile.getInputStream(entry);
                    bi = new BufferedInputStream(is);
                    bo = new BufferedOutputStream(new FileOutputStream(outFile));
                    int data = 0;
                    while ((data = bi.read()) != -1)
                    {
                        bo.write(data);
                    }
                    bo.flush();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bi != null)
            {
                try
                {
                    bi.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (bo != null)
            {
                try
                {
                    bo.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static boolean copyAssets(Context context) {
        // to check retail.zip
        File zipFile = new File(DEFAULT_PHOTO_ZIP);
        
        if (zipFile.exists())
        {
            Log.d(TAG, "the retail zip exist");
            unzip(context, DEFAULT_PHOTO_ZIP);
            return false;
        }
        
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
            return false;
        }
        
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open(filename);
              File outFile = new File(Environment.getExternalStorageDirectory(), filename);
              out = new FileOutputStream(outFile);
              copyFile(in, out);
            } catch(IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + filename, e);
            }     
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }  
        }
        
        unzip(context, DEFAULT_PHOTO_ZIP);
        
        return true;
    }
    
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    
    // for generate default base64 content
    public static void generateDefaultBase64File(File rootFile)
    {
        File[] list = rootFile.listFiles();

        for (File file : list) {
            if (file.isDirectory()) {
                Log.d(TAG, "Dir: " + file.getAbsoluteFile());
                if (!file.getName().contains(".thumb"))
                {
                    generateDefaultBase64File(file);
                }
            }
            else {
                Log.d(TAG, "File: " + file.getAbsoluteFile());
                
                StringBuilder sb = new StringBuilder();
                try
                {
                    // read file (reference from readFromSDcard)
                    FileInputStream fin = new FileInputStream(file);
                    byte[] data = new byte[fin.available()];
                    while (fin.read(data) != -1)
                    {
                        sb.append(new String(data));
                    }
                    fin.close();
                    
                    // write file in the begin
//                    // 1. get file path
//                    String path = filePath.replace(Utility.REPLACE_SERVER_FOLDER, Utility.FILE_PATH_2).replace("\\", "/");
//                    
//                    Log.d(TAG, "path  is  ~~~~~~~~~~~~~~~~~~~~ " + path);

//                    // 2. create the folder from file path
//                    Utility.createFolder(path.toString());

                    // 3. generate the MD5 string from file name
                    Log.d(TAG, "file.getName()  is  ~~~~~~~~~~~~~~~~~~~~ " + file.getName());
                    String md5FileName = Utility.generateMD5String(file.getName().replace(REPLACE_TXT_STRING, SPACE_STRING));
                    Log.d(TAG, "md5FileName  is  ~~~~~~~~~~~~~~~~~~~~ " + md5FileName);

                    // 4. To mix md5 file name and file data to "newData", then write data to file path(newFileName)
//                    StringBuilder newFileName = new StringBuilder().append(path.toString()).append("/").append(fileName)
//                            .append(REPLACE_TXT_STRING);

//                    Log.d(TAG, "newFileName  is  ~~~~~~~~~~~~~~~~~~~~ " + newFileName.toString());

//                    StringBuilder newData = new StringBuilder().append(md5FileName).append(fileStream);

//                    int status = writeFile(newFileName.toString(), newData.toString(), false);
                    
                    // 5. to generate thumbnail
                    generateThumbnail(file.getName().replace(".txt", ""), file.getAbsolutePath().replace(file.getName(), "") + Utility.THUMB_PATH, sb.toString());
                    
                    sb.insert(0, md5FileName);
                    
                    Log.d(TAG, "sb  is  ~~~~~~~~~~~~~~~~~~~~ " + sb.toString());
                    
                    // write file (reference from writeToSDcard)
                    FileOutputStream fout = new FileOutputStream(file);
                    fout.write(sb.toString().getBytes());
                    fout.flush();
                    fout.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                Log.d(TAG, "*************************************************************** ");
            }
        }
    }
}
