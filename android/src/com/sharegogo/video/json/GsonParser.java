package com.sharegogo.video.json;

import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

/**
 * 对google json做简单封装
 * @author weizhengqin
 * @date 2012-10-18
 * @version 1.0
 */
public class GsonParser {
	
	public static <T> T fromJson(String data,Class<? extends T> cls)
	{
		Gson gson = new Gson();
		
		/*
		 * 防止异常MalformedJsonException 
		 */
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		
		T t =  null;
		try
		{
			t = gson.fromJson(reader, cls);
		}
		catch(JsonSyntaxException e)
		{
			e.printStackTrace();
		}
		catch(JsonIOException e)
		{
			e.printStackTrace();
		}
		return t;
	}
	
	
}
