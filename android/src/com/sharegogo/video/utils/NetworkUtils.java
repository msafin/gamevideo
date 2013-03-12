package com.sharegogo.video.utils;

import com.sharegogo.video.SharegogoVideoApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关辅助函数
 * @author Raymon
 *
 */
public class NetworkUtils {
	// CMNET
	public static final int TYPE_CMNET = 0x001;
	// CMWAP
	public static final int TYPE_CMWAP = 0x002;
	// UNINET
	public static final int TYPE_UNINET = 0x004;
	// UNIWAP
	public static final int TYPE_UNIWAP = 0x008;
	// CTNET
	public static final int TYPE_CTNET = 0x010;
	// CTWAP
	public static final int TYPE_CTWAP = 0x020;
	// 3GNET
	public static final int TYPE_3GNET = 0x040;
	// 3GWAP
	public static final int TYPE_3GWAP = 0x080;
	// net类
	public static final int TYPE_NET = 0x100;
	// wap类
	public static final int TYPE_WAP = 0x200;
	// WIFI
	public static final int TYPE_WIFI = 0x400;
	// 默认
	public static final int TYPE_DEFAULT = 0x800;
	
	/**
	 * 是否已经联网
	 * @return
	 */
	static public boolean isNetworkAvailable()
	{
		Context context = SharegogoVideoApplication.getApplication();
		
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
			{
				for (int i = 0; i < info.length; i++) 
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 获取当前网络类型
	 * @return
	 */
	static public int getNetType() 
	{
		try 
		{
			Context context = SharegogoVideoApplication.getApplication();
			ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo info = cm.getActiveNetworkInfo();
			if(info == null)
			{
				return TYPE_DEFAULT;
			}
			
			String typeName = info.getTypeName();
			if ("wifi".equalsIgnoreCase(typeName))
			{
				return TYPE_WIFI;
			}
			
			String extraInfo = info.getExtraInfo();
			if(extraInfo == null)
			{
				return TYPE_DEFAULT;
			}
			
			extraInfo = extraInfo.trim().toLowerCase();
			if ("cmnet".equals(extraInfo)) 
			{
				return TYPE_CMNET;
			} 
			else if ("cmwap".equals(extraInfo)) 
			{
				return TYPE_CMWAP;
			} 
			else if ("uninet".equals(extraInfo)) 
			{
				return TYPE_UNINET;
			} 
			else if ("uniwap".equals(extraInfo)) 
			{
				return TYPE_UNIWAP;
			} 
			else if ("ctnet".equals(extraInfo)) 
			{
				return TYPE_CTNET;
			} 
			else if ("ctwap".equals(extraInfo)) 
			{
				return TYPE_CTWAP;
			} 
			else if ("3gnet".equals(extraInfo)) 
			{
				return TYPE_3GNET;
				
			} 
			else if ("3gwap".equals(extraInfo)) 
			{
				return TYPE_3GWAP;
				
			} 
			else if (extraInfo.contains("net")) 
			{
				return TYPE_NET;
				
			} 
			else if (extraInfo.contains("wap")) 
			{
				return TYPE_WAP;
			}
			// For emulator
			else if ("epc.tmobile.com".equals(extraInfo)) 
			{
				return TYPE_CMNET;
			} 
			else 
			{
				return TYPE_DEFAULT;
			}
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return TYPE_CMNET;
	}
	
	/**
	 * 判断是否使用wifi接入
	 * 
	 * @param context
	 * @return
	 */
	static public boolean isWifi() 
	{
		return getNetType() == TYPE_WIFI;
	}
	
	/**
	 * 判断是否是wap网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWap() 
	{
		final int currNetWorkType = getNetType();
		switch(currNetWorkType)
		{
			case TYPE_CMWAP:
			case TYPE_UNIWAP:
			case TYPE_CTWAP:
			case TYPE_3GWAP:
			case TYPE_WAP:
				return true;
			default:
				break;
		}
		
		return false;
	}
	
	/**
	 * 判断是否是net类型
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNet() 
	{
		final int currNetWorkType = getNetType();

		switch(currNetWorkType)
		{
			case TYPE_CMNET:
			case TYPE_UNINET:
			case TYPE_CTNET:
			case TYPE_3GNET:
			case TYPE_NET:
				return true;
			default:
				break;
		}
		
		return false;
	}	

}
