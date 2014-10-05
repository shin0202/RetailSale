package com.example.retailsale.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.retailsale.volly.toolbox.GsonListRequest;
import com.example.retailsale.volly.toolbox.GsonRequest;
import com.example.retailsale.volly.toolbox.StringXORer;
import com.example.retailsale.volly.toolbox.VolleySingleton;

public class HttpManager
{
	private final static String TAG = "HttpManager";
	private final static String HASH_CODE = "nu84x61w";
	private static String hash_time;
	private static String facebookID;
	private static String userName;
	private static String deviceIMEI;
	private static String hash_auth;
	private static String mobile;
	private static ArrayList<Boolean> isPostMailOkList = new ArrayList<Boolean>();
	private static int parserCount = 0;

	// LoginListener loginListener;
	// PostMailListener postMailListener;
	public HttpManager()
	{
	}

	public HttpManager(Context context)
	{
	}

	public void init(Context context, String facebookID)
	{
		// TelephonyManager tm = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// String phone = tm.getLine1Number();
		//
		// HttpManager.mobile = phone;
		//
		// Log.e(TAG, "HttpManager()  init() phone:" + phone);
		HttpManager.facebookID = facebookID;
		HttpManager.hash_time = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance()
				.getTime());
		HttpManager.hash_auth = StringXORer.encode(facebookID + "_" + hash_time, HASH_CODE);
		// if (tm.getDeviceId() != null)
		// {
		// HttpManager.deviceIMEI = tm.getDeviceId();
		// }
		// else
		// {
		// HttpManager.deviceIMEI = android.os.Build.SERIAL;
		// }
		Log.e(TAG, "init(): facebookID:" + facebookID + ",hash_time:" + hash_time + ",deviceIMEI:"
				+ deviceIMEI + ",hash_auth:" + hash_auth + ",mobile=" + mobile);
	}

    //////////////////////////////////////////////////////////////////////////////// Login related.
    public void login(Context context, UserInfo user, final LoginListener loginListener) {
        // this.loginListener = loginListeners;

        String uriLogin = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        HashMap<String, Object> paramsLoginUpdate = new HashMap<String, Object>();
        paramsLoginUpdate.put("auth", hash_auth);
        paramsLoginUpdate.put("user_account", (user.getUserAccount() != null) ? user.getUserAccount() : "");
        paramsLoginUpdate.put("user_name", (user.getUserName() != null) ? user.getUserName() : "");
        paramsLoginUpdate.put("user_mobile", (user.getUserMobile() != null) ? user.getUserMobile() : "");
        paramsLoginUpdate.put("user_key", (user.getUserKey() != null) ? user.getUserKey() : "");
        paramsLoginUpdate.put("user_group", user.getUserGroup());
        paramsLoginUpdate.put("user_type", user.getUserType());
        paramsLoginUpdate.put("last_time", (user.getLastTime() != null) ? user.getLastTime() : "");
        Log.e(TAG, "login() uriLogin = " + uriLogin);
        JsonObjectRequest req = new JsonObjectRequest(uriLogin, new JSONObject(paramsLoginUpdate),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "login()  Response:" + response.toString(4));
                            if (loginListener != null)
                                loginListener.onResult(true, response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (loginListener != null)
                                loginListener.onResult(false, e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "login()  Error: " + error.getMessage());
                        if (loginListener != null)
                            loginListener.onResult(false, error.getMessage());
                    }
                });
        req.setTag("login");
        VolleySingleton.getInstance(context).getRequestQueue().add(req);
    }

    public void cancelLogin(Context context) {
        VolleySingleton.getInstance(context).getRequestQueue().cancelAll("login");
    }

    public void getLoginHistory(Context context, GetLoginHistoryListener getLoginHistoryListener) {
        java.lang.reflect.Type typeGet = new com.google.gson.reflect.TypeToken<ArrayList<LoginHistory>>() {
        }.getType();

        String getLoginHistoryUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        Log.e(TAG, "getLoginHistoryUri:" + getLoginHistoryUri);
        GsonListRequest<ArrayList<LoginHistory>> getLoginHistoriesGsonRequset = new GsonListRequest<ArrayList<LoginHistory>>(
                Method.GET, getLoginHistoryUri, typeGet, getLoginHistoriesReqSuccessListener(getLoginHistoryListener),
                getLoginHistoriesReqErrorListener());
        getLoginHistoriesGsonRequset.setTag("getLoginHistory");
        VolleySingleton.getInstance(context).getRequestQueue().add(getLoginHistoriesGsonRequset);
    }

    //////////////////////////////////////////////////////////////////////////////////

    public void getTestInfo(Context context, final GetTestInfoListener getTestInfoListener) {
        java.lang.reflect.Type typeGet = new com.google.gson.reflect.TypeToken<ArrayList<TestInfo>>() {
        }.getType();
        String testInfoUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        Log.e(TAG, "testInfoUri:" + testInfoUri);
        GsonListRequest<ArrayList<TestInfo>> getTestInfoGsonRequset = new GsonListRequest<ArrayList<TestInfo>>(
                Method.GET, testInfoUri, typeGet, getTestInfoReqSuccessListener(getTestInfoListener),
                getTestInfoReqErrorListener());
        getTestInfoGsonRequset.setTag("getTestInfo");
        VolleySingleton.getInstance(context).getRequestQueue().add(getTestInfoGsonRequset);
    }

    public void addCustomerInfo(Context context, AddCustomerListener addCustomerListener,
            HashMap<String, String> paramsAddComsumerPost) {
        String addCustomerInfoUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");

        JsonObjectRequest addCustomerInfoRequset = new JsonObjectRequest(Method.POST, addCustomerInfoUri,
                new JSONObject(paramsAddComsumerPost), addCustomerReqSuccessListener(addCustomerListener),
                addCustomerReqErrorListener());
        VolleySingleton.getInstance(context).getRequestQueue().add(addCustomerInfoRequset);

//		GsonRequest addCustomerInfoRequset = new GsonRequest(Method.POST, addCustomerInfoUri, null, paramsAddComsumerPost, addCustomerReqSuccessListener(addCustomerListener), addCustomerReqErrorListener());
//		VolleySingleton.getInstance(context).getRequestQueue().add(addCustomerInfoRequset);
    }

    public void getServiceContent(Context context, GetServiceContentListener getServiceContentListener) {
        java.lang.reflect.Type typeGet = new com.google.gson.reflect.TypeToken<ArrayList<ServiceInfo>>() {
        }.getType();

        String serviceContentUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        Log.e(TAG, "serviceContentUri:" + serviceContentUri);
        GsonListRequest<ArrayList<ServiceInfo>> getServiceContentGsonRequset = new GsonListRequest<ArrayList<ServiceInfo>>(
                Method.GET, serviceContentUri, typeGet, getServiceContentReqSuccessListener(getServiceContentListener),
                getServiceContentReqErrorListener());
        getServiceContentGsonRequset.setTag("getServiceContent");
        VolleySingleton.getInstance(context).getRequestQueue().add(getServiceContentGsonRequset);
    }

    public void getDataOptions(Context context, GetDataOptionListener getDataOptionListener) {
        String dataOptionsUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
//		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, dataOptionsUri,
//				null, new Response.Listener<JSONObject>()
//				{
//					@Override
//					public void onResponse(JSONObject response)
//					{
//						Log.e(TAG, "response:" + response.toString());
//					}
//				}, new Response.ErrorListener()
//				{
//					@Override
//					public void onErrorResponse(VolleyError error)
//					{
//						Log.e(TAG, "onErrorResponse:" + error.toString());
//					}
//				});

        GsonRequest<DataOption> getDataOptionsGsonRequset = new GsonRequest<DataOption>(Method.GET, dataOptionsUri,
                DataOption.class, getDataOptionReqSuccessListener(getDataOptionListener),
                getDataOptionReqErrorListener());
        getDataOptionsGsonRequset.setTag("getDataOptions");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionsGsonRequset);
    }

    public void getDataOptionType(Context context, GetDataOptionTypeListener getDataOptionTypeListener) {
        String dataOptionTypeUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");

        GsonRequest<DataOptionType> getDataOptionTypeGsonRequset = new GsonRequest<DataOptionType>(Method.GET,
                dataOptionTypeUri, DataOptionType.class,
                getDataOptionTypeReqSuccessListener(getDataOptionTypeListener), getDataOptionTypeReqErrorListener());
        getDataOptionTypeGsonRequset.setTag("getDataOptionType");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionTypeGsonRequset);
    }

    //////////////////////////////////////////////////////////////////////////////// image related
    public ImageLoader getImageLoader(Context context) {
        return VolleySingleton.getInstance(context).getImageLoader();
    }

    public void getBitmapFromWeb(Context context, String url, ImageLoader.ImageListener imageListener) {
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        imageLoader.get(url, imageListener);
    }

    public void getFileInfo(Context context, GetFileInfoListener getFileInfoListener) {
        String dataOptionTypeUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");

        GsonRequest<FileInfo> getFileInfoGsonRequset = new GsonRequest<FileInfo>(Method.GET, dataOptionTypeUri,
                FileInfo.class, getFileInfoReqSuccessListener(getFileInfoListener), getFileInfoReqErrorListener());
        getFileInfoGsonRequset.setTag("getFileInfo");

        VolleySingleton.getInstance(context).getRequestQueue().add(getFileInfoGsonRequset);
    }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// listener of test info
    private Response.Listener<ArrayList<TestInfo>> getTestInfoReqSuccessListener(
            final GetTestInfoListener getTestInfoListener) {
        return new Response.Listener<ArrayList<TestInfo>>() {
            @Override
            public void onResponse(ArrayList<TestInfo> response) {
                Log.e(TAG, "getTestInfo response: " + response.toString());
                if (getTestInfoListener != null)
                    getTestInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getTestInfoReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getTestInfo error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of login history
    private Response.Listener<ArrayList<LoginHistory>> getLoginHistoriesReqSuccessListener(
            final GetLoginHistoryListener getLoginHistoryListener) {
        return new Response.Listener<ArrayList<LoginHistory>>() {
            @Override
            public void onResponse(ArrayList<LoginHistory> response) {
                Log.e(TAG, "getLoginHistories response: " + response.toString());
                if (getLoginHistoryListener != null)
                    getLoginHistoryListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getLoginHistoriesReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getLoginHistories error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of service content
    private Response.Listener<ArrayList<ServiceInfo>> getServiceContentReqSuccessListener(
            final GetServiceContentListener getServiceContentListener) {
        return new Response.Listener<ArrayList<ServiceInfo>>() {
            @Override
            public void onResponse(ArrayList<ServiceInfo> response) {
                Log.e(TAG, "getServiceContent response: " + response.toString());
                if (getServiceContentListener != null)
                    getServiceContentListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getServiceContentReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getServiceContent error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of data option
    private Response.Listener<DataOption> getDataOptionReqSuccessListener(
            final GetDataOptionListener getDataOptionListener) {
        return new Response.Listener<DataOption>() {
            @Override
            public void onResponse(DataOption response) {
                Log.e(TAG, "getDataOption response: " + response.toString());
                if (getDataOptionListener != null)
                    getDataOptionListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getDataOptionReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getDataOption error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of data option type
    private Response.Listener<DataOptionType> getDataOptionTypeReqSuccessListener(
            final GetDataOptionTypeListener getDataOptionTypeListener) {
        return new Response.Listener<DataOptionType>() {
            @Override
            public void onResponse(DataOptionType response) {
                Log.e(TAG, "getDataOptionType success response: " + response.toString());
                if (getDataOptionTypeListener != null)
                    getDataOptionTypeListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getDataOptionTypeReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getDataOptionType error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of file info
    private Response.Listener<FileInfo> getFileInfoReqSuccessListener(final GetFileInfoListener getFileInfoListener) {
        return new Response.Listener<FileInfo>() {
            @Override
            public void onResponse(FileInfo response) {
                Log.e(TAG, "getDataOptionType success response: " + response.toString());
                handleFileInfo(response);
                if (getFileInfoListener != null)
                    getFileInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getFileInfoReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getDataOptionType error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// listener of add customer
    private Response.Listener<JSONObject> addCustomerReqSuccessListener(final AddCustomerListener addCustomerListener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "addCustomer response: " + response.toString());
                if (addCustomerListener != null)
                    addCustomerListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener addCustomerReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "addCustomer error: " + error.toString());
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    private void handleFileInfo(FileInfo fileInfo) {        
        if (fileInfo != null) {
            // 1. get file path
            StringBuilder path = new StringBuilder().append(Environment.getExternalStorageDirectory().getPath())
                    .append(fileInfo.getFilePath());
            
            // 2. create the folder from file path
            createFolder(Environment.getExternalStorageDirectory().getPath() + fileInfo.getFilePath());
            
            // 3. generate the MD5 string from file name
            String md5FileName = generateMD5String(fileInfo.getFileName());
            
            // 4. To mix md5 file name and file data, then write data to file path
            StringBuilder newFileName = new StringBuilder().append(path.toString()).append(fileInfo.getFileName())
                    .append(".txt");
            StringBuilder newData = new StringBuilder().append(md5FileName).append(fileInfo.getFileData());
            
            writeFile(newFileName.toString(), newData.toString());
            
            final File folder = new File(path.toString());
            listFilesForFolder(folder);
            
            // 5. to read file and remove md5
            String readContent = readFile(newFileName.toString());
            Log.d(TAG, "readContent is " + readContent);
            String realData = readContent.replace(md5FileName, "");
            Log.d(TAG, "realData is " + realData);
            
            // 6. decode Base64 to byte[]
            String encodeString = encodeBase64(realData);
            decodeBase64(encodeString);
        }
    }
    
    private void writeFile(String path, String data) {
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
    
    private String readFile(String path) {
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
    
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            Log.d(TAG, "the name is " + fileEntry.getName());
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
//                System.out.println(fileEntry.getName());
//            }
        }
    }
    
    private void createFolder(String path) {
        if (path != null) {
            File directories = new File(path);
            boolean result = directories.mkdirs();
            
            Log.d(TAG, "directories is not exist and created ? " + result);
        }
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
    
    public byte[] decodeBase64(String content) {
        byte[] data = Base64.decode(content, Base64.DEFAULT);
        try {
            String text = new String(data, "UTF-8");
            Log.d(TAG, "decode string is " + text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public String encodeBase64(String content) {
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
}
