package com.example.retailsale.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONStringer;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.retailsale.RetialSaleDbAdapter;
import com.example.retailsale.fragment.SynchronizationFragment;
import com.example.retailsale.manager.addcustomer.AddCustomerListener;
import com.example.retailsale.manager.dataoption.GetDataOptionListener;
import com.example.retailsale.manager.dataoption.GsonDataOption;
import com.example.retailsale.manager.fileinfo.GetFileInfoListener;
import com.example.retailsale.manager.fileinfo.GsonFileInfo;
import com.example.retailsale.manager.folderinfo.GetFolderInfoListener;
import com.example.retailsale.manager.folderinfo.GsonFolderInfo;
import com.example.retailsale.manager.login.GetLoginListener;
import com.example.retailsale.manager.login.GsonLoginInfo;
import com.example.retailsale.manager.userlist.GetUsetListByGroupListener;
import com.example.retailsale.manager.userlist.GsonUserByGroup;
import com.example.retailsale.util.Utility;
import com.example.retailsale.volly.toolbox.GsonRequest;
import com.example.retailsale.volly.toolbox.VolleySingleton;

public class HttpManager
{
    private static final String TAG = "HttpManager";

    public static final String USER_HOST = "127.0.0.1";

    public static final String ACTION_NAME = "http://fatcaweb/FATCA/FATCA/";
    
//    public static final String IP = "192.168.49.128";
    
    public static final String IP = "182.155.124.205"; // server

    public class LogType
    {
        public static final String LOGIN = "Login";
        public static final String OPERATION = "Operation";
    }

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
    public void login(Context context, String userAccount, String userPwd, final GetLoginListener loginListener)
    {

        String loginUri = "http://" + IP + "/KendoAPI/odata/userQuery(userAccount='" + userAccount + "',userPwd='"
                + userPwd + "')";
        Log.e(TAG, "login() loginUri = " + loginUri);

        GsonRequest<GsonLoginInfo> getLoginGsonRequset = new GsonRequest<GsonLoginInfo>(Method.GET, loginUri,
                GsonLoginInfo.class, getLoginReqSuccessListener(loginListener),
                getLoginReqErrorListener(loginListener), LogType.LOGIN, "095050", Utility.SPACE_STRING, USER_HOST,
                ACTION_NAME, Utility.SPACE_STRING);
        getLoginGsonRequset.setTag("login");

        VolleySingleton.getInstance(context).getRequestQueue().add(getLoginGsonRequset);
    }

    public void cancelLogin(Context context)
    {
        VolleySingleton.getInstance(context).getRequestQueue().cancelAll("login");
    }

    //////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// add customer
    public void addCustomerInfo(Context context, AddCustomerListener addCustomerListener,
            Map<String, String> paramsAddComsumerPost)
    {
//        String addCustomerInfoUri = "http://192.168.49.128/KendoAPI/odata/customerData";

//		Map<String, String> param = new HashMap<String, String>();
//		
//		param.put("customerAccount", "C20142000180054700");
//		param.put("custometName", "Shin");
//		param.put("customerMobile", "無資料"); 0912345678
//		param.put("customerHome", "2727-8831"); 02-12345678
//		param.put("customerCompany", "2727-8831");
//		param.put("customerSex", "8");
//		param.put("customerTitle", "10");
//		param.put("customerMail", "johnwang@hotmail.com");
//		param.put("customerVisitDate", "2014-10-08T16:00:00");
//		param.put("customerInfo", "13");
//		
//		param.put("customerIntroducer", "john");
//		param.put("customerJob", "15");
//		param.put("customerAge", null);
//		param.put("customerBirth", "2014-10-15");
//		param.put("creator", "23");
//		param.put("creatorGroup", "23");
//		param.put("createTime", "2014-10-07T10:19:32");

//        JsonObjectRequest addCustomerInfoRequset = new JsonObjectRequest(Method.POST, addCustomerInfoUri,
//                new JSONObject(paramsAddComsumerPost), addCustomerReqSuccessListener(addCustomerListener),
//                addCustomerReqErrorListener(addCustomerListener));
//        VolleySingleton.getInstance(context).getRequestQueue().add(addCustomerInfoRequset);

//		GsonRequest addCustomerInfoRequset = new GsonRequest(Method.POST, addCustomerInfoUri, null, paramsAddComsumerPost, addCustomerReqSuccessListener(addCustomerListener), addCustomerReqErrorListener());
//		VolleySingleton.getInstance(context).getRequestQueue().add(addCustomerInfoRequset);

//		GsonJSONRequest addCustomerInfoRequset = new GsonJSONRequest(Method.POST,
//				addCustomerInfoUri, paramsAddComsumerPost,
//				addCustomerReqSuccessListener(addCustomerListener),
//				addCustomerReqErrorListener(addCustomerListener), LogType.Login, "095050", Utility.SPACE_STRING,
//				USER_HOST, ACTION_NAME);
//		VolleySingleton.getInstance(context).getRequestQueue().add(addCustomerInfoRequset);
    }

