package com.example.retailsale.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.example.retailsale.fragment.SynchronizationFragment;
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
    public static final String REPLACE_SERVER_FOLDER = "C:\\Project\\_code\\testFolder";
    public static final String REPLACE_STRING = ".jpg.txt";
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
        public static final String DATA = "data";
        public static final String ID = "id";
        public static final String PASSWORD = "password";
        public static final String USER_SERIAL = "user_serial";
        public static final String USER_GROUP = "user_group";
        public static final String LOGIN_KEY = "login_key";
    }

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

    // for cellphone
    public static boolean isCellphoneValid(String number)
    {
        boolean isValid = false;

		String expression = "[0-9]{4}-[0-9]{6}";
//        String expression = "[0-9]{10}";
        CharSequence inputStr = number;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }

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

    public static String getCurrentDateTime()
    {
        String dateTime = Utility.SPACE_STRING;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateTime = dateFormat.format(date).toString();

        return dateTime;
    }
    
    public static int getCurrentYear()
    {
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);      // The current year as an int
        return year;
    }
    
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
    
    public static String covertDateStringToServer(String date)
    {
        return date.replace(" ", DATE_STRING);
    }

    public static void createFolder(String path)
    {
        if (path != null)
        {
            File directories = new File(path);
            boolean result = directories.mkdirs();

            Log.d(TAG, "directories is not exist and created ? " + result);
        }
    }

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

    public static int writeFile(String path, String data)
    {
        try
        {
            File fakeFile = new File(path);
            if (!fakeFile.exists())
            {
                fakeFile.createNewFile();
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(fakeFile));
            output.write(data);
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

    public static String encodeBase64(Bitmap bm, int quality)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos); // quality default is 100
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d(TAG, " imageEncoded is " + imageEncoded);

        return imageEncoded;
    }

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

    public static Bitmap covertByteArrayToBitmap(byte[] data)
    {
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

        return bm;
    }

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

    public static void saveData(Activity activity, String id, String password, int userSerial, int userGroup,
            String loginKey)
    {
        SharedPreferences settings = activity.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        settings.edit().putString(Utility.LoginField.ID, id).putString(Utility.LoginField.PASSWORD, password)
                .putInt(Utility.LoginField.USER_SERIAL, userSerial).putInt(Utility.LoginField.USER_GROUP, userGroup)
                .putString(Utility.LoginField.LOGIN_KEY, loginKey).commit();
    }

    public static boolean hadLogin(Activity actiivty)
    {
        SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
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

    public static int getCreator(Activity activity)
    {
        SharedPreferences settings = activity.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        int creator = settings.getInt(Utility.LoginField.USER_SERIAL, -1);

        Log.d(TAG, "creator is " + creator);

        return creator;
    }

    public static int getCreatorGroup(Activity actiivty)
    {
        SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        int creatorGroup = settings.getInt(Utility.LoginField.USER_GROUP, -1);

        Log.d(TAG, "creator group is " + creatorGroup);

        return creatorGroup;
    }
    
    public static String getLoginKey(Activity actiivty)
    {
        SharedPreferences settings = actiivty.getSharedPreferences(Utility.LoginField.DATA, Utility.DEFAULT_ZERO_VALUE);
        String loginKey = settings.getString(Utility.LoginField.LOGIN_KEY, "");

        Log.d(TAG, "loginKey is " + loginKey);

        return loginKey;
    }

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
    
    ////////////////////////////////////////////////////////////////////////////////

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

            int status = Utility.writeFile(newFileName.toString(), newData.toString());
            
            // 5. to generate thumbnail
            generateThumbnail(fileName, path + "/" + Utility.THUMB_PATH, fileStream);

            Message msg = new Message();
            msg.what = SynchronizationFragment.SelectedItem.DOWNLOAD_PICTURE;
            msg.obj = fileName;
            msg.arg1 = status;

            handler.sendMessage(msg);
        }
    }
    
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
            
            Utility.writeFile(newFileName.toString(), baseThumbnail.toString());
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
}
