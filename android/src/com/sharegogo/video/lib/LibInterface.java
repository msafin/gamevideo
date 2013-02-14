package com.sharegogo.video.lib;

import android.content.Context;

public class LibInterface {
	
	static
	{
		System.loadLibrary("sharegogo");
	}
	
	native public void init(Context context);
}