    public void addCustomerInfo(Activity activity, String custometName, Handler handler, String logType, String userNo,
            String userName, String userHostAddress, String actionName, String loginKey, JSONStringer json, long rowId,
            RetialSaleDbAdapter retialSaleDbAdapter)
    {
        String addCustomerInfoUri = "http://" + IP + "/KendoAPI/odata/customerData_Mobile";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(addCustomerInfoUri);
        httppost.setHeader(Utility.JSONTag.CONTENT_TYPE, Utility.HeaderContent.CONTENT_TYPE);
        httppost.setHeader(Utility.JSONTag.FATCA_INFO,
                Utility.getFactaInfoHeader(logType, userNo, userName, userHostAddress, actionName, loginKey));
        
        Log.d(TAG, "json == " + json.toString());

        Message msg = new Message();
        msg.what = SynchronizationFragment.SelectedItem.UPLOAD_CUSTOMER;
        msg.obj = custometName;
        try
        {
            // Add your data
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentType("application/json");
            httppost.setEntity(stringEntity);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if (response != null)
            {
                msg.arg1 = response.getStatusLine().getStatusCode();

                Log.d(TAG, "response === " + response.getStatusLine().toString());
                retialSaleDbAdapter.updateCustomer(rowId, RetialSaleDbAdapter.UPLOAD);
            }
            else
            {
                msg.arg1 = Utility.FAILED_UPLOAD;
                Log.d(TAG, "response is null");
            }
            httpclient.getConnectionManager().shutdown();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
            msg.arg1 = Utility.FAILED_UPLOAD;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            msg.arg1 = Utility.FAILED_UPLOAD;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            msg.arg1 = Utility.FAILED_UPLOAD;
        }
        handler.sendMessage(msg);
    }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// data option
    public void getDataOptions(Activity activity, GetDataOptionListener getDataOptionListener)
    {
        String dataOptionsUri = "http://" + IP + "/KendoAPI/odata/dataOptionParm";
        Log.e(TAG, "getDataOptions() dataOptionsUri = " + dataOptionsUri);

        GsonRequest<GsonDataOption> getDataOptionsGsonRequset = new GsonRequest<GsonDataOption>(Method.GET,
                dataOptionsUri, GsonDataOption.class, getDataOptionReqSuccessListener(getDataOptionListener),
                getDataOptionReqErrorListener(getDataOptionListener), LogType.OPERATION, String.valueOf(Utility
                        .getCreator(activity)), Utility.SPACE_STRING, USER_HOST, ACTION_NAME,
                Utility.getLoginKey(activity));

        getDataOptionsGsonRequset.setTag("getDataOptions");

        VolleySingleton.getInstance(activity).getRequestQueue().add(getDataOptionsGsonRequset);
    }

//    public void getDataOptionType(Context context, GetDataOptionTypeListener getDataOptionTypeListener) {
//        String dataOptionTypeUri = String.format("http://www.cpami.gov.tw/opendata/fd2_json.php");
//
//        GsonRequest<GsonDataOptionType> getDataOptionTypeGsonRequset = new GsonRequest<GsonDataOptionType>(Method.GET,
//                dataOptionTypeUri, GsonDataOptionType.class,
//                getDataOptionTypeReqSuccessListener(getDataOptionTypeListener), getDataOptionTypeReqErrorListener());
//        getDataOptionTypeGsonRequset.setTag("getDataOptionType");
//
//        VolleySingleton.getInstance(context).getRequestQueue().add(getDataOptionTypeGsonRequset);
//    }
    ////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////get user list by group
    public void getUserListByGroup(Activity activity, GetUsetListByGroupListener getUsetListByGroupListener, int group)
    {
//        String getUserListUri = "http://" + IP + "/KendoAPI/odata/usp_Qry_UserbyGroup?$filter=userGroup eq " + group;
        String getUserListUri = "http://" + IP + "/KendoAPI/odata/usp_Qry_UserbyGroup?userGroup=" + group;
        Log.e(TAG, "getUserListByGroup() getUserListUri = " + getUserListUri);

        GsonRequest<GsonUserByGroup> getUserListByGroupGsonRequset = new GsonRequest<GsonUserByGroup>(Method.GET, getUserListUri,
                GsonUserByGroup.class, getUsetListByGroupReqSuccessListener(getUsetListByGroupListener),
                getUsetListByGroupReqErrorListener(getUsetListByGroupListener), LogType.OPERATION, String.valueOf(Utility
                        .getCreator(activity)), Utility.SPACE_STRING, USER_HOST, ACTION_NAME,
                Utility.getLoginKey(activity));
        getUserListByGroupGsonRequset.setTag("getUserListByGroup");

        VolleySingleton.getInstance(activity).getRequestQueue().add(getUserListByGroupGsonRequset);
    }
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// file info
    public void getFileInfo(Activity activity, int pathId, int fileId, GetFileInfoListener getFileInfoListener,
            Handler handler)
    {
        String fileInfoUri = "http://" + IP + "/KendoAPI/odata/fileContent(pathId=" + pathId + ",fileId=" + fileId
                + ")";
        Log.e(TAG, "getFileInfo() fileInfoUri = " + fileInfoUri);

        GsonRequest<GsonFileInfo> getFileInfoGsonRequset = new GsonRequest<GsonFileInfo>(Method.GET, fileInfoUri,
                GsonFileInfo.class, getFileInfoReqSuccessListener(getFileInfoListener, handler),
                getFileInfoReqErrorListener(getFileInfoListener), LogType.OPERATION, String.valueOf(Utility
                        .getCreator(activity)), Utility.SPACE_STRING, USER_HOST, ACTION_NAME,
                Utility.getLoginKey(activity));

        getFileInfoGsonRequset.setTag("getFileInfo");

        VolleySingleton.getInstance(activity).getRequestQueue().add(getFileInfoGsonRequset);
    }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// folder info
    public void getFolderInfo(Activity activity, GetFolderInfoListener getFolderInfoListener)
    {
        String fileInfoUri = "http://" + IP + "/KendoAPI/odata/folderInfo";
        Log.e(TAG, "getFolderInfo() fileInfoUri = " + fileInfoUri);

        GsonRequest<GsonFolderInfo> getFolderInfoGsonRequset = new GsonRequest<GsonFolderInfo>(Method.GET, fileInfoUri,
                GsonFolderInfo.class, getFolderInfoReqSuccessListener(getFolderInfoListener),
                getFolderInfoReqErrorListener(getFolderInfoListener), LogType.OPERATION, String.valueOf(Utility
                        .getCreator(activity)), Utility.SPACE_STRING, USER_HOST, ACTION_NAME,
                Utility.getLoginKey(activity));
        getFolderInfoGsonRequset.setTag("getFolderInfo");

        VolleySingleton.getInstance(activity).getRequestQueue().add(getFolderInfoGsonRequset);
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of data option
    private Response.Listener<GsonDataOption> getDataOptionReqSuccessListener(
            final GetDataOptionListener getDataOptionListener)
    {
        return new Response.Listener<GsonDataOption>()
        {
            @Override
            public void onResponse(GsonDataOption response)
            {
                Log.e(TAG, "getDataOption response: " + response.toString());
                if (getDataOptionListener != null) getDataOptionListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getDataOptionReqErrorListener(final GetDataOptionListener getDataOptionListener)
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "getDataOption error: " + error.toString());

                if (getDataOptionListener != null) getDataOptionListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////listener of get user list
    private Response.Listener<GsonUserByGroup> getUsetListByGroupReqSuccessListener(final GetUsetListByGroupListener getUsetListByGroupListener)
    {
        return new Response.Listener<GsonUserByGroup>()
        {
            @Override
            public void onResponse(GsonUserByGroup response)
            {
                Log.e(TAG, "getLogin response: " + response.toString());
                if (getUsetListByGroupListener != null) getUsetListByGroupListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getUsetListByGroupReqErrorListener(final GetUsetListByGroupListener getUsetListByGroupListener)
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "getLogin error: " + error.toString());
                if (getUsetListByGroupListener != null) getUsetListByGroupListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of login
    private Response.Listener<GsonLoginInfo> getLoginReqSuccessListener(final GetLoginListener getLoginListener)
    {
        return new Response.Listener<GsonLoginInfo>()
        {
            @Override
            public void onResponse(GsonLoginInfo response)
            {
                Log.e(TAG, "getLogin response: " + response.toString());
                if (getLoginListener != null) getLoginListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getLoginReqErrorListener(final GetLoginListener getLoginListener)
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "getLogin error: " + error.toString());
                if (getLoginListener != null) getLoginListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of data option type
//    private Response.Listener<GsonDataOptionType> getDataOptionTypeReqSuccessListener(
//            final GetDataOptionTypeListener getDataOptionTypeListener) {
//        return new Response.Listener<GsonDataOptionType>() {
//            @Override
//            public void onResponse(GsonDataOptionType response) {
//                Log.e(TAG, "getDataOptionType success response: " + response.toString());
//                if (getDataOptionTypeListener != null)
//                    getDataOptionTypeListener.onResult(true, response);
//            }
//        };
//    }
//
//    private Response.ErrorListener getDataOptionTypeReqErrorListener() {
//        return new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "getDataOptionType error: " + error.toString());
//            }
//        };
//    }
    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of file info
    private Response.Listener<GsonFileInfo> getFileInfoReqSuccessListener(
            final GetFileInfoListener getFileInfoListener, final Handler handler)
    {
        return new Response.Listener<GsonFileInfo>()
        {
            @Override
            public void onResponse(GsonFileInfo response)
            {
                Log.e(TAG, "getFileInfo success response: " + response.toString());
                Utility.handleFileInfo(response, handler);
                if (getFileInfoListener != null) getFileInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getFileInfoReqErrorListener(final GetFileInfoListener getFileInfoListener)
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "getFileInfo error: " + error.toString());
                if (getFileInfoListener != null) getFileInfoListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////listener of folder info
    private Response.Listener<GsonFolderInfo> getFolderInfoReqSuccessListener(
            final GetFolderInfoListener getFolderInfoListener)
    {
        return new Response.Listener<GsonFolderInfo>()
        {
            @Override
            public void onResponse(GsonFolderInfo response)
            {
                Log.e(TAG, "getFolderInfo success response: " + response.toString());
                if (getFolderInfoListener != null) getFolderInfoListener.onResult(true, response);
            }
        };
    }

    private Response.ErrorListener getFolderInfoReqErrorListener(final GetFolderInfoListener getFolderInfoListener)
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "getFolderInfo error: " + error.toString());
                if (getFolderInfoListener != null) getFolderInfoListener.onResult(false, null);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////// listener of add customer
//    private Response.Listener<JSONObject> addCustomerReqSuccessListener(final AddCustomerListener addCustomerListener) {
//        return new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e(TAG, "addCustomer response: " + response.toString());
//                if (addCustomerListener != null)
//                    addCustomerListener.onResult(true, response);
//            }
//        };
//    }
//
//    private Response.ErrorListener addCustomerReqErrorListener(final AddCustomerListener addCustomerListener) {
//        return new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "addCustomer error: " + error.toString());
//                if (addCustomerListener != null) try
//				{
//					addCustomerListener.onResult(false, new JSONObject().put("message", "Error"));
//				}
//				catch (JSONException e)
//				{
//					e.printStackTrace();
//					addCustomerListener.onResult(false, null);
//				}
//            }
//        };
//    }
}
