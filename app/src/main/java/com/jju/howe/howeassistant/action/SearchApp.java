package com.jju.howe.howeassistant.action;

import android.content.Intent;
import android.net.Uri;

import com.jju.howe.howeassistant.activity.MainActivity;


public class SearchApp {
	private String mName;
	MainActivity mActivity;

	public SearchApp(String name, MainActivity activity){
		mName=name;
		mActivity=activity;
	}

	public void start(){
		mActivity.speakAnswer("正在搜索...");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q="+mName));
		mActivity.startActivity(intent);
	}
}
