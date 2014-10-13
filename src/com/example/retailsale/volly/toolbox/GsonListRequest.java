package com.example.retailsale.volly.toolbox;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonListRequest<T> extends Request<T>
{
	private final Gson gson = new Gson();
	private Class<T> classOfT;
	private Type type;
	private final Response.Listener<T> listener;
	private Map<String, String> params;

	public GsonListRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
			ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.classOfT = clazz;
		this.listener = listener;
	}

	public GsonListRequest(int method, String url, Type type, Response.Listener<T> listener,
			ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.type = type;
		this.listener = listener;
	}

	public GsonListRequest(int method, String url, Type type, Map<String, String> params,
			Response.Listener<T> listener, Response.ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.params = params;
		this.type = type;
		this.listener = listener;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			// Log.e("GsonListRequest", "response.headers.get(\"content-type\")"
			// +response.headers.get("content-type"));
			// String json = new String(response.data,
			// HttpHeaderParser.parseCharset(response.headers));
			response.headers.put(HTTP.CONTENT_TYPE, "text/plain; charset=utf-8");
			String json = new String(response.data, "utf-8");
			json = URLDecoder.decode(URLDecoder.decode(json));
			//
			// Log.d("GsonListRequest", "decode json:" + json);
			// Log.d("GsonListRequest", "json:" +json);
			return classOfT != null ? Response.success(gson.fromJson(json, classOfT),
					HttpHeaderParser.parseCacheHeaders(response)) : (Response<T>) Response.success(
							gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e("GsonListRequest", "UnsupportedEncodingException" + e.toString());
			return Response.error(new ParseError(e));
		}
		catch (JsonSyntaxException e)
		{
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public Map<String, String> getParams() throws AuthFailureError
	{
		return params;
	}

	@Override
	protected void deliverResponse(T response)
	{
		if (listener != null) listener.onResponse(response);
	}
}