package com.sharegogo.video.http;

import com.sharegogo.video.SharegogoVideoApplication;

import android.widget.Toast;

public class BasicResponseHandler implements ResponseHandler{

	@Override
	public void onSuccess(Object data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFailed(int what, Object msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onPersistent(Object data) {
		// TODO Auto-generated method stub
		return true;
	}

}
