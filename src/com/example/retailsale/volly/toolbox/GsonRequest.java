/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.retailsale.volly.toolbox;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T>
{
	private Gson gson;
	private Class<T> clazz;
	private final Listener<T> listener;
	private Map<String, String> params;
	private Type type;

	public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.clazz = clazz;
		this.listener = listener;
		gson = new Gson();
	}

	public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener, Gson gson)
	{
		super(method, url, errorListener);
		this.clazz = clazz;
		this.listener = listener;
		this.gson = gson;
	}
	
	public GsonRequest(int method, String url, Type type, Map<String, String> params,
			Listener<T> listener, ErrorListener errorListener)
	{
		super(method, url, errorListener);
		this.params = params;
		this.type = type;
		this.listener = listener;
	}

	@Override
	protected void deliverResponse(T response)
	{
		if (listener != null) listener.onResponse(response);
	}
	
	@Override
	public Map<String, String> getParams() throws AuthFailureError
	{
		return params;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
//			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			response.headers.put(HTTP.CONTENT_TYPE, "text/plain; charset=utf-8");
			String json = new String(response.data, "utf-8");
			json = URLDecoder.decode(URLDecoder.decode(json));
			
			return clazz != null ? Response.success(gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response)) : (Response<T>) Response.success(
							gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
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