package com.example.retailsale.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.util.Log;

public class Utility
{
	private static final String TAG = "Utility";
	private static final int BOUNDS = 10;
	private static final String FILL_ZERO = "0";
	private static final String FILL_SLASH = "/";
	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "retailSale/";
	
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
}
