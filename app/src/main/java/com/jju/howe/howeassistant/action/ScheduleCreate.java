package com.jju.howe.howeassistant.action;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;

import com.jju.howe.howeassistant.activity.MainActivity;


public class ScheduleCreate {
	private String mName,mTime,mDate,mContent;
	private MainActivity mActivity;
	public ScheduleCreate(String name, String time, String date, String content, MainActivity activity){
		mName=name;
		mTime=time;
		mDate=date;
		mContent=content;
		mActivity=activity;
	}

	public void start(){
		switch(mName){
			case "clock":{//设置闹钟提醒
				setClock();
				break;
			}
			case "reminder":{//设置日历提醒
				setCalendar();
				break;
			}
			default:break;
		}
	}

	private void setClock(){
		Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);


		mActivity.startActivity(alarmas);
	/*	  AlarmManager aManager;
		  Calendar currentTime=Calendar.getInstance();
		     获取闹钟管理的实例
		  aManager = (AlarmManager)mActivity.getSystemService(mActivity.ALARM_SERVICE);
           设置闹钟 */
		//aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}
	@SuppressLint("NewApi")
	private void setCalendar(){
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(CalendarContract.Events.CONTENT_URI);
		mActivity.startActivity(intent);
	}
}
