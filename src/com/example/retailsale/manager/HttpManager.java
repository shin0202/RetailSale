package com.example.retailsale.manager;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.retailsale.manager.addcustomer.AddCustomerListener;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GetDataOptionTypeListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.dataoption.GsonDataOptionType;
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.folderinfo.GetFolderInfoListener;
import com.example.retailsale.manager.folderinfo.GsonFolderInfo;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.util.Utility;
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
 
    	String loginUri = "http://192.168.49.128/KendoAPI/ODATA/userQuery(userAccount='" + userAccount + "',userPwd='" + userPwd + "')";
        Log.e(TAG, "login() loginUri = " + loginUri);
        
        GsonRequest<GsonLoginInfo> getDataOptionsGsonRequset = new GsonRequest<GsonLoginInfo>(Method.GET, loginUri,
                GsonLoginInfo.class, getLoginReqSuccessListener(loginListener),
                getLoginReqErrorListener(loginListener));
        getDataOptionsGsonRequset.setTag("login");

        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionsGsonRequset);
    }

    public void cancelLogin(Context context) {
        VolleySingleton.getInstance(context).getRequestQueue().cancelAll("login");
    }
    //////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// add customer
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
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// data option
    public void getDataOptions(Context context, GetDataOptionListener getDataOptionListener) {       
    	String dataOptionsUri = "http://192.168.49.128/KendoAPI/ODATA/dataOptionParm";
        Log.e(TAG, "getDataOptions() dataOptionsUri = " + dataOptionsUri);
        
        GsonRequest<GsonDataOption> getDataOptionsGsonRequset = new GsonRequest<GsonDataOption>(Method.GET, dataOptionsUri,
        		GsonDataOption.class, getDataOptionReqSuccessListener(getDataOptionListener),
        		getDataOptionReqErrorListener(getDataOptionListener));
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
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// file info
    public void getFileInfo(Context context, int pathId, int fileId, GetFileInfoListener getFileInfoListener) {
    	String fileInfoUri = "http://192.168.49.128/KendoAPI/ODATA/fileContent(pathId=" + pathId + ",fileId=" + fileId + ")";
        Log.e(TAG, "getFileInfo() fileInfoUri = " + fileInfoUri);

        GsonRequest<GsonFileInfo> getFileInfoGsonRequset = new GsonRequest<GsonFileInfo>(Method.GET, fileInfoUri,
                GsonFileInfo.class, getFileInfoReqSuccessListener(getFileInfoListener), getFileInfoReqErrorListener(getFileInfoListener));
        getFileInfoGsonRequset.setTag("getFileInfo");

        VolleySingleton.getInstance(context).getRequestQueue().add(getFileInfoGsonRequset);
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    //////////////////////////////////////////////////////////////////////////////// folder info
    public void getFolderInfo(Context context, GetFolderInfoListener getFolderInfoListener) {
    	String fileInfoUri = "http://192.168.49.128/KendoAPI/ODATA/folderInfo";
        Log.e(TAG, "getFolderInfo() fileInfoUri = " + fileInfoUri);

        GsonRequest<GsonFolderInfo> getFolderInfoGsonRequset = new GsonRequest<GsonFolderInfo>(Method.GET, fileInfoUri,
        		GsonFolderInfo.class, getFolderInfoReqSuccessListener(getFolderInfoListener), getFolderInfoReqErrorListener(getFolderInfoListener));
        getFolderInfoGsonRequset.setTag("getFolderInfo");

        VolleySingleton.getInstance(context).getRequestQueue().add(getFolderInfoGsonRequset);
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

    private Response.ErrorListener getDataOptionReqErrorListener(final GetDataOptionListener getDataOptionListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getDataOption error: " + error.toString());
                
                if (getDataOptionListener != null)
                    getDataOptionListener.onResult(false, null);
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
                Log.e(TAG, "getFileInfo success response: " + response.toString());
//                handleFileInfo(response);
                if (getFileInfoListener != null)
                    getFileInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getFileInfoReqErrorListener(final GetFileInfoListener getFileInfoListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getFileInfo error: " + error.toString());
                if (getFileInfoListener != null)
                    getFileInfoListener.onResult(false, null);
            }
        };
    }
    ////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////listener of folder info
    private Response.Listener<GsonFolderInfo> getFolderInfoReqSuccessListener(final GetFolderInfoListener getFolderInfoListener) {
        return new Response.Listener<GsonFolderInfo>() {
            @Override
            public void onResponse(GsonFolderInfo response) {
                Log.e(TAG, "getFolderInfo success response: " + response.toString());
                if (getFolderInfoListener != null)
                	getFolderInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getFolderInfoReqErrorListener(final GetFolderInfoListener getFolderInfoListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getFolderInfo error: " + error.toString());
                if (getFolderInfoListener != null)
                	getFolderInfoListener.onResult(false, null);
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
            StringBuilder path = new StringBuilder().append(Utility.FILE_PATH).append(fileInfo.getValue().get(0).getPath());
            
            // 2. create the folder from file path
            Utility.createFolder(path.toString());
            
            // 3. generate the MD5 string from file name
            String md5FileName = Utility.generateMD5String(fileInfo.getValue().get(0).getFileName());
            
            // 4. To mix md5 file name and file data to "newData", then write data to file path(newFileName)
            StringBuilder newFileName = new StringBuilder().append(path.toString()).append(fileInfo.getValue().get(0).getFileName())
                    .append(".txt");
            StringBuilder newData = new StringBuilder().append(md5FileName).append(fileInfo.getValue().get(0).getFileStream());
            
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
