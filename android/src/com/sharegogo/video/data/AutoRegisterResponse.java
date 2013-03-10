package com.sharegogo.video.data;

import com.sharegogo.video.json.GsonParser;

/**
 * �Զ�ע��Ӧ��
 * @author Raymon
 *
 */
public class AutoRegisterResponse extends BaseResponse{
	
	static public AutoRegisterResponse fromJson(String data)
	{
		return GsonParser.fromJson(data, AutoRegisterResponse.class);
	}
	
	public String token;
}
