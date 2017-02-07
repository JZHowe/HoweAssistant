package com.jju.howe.howeassistant.action;

import android.content.Intent;

import com.jju.howe.howeassistant.activity.MainActivity;

public class CallView {

    private MainActivity mActivity;

    public CallView(MainActivity activity) {
        mActivity = activity;
    }

    public void start() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL_BUTTON);
        mActivity.startActivity(intent);
    }
}
