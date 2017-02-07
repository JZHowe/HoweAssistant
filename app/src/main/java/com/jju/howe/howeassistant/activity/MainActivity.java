package com.jju.howe.howeassistant.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.sunflower.FlowerCollector;
import com.jju.howe.howeassistant.R;
import com.jju.howe.howeassistant.action.CallAction;
import com.jju.howe.howeassistant.action.CallView;
import com.jju.howe.howeassistant.action.MessageView;
import com.jju.howe.howeassistant.action.OpenAppAction;
import com.jju.howe.howeassistant.action.OpenQA;
import com.jju.howe.howeassistant.action.ScheduleCreate;
import com.jju.howe.howeassistant.action.ScheduleView;
import com.jju.howe.howeassistant.action.SearchAction;
import com.jju.howe.howeassistant.action.SearchApp;
import com.jju.howe.howeassistant.action.SearchWeather;
import com.jju.howe.howeassistant.action.SendMessage;
import com.jju.howe.howeassistant.bean.AnswerBean;
import com.jju.howe.howeassistant.bean.DataBean;
import com.jju.howe.howeassistant.bean.DatetimeBean;
import com.jju.howe.howeassistant.bean.MainBean;
import com.jju.howe.howeassistant.bean.ResultBean;
import com.jju.howe.howeassistant.bean.SlotsBean;
import com.jju.howe.howeassistant.util.JsonParser;

