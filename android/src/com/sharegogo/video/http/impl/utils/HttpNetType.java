package  com.sharegogo.video.http.impl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sharegogo.video.http.HttpServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * 网络模块的当前网络类型
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpNetType  extends BroadcastReceiver {
	
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
	// Private Property
	private static final int TYPE_COMMON_WAP = 0x01;
	private static final int TYPE_COMMON_NET = 0x02;
	private static final int TYPE_COMMON_WIFI = 0x04;
	

	
	private static List<onNetChangedListener> mChangedListenter = 
			new ArrayList<onNetChangedListener>();

	public static int getNetType() {
		return getNetType(HttpServer.getContext());
	}
	
	
	/**
	 * 获取当前联网类型
	 * 
	 * @param act
	 *            当前活动Activity
	 * @return 联网类型 -1表示未知的联网类型, 正确类型： MPROXYTYPE_WIFI | MPROXYTYPE_CMWAP |
	 *         MPROXYTYPE_CMNET
	 */
	public static int getNetType(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo info = cm.getActiveNetworkInfo();
			if(info == null){
				return TYPE_DEFAULT;
			}
			
			String typeName =info.getTypeName();
			if ("wifi".equalsIgnoreCase(typeName)){ // wifi网络
				return TYPE_WIFI;
			}
			
			String extraInfo = info.getExtraInfo();
			if(extraInfo == null){
				return TYPE_DEFAULT;
			}
			extraInfo = extraInfo.trim().toLowerCase();
			if ("cmnet".equals(extraInfo)) {
				return TYPE_CMNET;
			} else if ("cmwap".equals(extraInfo)) {
				return TYPE_CMWAP;
			} else if ("uninet".equals(extraInfo)) {
				return TYPE_UNINET;
			} else if ("uniwap".equals(extraInfo)) {
				return TYPE_UNIWAP;
			} else if ("ctnet".equals(extraInfo)) {
				return TYPE_CTNET;
			} else if ("ctwap".equals(extraInfo)) {
				return TYPE_CTWAP;
			} else if ("3gnet".equals(extraInfo)) {
				return TYPE_3GNET;
			} else if ("3gwap".equals(extraInfo)) {
				return TYPE_3GWAP;
			} else if (extraInfo.contains("net")) {
				return TYPE_NET;
			} else if (extraInfo.contains("wap")) {
				return TYPE_WAP;
			}
			// For emulator
			else if ("epc.tmobile.com".equals(extraInfo)) {
				return TYPE_CMNET;
			} else {
				return TYPE_DEFAULT;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缺省返回CMNET类型
		return -1;
	}	
	
	/**
	 * 返回网络连接是否可用
	 */
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * 判断是否使用wifi接入
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi() {
		return getNetType(HttpServer.getContext()) == TYPE_WIFI;
	}

	/**
	 * 判断是否使用wifi接入
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		return getNetType(context) == TYPE_WIFI;
	}

	/**
	 * 判断是否是wap网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWap() {
		int currNetWorkType = getNetType(HttpServer.getContext());
		return changeNetType(currNetWorkType) == TYPE_COMMON_WAP;
	}

	/**
	 * 判断是否是net类型
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNet() {
		int currNetWorkType = getNetType(HttpServer.getContext());
		return changeNetType(currNetWorkType) == TYPE_COMMON_NET;
	}	
	
	
	/**
	 * 转换成通用网络类型
	 * 
	 * @param networkType
	 *            Specific network type.
	 */
	private static int changeNetType(int networkType) {
		switch (networkType) {
		case TYPE_CMWAP:
		case TYPE_UNIWAP:
		case TYPE_CTWAP:
		case TYPE_3GWAP:
		case TYPE_WAP:
			return TYPE_COMMON_WAP;
		case TYPE_CMNET:
		case TYPE_UNINET:
		case TYPE_CTNET:
		case TYPE_3GNET:
		case TYPE_NET:
			return TYPE_COMMON_NET;
		case TYPE_WIFI:
		case TYPE_DEFAULT:
		default:
			return TYPE_COMMON_WIFI;
		}
	}


	/**
	 * 广播接收网络状态改变
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
        	for(onNetChangedListener listener:mChangedListenter){
        		if(listener != null){
        			Log.e("zhaodsh", "net changed");
        			listener.onNetChanged();
        		}
        	}
        }
		
	}
	
	public static void setOnNetChangedListener(onNetChangedListener listener){
		mChangedListenter.add(listener);
	}
	
	public static void removeOnNetChangedListener(onNetChangedListener listener){
		mChangedListenter.remove(listener);
	}	
	
	public interface onNetChangedListener{
		
		public void onNetChanged();
	}
	
	
}