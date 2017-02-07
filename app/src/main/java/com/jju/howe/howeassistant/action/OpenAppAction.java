package com.jju.howe.howeassistant.action;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;


import com.jju.howe.howeassistant.activity.MainActivity;

import java.util.List;


public class OpenAppAction {

	MainActivity mActivity;
	String mAppName;
	public OpenAppAction(String appname, MainActivity activity)
	{
		// Log.d("dd","here");
		mAppName = appname;
		mActivity=activity;
	}

	public void start()
	{
		if((mAppName!=null)&&(mAppName.length()!=0)){
			getAppByName();
		}
	}
	private void getAppByName(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		PackageManager pm = mActivity.getPackageManager();
		List<ResolveInfo> installAppList = pm.queryIntentActivities(intent, 0);
		for(ResolveInfo info :installAppList){
			String name = info.loadLabel(pm).toString();
			if(name.equalsIgnoreCase(mAppName)){
				String pkgname=info.activityInfo.packageName;
				if("com.android.contacts".equalsIgnoreCase(pkgname)){
					Uri uri = Uri.parse("content://contacts/people");
					Intent i= new Intent("android.intent.action.VIEW", uri);
					mActivity.startActivity(i);
				}else{
					intent = pm.getLaunchIntentForPackage(pkgname);
					intent.addCategory("android.intent.category.LAUNCHER");
					mActivity.startActivity(intent);
				}
				mActivity.speakAnswer("正在打开"+mAppName+"...");
				return ;
			}
		}
		mActivity.speakAnswer("没有找到你所说的应用哦^_^");
	}

}