import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private MainBean mMainBean;
    private SpeechUnderstander mSpeechUnderstander;
    private Toast mToast;
    private TextView mAskText, mUnderstanderText;
    private HeartProgressBar heartProgressBar;
    private FiveLine mFiveLine;

    public static boolean service_flag = false;//表示是否在一项服务中
    public static String SRResult = "";    //识别结果

    // 语音合成对象
    private SpeechSynthesizer mTts;


    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58057ac8");

        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(MainActivity.this, mSpeechUdrInitListener);

        mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);

        mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this, mTtsInitListener);
        initLayout();
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        mAskText = (TextView) findViewById(R.id.tv_ask);
        mUnderstanderText = (TextView) findViewById(R.id.tv_answer);
        heartProgressBar = (HeartProgressBar) findViewById(R.id.progressBar);
        mFiveLine = (FiveLine) findViewById(R.id.fiveLine);

        mUnderstanderText.setText("我能帮您做什么吗?");
        speakAnswer("我能帮您做什么吗?");

        findViewById(R.id.start_understander).setOnClickListener(MainActivity.this);
    }

    int ret = 0;// 函数调用返回值

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 开始语音理解
            case R.id.start_understander:
                mFiveLine.setVisibility(View.INVISIBLE);
                heartProgressBar.start();
                mTts.stopSpeaking();

                if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
                    mSpeechUnderstander.stopUnderstanding();
                    //showTip("停止录音");
                }
                ret = mSpeechUnderstander.startUnderstanding(mSpeechUnderstanderListener);
                if (ret != 0) {
                    showTip("语义理解失败,错误码:" + ret);
                } else {
                    showTip("请开始说话…");
                }

                break;

        }
    }

    /**
     * 帮助
     */
    public void help(View view) {
        startActivity(new Intent(MainActivity.this, HelpActivity.class));
    }

    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener mSpeechUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };


    /**
     * 语义理解回调。
     */
    private SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                Log.d(TAG, result.getResultString());

                // 显示
                String text = result.getResultString();
                Log.e(TAG, text);
                mMainBean = JsonParser.parseIatResult(text);


                if (!TextUtils.isEmpty(text)) {
                    mAskText.setText(mMainBean.getText());
                    if (mMainBean.getRc() == 0) {
                        SRResult = mMainBean.getText();
                        judgeService();
                    } else {
                        mUnderstanderText.setText("我听不懂您说什么，亲爱的，下次可能我就明白了");
                        speakAnswer("我听不懂您说什么，亲爱的，下次可能我就明白了");
                    }
                }
            } else {
                showTip("识别结果不正确。");
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, data.length + "");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //showTip("结束说话");
            heartProgressBar.dismiss();
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            //mPercentForBuffering = percent;
            //showTip(String.format(getString(R.string.tts_toast_format),
            //      mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            //mPercentForPlaying = percent;
            //showTip(String.format(getString(R.string.tts_toast_format),
            //       mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //showTip("播放完成");
                mFiveLine.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    public void speakAnswer(String text) {
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(MainActivity.this, "tts_play");

        int code = mTts.startSpeaking(text, mTtsListener);
        mFiveLine.setVisibility(View.VISIBLE);
        mUnderstanderText.setText(text);
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //未安装则跳转到提示安装页面
                showTip("请安装语记!");
            } else {
                showTip("语音合成失败,错误码: " + code);
            }
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    //语义场景判断
    private void judgeService() {

        SRResult = null;
        String service = mMainBean.getService();
        String operation = mMainBean.getOperation();

        AnswerBean answerBean = new AnswerBean();
        SlotsBean slotsBean = new SlotsBean();
        DatetimeBean datetimeBean = new DatetimeBean();
        ResultBean resultBean = new ResultBean();
        DataBean dataBean = new DataBean();

        String date = "该天";

        if (mMainBean.getAnswer() != null) {
            answerBean = mMainBean.getAnswer();
        }


        if (mMainBean.getSemantic() != null) {
            if (mMainBean.getSemantic().getSlots() != null) {
                slotsBean = mMainBean.getSemantic().getSlots();
                if (mMainBean.getSemantic().getSlots().getDatetime() != null) {
                    datetimeBean = mMainBean.getSemantic().getSlots().getDatetime();
                }
            }
        }


        if (mMainBean.getData() != null) {
            if (mMainBean.getData().getResult() != null) {
                if (mMainBean.getSemantic().getSlots().getDatetime() != null) {
                    Calendar calendar = Calendar.getInstance();
                    int today = calendar.get(Calendar.DAY_OF_MONTH);
                    dataBean = mMainBean.getData();
                    String day = datetimeBean.getDate().substring(datetimeBean.getDate().length() - 2, datetimeBean.getDate().length());
                    if (day.equals("AY")) {
                        day = today + "";
                    }
                    int getday = Integer.parseInt(day);
                    int sub = getday - today;
                    resultBean = dataBean.getResult().get(sub);

                    if (sub == 0) {
                        date = "今天";
                    } else if (sub == 1) {
                        date = "明天";
                    } else if (sub == 2) {
                        date = "后天";
                    } else if (sub == 3) {
                        date = "大后天";
                    } else if (sub == 4) {
                        date = "四天后";
                    } else if (sub == 5) {
                        date = "五天后";
                    } else if (sub == 6) {
                        date = "六天后";
                    }
                }
            }

        }

        if (service_flag == false) {//如果不在一项服务中才进行服务的判断


            switch (service) {

                case "telephone":
                    switch (operation) {
                        case "CALL": {    //1打电话
                            //必要条件【电话号码code】
                            //可选条件【人名name】【类型category】【号码归属地location】【运营商operator】【号段head_num】【尾号tail_num】
                            //可由多个可选条件确定必要条件
                            CallAction callAction = new CallAction(slotsBean.getName(), slotsBean.getCode(), MainActivity.this);//目前可根据名字或电话号码拨打电话
                            callAction.start();
                            break;
                        }
                        case "VIEW": {    //2查看电话拨打记录
                            //必要条件无
                            //可选条件【未接电话】【已拨电话】【已接电话】
                            CallView callview = new CallView(this);
                            callview.start();
                            break;
                        }
                        default:
                            break;
                    }

                    break;
                case "message": {//2 短信相关服务

                    switch (operation) {

                        case "SEND": {//1发送短信
                            SendMessage sendMessage = new SendMessage(slotsBean.getName(), slotsBean.getCode(), slotsBean.getContent(), MainActivity.this);
                            sendMessage.start();
                            break;
                        }

                        case "VIEW": {//2查看发送短信页面

                            MessageView messageView = new MessageView(this);
                            messageView.start();
                            break;
                        }

                        default:
                            break;
                    }

                    break;
                }
                case "app": {//3 应用相关服务

                    switch (operation) {

                        case "LAUNCH": {//1打开应用
                            OpenAppAction openApp = new OpenAppAction(slotsBean.getName(), MainActivity.this);
                            openApp.start();
                            break;
                        }

                        case "QUERY": {//2应用中心搜索应用
                            SearchApp searchApp = new SearchApp(slotsBean.getName(), this);
                            searchApp.start();
                            break;
                        }

                        default:
                            break;

                    }
                    break;
                }

                case "websearch": {//5 搜索相关服务

                    switch (operation) {

                        case "QUERY": {//1搜索

                            SearchAction searchAction = new SearchAction(slotsBean.getKeywords(), MainActivity.this);
                            searchAction.Search();
                            break;
                        }

                        default:
                            break;

                    }

                    break;
                }

                case "faq": {//6 社区问答相关服务

                    switch (operation) {
                        case "ANSWER": {//1社区问答
                            OpenQA openQA = new OpenQA(answerBean.getText(), this);
                            openQA.start();
                            break;
                        }
                        default:
                            break;
                    }

                    break;

                }

                case "chat": {//7 聊天相关服务

                    switch (operation) {

                        case "ANSWER": {//1聊天模式

                            OpenQA openQA = new OpenQA(answerBean.getText(), this);
                            openQA.start();

                            break;
                        }

                        default:
                            break;
                    }

                    break;
                }

                case "openQA": {//8 智能问答相关服务

                    switch (operation) {

                        case "ANSWER": {//1智能问答

                            OpenQA openQA = new OpenQA(answerBean.getText(), this);
                            openQA.start();

                            break;
                        }

                        default:
                            break;
                    }

                    break;
                }

                case "baike": {//9 百科知识相关服务

                    switch (operation) {

                        case "ANSWER": {//1百科

                            OpenQA openQA = new OpenQA(answerBean.getText(), this);
                            openQA.start();

                            break;
                        }

                        default:
                            break;
                    }

                    break;
                }

                case "schedule": {//10 日程相关服务

                    switch (operation) {

                        case "CREATE": {//1创建日程/闹钟(直接跳转相应设置界面)

                            ScheduleCreate scheduleCreate = new ScheduleCreate(slotsBean.getName(), datetimeBean.getTime(), datetimeBean.getDate(), slotsBean.getContent(), this);
                            scheduleCreate.start();

                            break;
                        }

                        case "VIEW": {//1查看闹钟/日历(未实现)

                            ScheduleView scheduleView = new ScheduleView(slotsBean.getName(), datetimeBean.getTime(), datetimeBean.getDate(), slotsBean.getContent(), this);
                            scheduleView.start();
                            break;
                        }


                        default:
                            break;
                    }

                    break;
                }

                case "weather": {//11 天气相关服务

                    switch (operation) {

                        case "QUERY": {//1查询天气

                            SearchWeather searchWeather = new SearchWeather(date, resultBean.getCity(), resultBean.getSourceName(), resultBean.getDate(), resultBean.getWeather(), resultBean.getTempRange(), resultBean.getAirQuality(), resultBean.getWind(), resultBean.getHumidity(), resultBean.getWindLevel() + "", this);
                            searchWeather.start();

                            break;
                        }

                        default:
                            break;

                    }

                    break;
                }

                default:
                    mUnderstanderText.setText("我听不懂您说什么，亲爱的，下次可能我就明白了");
                    speakAnswer("我听不懂您说什么，亲爱的，下次可能我就明白了");
                    break;
            }
        }

    }

    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            showTip("再点击一次退出应用程序");
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mSpeechUnderstander.cancel();
        mSpeechUnderstander.destroy();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();

    }


    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(MainActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(MainActivity.this);
        super.onPause();
    }


}
