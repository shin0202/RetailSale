package com.example.retailsale.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Utility
{
	private static final String TAG = "Utility";
	private static final int BOUNDS = 10;
	private static final String FILL_ZERO = "0";
	private static final String FILL_SLASH = "/";
//	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/retailSale/";
	public static final String FILE_PATH = "/sdcard/retailSale/";
	public static final int SHOW_WAITING_DIALOG = 999;
	public static final int DISMISS_WAITING_DIALOG = -999;
	
	public class JSONTag 
	{
		public static final String CONTENT_TYPE = "Content-type";
		public static final String FATCA_INFO = "FATCA-INFO";
	}
	
	public class HeaderContent 
	{
		public static final String CONTENT_TYPE = "application/json;charset=utf-8";
		public static final String FATCA_INFO_OPERATION = "{\"LogType\":\"Operation\",\"UserNo\":\"095050\",\"UserName\":\"\",\"UserHostAddress\":\"127.0.0.1\",\"ActionName\":\"http://fatcaweb/FATCA/FATCA/\"}";
		public static final String FATCA_INFO_LOGIN = "{\"LogType\":\"Login\",\"UserNo\":\"095050\",\"UserName\":\"\",\"UserHostAddress\":\"127.0.0.1\",\"ActionName\":\"http://fatcaweb/FATCA/FATCA/\"}";
	}
	
	public class LoginField
	{
	    public static final String DATA = "data";
	    public static final String ID = "id";
	    public static final String PASSWORD = "password";
	    public static final String USER_SERIAL = "user_serial";
	    public static final String USER_GROUP = "user_group";
	    public static final String LOGIN_KEY = "login_key";
	}
	
	// for home, company
	public static boolean isPhoneValid(String email) {
		boolean isValid = false;

		String expression = "[0-9]{2,3}-[0-9]{6,8}";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	// for cellphone
	public static boolean isCellphoneValid(String email) {
		boolean isValid = false;

		String expression = "[0-9]{4}-[0-9]{6}";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	 public static boolean isBirthdayValid(String inDate) {

		    if (inDate == null)
		      return false;

		    //set the format to use as a constructor argument
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		    if (inDate.trim().length() != dateFormat.toPattern().length())
		      return false;

		    dateFormat.setLenient(false);

		    try {
		      //parse the inDate parameter
		      dateFormat.parse(inDate.trim());
		    }
		    catch (ParseException pe) {
		      return false;
		    }
		    return true;
		  }
	
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	public static String covertDateToString(int year, int month, int day) {
		Log.d(TAG, "year: " + year + " month: " + month + " day: " + day);
		StringBuilder dateTimeString = new StringBuilder();
		
		dateTimeString.append(year).append(FILL_SLASH);
		
		if (month < BOUNDS) {
			dateTimeString.append(FILL_ZERO);
		}
		dateTimeString.append(month).append(FILL_SLASH);
		
		if (day < BOUNDS) {
			dateTimeString.append(FILL_ZERO);
		}
		dateTimeString.append(day).append(" ");
		
		return dateTimeString.toString();
	}
	
	public static String covertTimeToString(int hour, int minute) {
		Log.d(TAG, " hour: " + hour + " minute: " + minute);
		StringBuilder timeString = new StringBuilder();
		
		if (hour < BOUNDS) {
			timeString.append(FILL_ZERO);
		}
		timeString.append(hour).append(":");
		
		if (minute < BOUNDS) {
			timeString.append(FILL_ZERO);
		}
		timeString.append(minute);
		
		return timeString.toString();
	}
	
	public static String getCurrentDateTime() {
	    String dateTime = "";
	    
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    dateTime = dateFormat.format(date).toString();
	    
	    return dateTime;
	}
	
    public static void createFolder(String path) {
        if (path != null) {
            File directories = new File(path);
            boolean result = directories.mkdirs();
            
            Log.d(TAG, "directories is not exist and created ? " + result);
        }
    }
	
    public static String readFile(String path) {
        BufferedReader reader;
        StringBuilder readContent = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    readContent.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return readContent.toString();
    }
    
    public static void writeFile(String path, String data) {
        try {
            File fakeFile = new File(path);
            if (!fakeFile.exists()) {
                fakeFile.createNewFile();
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(fakeFile));
            output.write(data);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static byte[] decodeBase64(String content) {
        byte[] data = Base64.decode(content, Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            Log.d(TAG, "decode string is " + text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public static String encodeBase64(String content) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, "encode string is " + base64);
        
        return base64;
    }
    
    public static String generateMD5String(String source) {
        // test string
//        String testString = "catch12";
//        String resultString = "";
//        
//        resultString = testMD52(testString);
//        Log.d(TAG, "resultString === " + resultString);
        
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(source.getBytes("UTF-8"));
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public static Bitmap covertByteArrayToBitmap(byte[] data) 
	{
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
        
        return bm;
	}
	
	public static String getFactaInfoHeader(String logType, String userNo, String userName,
			String userHostAddress, String actionName)
	{
		// "{\"LogType\":\"Operation\",\"UserNo\":\"095050\",\"UserName\":\"\",\"UserHostAddress\":\"127.0.0.1\",\"ActionName\":\"http://fatcaweb/FATCA/FATCA/\"}"
		StringBuilder header = new StringBuilder().append("{\"LogType\":\"").append(logType)
				.append("\",\"UserNo\":\"").append(userNo).append("\",\"UserName\":\"")
				.append(userName).append("\",\"UserHostAddress\":\"").append(userHostAddress).append("\",\"ActionName\":\"").append(actionName).append("\"}");
		
		return header.toString();
	}
	
	public static void saveData(Activity activity, String id, String password, String userSerial, String userGroup, String loginKey)
	{
		SharedPreferences settings = activity.getSharedPreferences(Utility.LoginField.DATA, 0);
		settings.edit()
		        .putString(Utility.LoginField.ID, id)
				.putString(Utility.LoginField.PASSWORD, password)
				.putString(Utility.LoginField.USER_SERIAL, userSerial)
				.putString(Utility.LoginField.USER_GROUP, userGroup)
				.putString(Utility.LoginField.LOGIN_KEY, loginKey)
				.commit();
	}
	
	public static boolean hadLogin(Activity actiivty) {
		SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.DATA, 0);
        String id = settings.getString(Utility.LoginField.ID, "");
        
        if (id != null && !id.equals(""))
        {
        	return true;
        }
        else
        {
        	return false;
        }
	}
}
