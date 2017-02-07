package com.jju.howe.howeassistant.action;


import com.jju.howe.howeassistant.activity.MainActivity;

public class OpenQA {

	private String mText;
	MainActivity mActivity;
	
	public OpenQA(String text, MainActivity activity){
		mText=text;
		mActivity=activity;
	}
	
	public void start(){
		mActivity.speakAnswer(mText);
	}
	
}
