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
import com.example.retailsale.manager.addcustomer.AddCustomerListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.dataoption.GsonDataOptionType;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GetDataOptionTypeListener;
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.login.GetLoginHistoryListener;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginHistory;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.manager.service.GetServiceContentListener;
import com.example.retailsale.manager.service.GsonServiceInfo;
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
        
        GsonRequest<GsonLoginInfo> getDataOptionsGsonRequset = new GsonRequest<GsonLoginInfo>(Method.GET, uriLogin,
                GsonLoginInfo.class, getLoginReqSuccessListener(loginListener),
                getLoginReqErrorListener(loginListener));
        getDataOptionsGsonRequset.setTag("login");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionsGsonRequset);
    }

    public void cancelLogin(Context context) {
        VolleySingleton.getInstance(context).getRequestQueue().cancelAll("login");
    }

    public void getLoginHistory(Context context, GetLoginHistoryListener getLoginHistoryListener) {
        java.lang.reflect.Type typeGet = new com.google.gson.reflect.TypeToken<ArrayList<GsonLoginHistory>>() {
        }.getType();

        String getLoginHistoryUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        Log.e(TAG, "getLoginHistoryUri:" + getLoginHistoryUri);
        GsonListRequest<ArrayList<GsonLoginHistory>> getLoginHistoriesGsonRequset = new GsonListRequest<ArrayList<GsonLoginHistory>>(
                Method.GET, getLoginHistoryUri, typeGet, getLoginHistoriesReqSuccessListener(getLoginHistoryListener),
                getLoginHistoriesReqErrorListener(getLoginHistoryListener));
        getLoginHistoriesGsonRequset.setTag("getLoginHistory");
        VolleySingleton.getInstance(context).getRequestQueue().add(getLoginHistoriesGsonRequset);
    }

    //////////////////////////////////////////////////////////////////////////////////

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
        java.lang.reflect.Type typeGet = new com.google.gson.reflect.TypeToken<ArrayList<GsonServiceInfo>>() {
        }.getType();

        String serviceContentUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
        Log.e(TAG, "serviceContentUri:" + serviceContentUri);
        GsonListRequest<ArrayList<GsonServiceInfo>> getServiceContentGsonRequset = new GsonListRequest<ArrayList<GsonServiceInfo>>(
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

        GsonRequest<GsonDataOption> getDataOptionsGsonRequset = new GsonRequest<GsonDataOption>(Method.GET, dataOptionsUri,
                GsonDataOption.class, getDataOptionReqSuccessListener(getDataOptionListener),
                getDataOptionReqErrorListener());
        getDataOptionsGsonRequset.setTag("getDataOptions");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionsGsonRequset);
    }

    public void getDataOptionType(Context context, GetDataOptionTypeListener getDataOptionTypeListener) {
        String dataOptionTypeUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");

        GsonRequest<GsonDataOptionType> getDataOptionTypeGsonRequset = new GsonRequest<GsonDataOptionType>(Method.GET,
                dataOptionTypeUri, GsonDataOptionType.class,
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

        GsonRequest<GsonFileInfo> getFileInfoGsonRequset = new GsonRequest<GsonFileInfo>(Method.GET, dataOptionTypeUri,
                GsonFileInfo.class, getFileInfoReqSuccessListener(getFileInfoListener), getFileInfoReqErrorListener());
        getFileInfoGsonRequset.setTag("getFileInfo");

        VolleySingleton.getInstance(context).getRequestQueue().add(getFileInfoGsonRequset);
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of login history
    private Response.Listener<ArrayList<GsonLoginHistory>> getLoginHistoriesReqSuccessListener(
            final GetLoginHistoryListener getLoginHistoryListener) {
        return new Response.Listener<ArrayList<GsonLoginHistory>>() {
            @Override
            public void onResponse(ArrayList<GsonLoginHistory> response) {
                Log.e(TAG, "getLoginHistories response: " + response.toString());
                if (getLoginHistoryListener != null)
                    getLoginHistoryListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getLoginHistoriesReqErrorListener(final GetLoginHistoryListener getLoginHistoryListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getLoginHistories error: " + error.toString());
                if (getLoginHistoryListener != null)
                    getLoginHistoryListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of service content
    private Response.Listener<ArrayList<GsonServiceInfo>> getServiceContentReqSuccessListener(
            final GetServiceContentListener getServiceContentListener) {
        return new Response.Listener<ArrayList<GsonServiceInfo>>() {
            @Override
            public void onResponse(ArrayList<GsonServiceInfo> response) {
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
    private Response.Listener<GsonDataOption> getDataOptionReqSuccessListener(
            final GetDataOptionListener getDataOptionListener) {
        return new Response.Listener<GsonDataOption>() {
            @Override
            public void onResponse(GsonDataOption response) {
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
    private Response.Listener<GsonLoginInfo> getLoginReqSuccessListener(
            final GetLoginListener getLoginListener) {
        return new Response.Listener<GsonLoginInfo>() {
            @Override
            public void onResponse(GsonLoginInfo response) {
                Log.e(TAG, "getLogin response: " + response.toString());
                if (getLoginListener != null)
                	getLoginListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getLoginReqErrorListener(final GetLoginListener getLoginListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getLogin error: " + error.toString());
				if (getLoginListener != null)
                	getLoginListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of data option type
    private Response.Listener<GsonDataOptionType> getDataOptionTypeReqSuccessListener(
            final GetDataOptionTypeListener getDataOptionTypeListener) {
        return new Response.Listener<GsonDataOptionType>() {
            @Override
            public void onResponse(GsonDataOptionType response) {
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
    private Response.Listener<GsonFileInfo> getFileInfoReqSuccessListener(final GetFileInfoListener getFileInfoListener) {
        return new Response.Listener<GsonFileInfo>() {
            @Override
            public void onResponse(GsonFileInfo response) {
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

    private void handleFileInfo(GsonFileInfo fileInfo) {        
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
