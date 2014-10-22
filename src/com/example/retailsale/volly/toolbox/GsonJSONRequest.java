package com.example.retailsale.volly.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.retailsale.util.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonJSONRequest extends Request<JSONObject>
{
	private static final String TAG = "GsonJSONRequest";
	private Gson gson;
	private final Listener<JSONObject> listener;
	private Map<String, String> params;
	private Map<String, String> header;

	public GsonJSONRequest(int method, String url, Map<String, String> params,
			Listener<JSONObject> listener, ErrorListener errorListener, String logType, String userNo, String userName, String userHostAddress, String actionName)
	{
		super(method, url, errorListener);
		
        header = new HashMap<String, String>();
        params = new HashMap<String, String>();

        header.put(Utility.JSONTag.CONTENT_TYPE, Utility.HeaderContent.CONTENT_TYPE);
        header.put(Utility.JSONTag.FATCA_INFO,
                Utility.getFactaInfoHeader(logType, userNo, userName, userHostAddress, actionName));
		
		this.params = params;
		this.listener = listener;
	}

	@Override
	protected void deliverResponse(JSONObject response)
	{
		if (listener != null) listener.onResponse(response);
	}
	
	@Override
	public Map<String, String> getParams() throws AuthFailureError
	{
		return params;
	}
	
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }
    
	@Override
	public String getBodyContentType()
	{
		return "{" +
        		"\n\"customerAccount\": \"C20141006180054700\"," +
        		"\n\"custometName\": \"Test\"," +
        		"\n\"customerMobile\": \"µL¸ê®Æ\"," +
        		"\n\"customerHome\": \"2727-8831\"," +
        		"\n\"customerCompany\": \"2727-8831\"," +
        		"\n\"customerSex\": 8," +
        		"\n\"customerTitle\": 10," +
        		"\n\"customerMail\": \"john@hotmail.com\"," +
        		"\n\"customerVisitDate\": \"2014-10-08T16:00:00\"," +
        		"\n\"customerInfo\": 13," +
        		"\n\"customerIntroducer\": \"john\"," +
        		"\n\"customerJob\": 15," +
        		"\n\"customerAge\": 10," +
        		"\n\"customerBirth\": \"2014-10-15\"," +
        		"\n\"creator\": 23," +
        		"\n\"creatorGroup\": 23," +
        		"\n\"createTime\": \"2014-10-07T10:19:32\"" +
        		"\n}";
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	{
		try
		{	
			String json = new String(response.data, "utf-8");
			Log.d(TAG, "json === " + json);
			
			return Response.success(gson.fromJson(json, JSONObject.class),
					HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch (JsonSyntaxException e)
		{
			return Response.error(new ParseError(e));
		}
	}
}
