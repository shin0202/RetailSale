package com.example.retailsale.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.retailsale.util.Utility;
import com.example.retailsale.volly.toolbox.GsonListRequest;
import com.example.retailsale.volly.toolbox.GsonRequest;
import com.example.retailsale.volly.toolbox.VolleySingleton;

public class HttpManager
{
	private final static String TAG = "HttpManager";

	// PostMailListener postMailListener;
	public HttpManager()
	{
	}

	public HttpManager(Context context)
	{
	}

	public void init(Context context, String facebookID)
	{
		
	}

    //////////////////////////////////////////////////////////////////////////////// Login related.
    public void login(Context context, String userAccount, String userPwd, final GetLoginListener loginListener) {
 
    	String uriLogin = "http://192.168.49.128/KendoAPI/ODATA/userQuery(userAccount='" + userAccount + "',userPwd='" + userPwd + "')";
        Log.e(TAG, "login() uriLogin = " + uriLogin);
        
        GsonRequest<UserInfo> getDataOptionsGsonRequset = new GsonRequest<UserInfo>(Method.GET, uriLogin,
                UserInfo.class, getLoginReqSuccessListener(loginListener),
                getLoginReqErrorListener());
        getDataOptionsGsonRequset.setTag("login");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionsGsonRequset);
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

        GsonRequest<WebFileInfo> getFileInfoGsonRequset = new GsonRequest<WebFileInfo>(Method.GET, dataOptionTypeUri,
                WebFileInfo.class, getFileInfoReqSuccessListener(getFileInfoListener), getFileInfoReqErrorListener());
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
    
    ////////////////////////////////////////////////////////////////////////////////listener of login
    private Response.Listener<UserInfo> getLoginReqSuccessListener(
            final GetLoginListener getLoginListener) {
        return new Response.Listener<UserInfo>() {
            @Override
            public void onResponse(UserInfo response) {
                Log.e(TAG, "getLogin response: " + response.toString());
                if (getLoginListener != null)
                	getLoginListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getLoginReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getLogin error: " + error.toString());
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
    private Response.Listener<WebFileInfo> getFileInfoReqSuccessListener(final GetFileInfoListener getFileInfoListener) {
        return new Response.Listener<WebFileInfo>() {
            @Override
            public void onResponse(WebFileInfo response) {
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

    private void handleFileInfo(WebFileInfo fileInfo) {        
        if (fileInfo != null) {
            // 1. get file path
            StringBuilder path = new StringBuilder().append(Utility.FILE_PATH).append(fileInfo.getFilePath());
            
            // 2. create the folder from file path
            Utility.createFolder(path.toString());
            
            // 3. generate the MD5 string from file name
            String md5FileName = Utility.generateMD5String(fileInfo.getFileName());
            
            // 4. To mix md5 file name and file data to "newData", then write data to file path(newFileName)
            StringBuilder newFileName = new StringBuilder().append(path.toString()).append(fileInfo.getFileName())
                    .append(".txt");
            StringBuilder newData = new StringBuilder().append(md5FileName).append(fileInfo.getFileData());
            
            Utility.writeFile(newFileName.toString(), newData.toString());
       
//            // 5. to read file and remove md5
//            String readContent = readFile(newFileName.toString());
//            Log.d(TAG, "readContent is " + readContent);
//            String realData = readContent.replace(md5FileName, "");
//            Log.d(TAG, "realData is " + realData);
//            
//            // 6. decode Base64 to byte[]
//            String encodeString = encodeBase64(realData);
//            decodeBase64(encodeString);
        }
    }
}
