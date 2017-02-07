package com.jju.howe.howeassistant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ContactManager;
import com.jju.howe.howeassistant.R;

public class HelpActivity extends Activity {
    private LinearLayout ll1, ll2;
    private SpeechRecognizer mIat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);

        mIat = SpeechRecognizer.createRecognizer(HelpActivity.this, mInitListener);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactManager mgr = ContactManager.createManager(HelpActivity.this,
                        mContactListener);
                mgr.asyncQueryAllContactsName();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HelpActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage(R.string.cando);
                dialog.setPositiveButton("确定",null);
                dialog.show();
            }
        });

    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(HelpActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();

            }
        }
    };
    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                Toast.makeText(HelpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HelpActivity.this, getString(R.string.text_upload_success), Toast.LENGTH_SHORT).show();

            }
        }
    };

    int ret = 0; // 函数调用返回值

    /**
     * 获取联系人监听器。
     */
    private ContactManager.ContactListener mContactListener = new ContactManager.ContactListener() {

        @Override
        public void onContactQueryFinish(final String contactInfos, boolean changeFlag) {
            // 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
            // 每当联系人发生变化，该接口都将会被回调，可通过ContactManager.destroy()销毁对象，解除回调。
            // if(changeFlag) {
            // 指定引擎类型
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            ret = mIat.updateLexicon("contact", contactInfos, mLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                Toast.makeText(HelpActivity.this, "上传联系人失败：" + ret, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
