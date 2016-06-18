package com.uhome.haier.bfvideodemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import bf.cloud.BFMediaPlayerControllerLive;
import bf.cloud.android.mxlbarrage.MxlBarrage;
import bf.cloud.android.playutils.LivePlayer;

/**
 * Created by LuYuan on 2016/6/17 15:26
 */
public class LiveBFActivity extends Activity {

    private BFMediaPlayerControllerLive mMediaContorLive;
    private String mUrl = "servicetype=2&uid=10279577&fid=41BA62731D5855EFF05C05852514EA098FC7F7BE";
    private String mGcid = "41BA62731D5855EFF05C05852514EA098FC7F7BE";
    private TextView mTvMessage;//发送弹幕的信息
    private Button mBtnSend;//发送弹幕按钮
    private Button mBtnHide;//隐藏弹幕按钮,默认Gone
    private LivePlayer mLivePlayer;
    private MxlBarrage mBarrage = null;

    public static final int START_PLAY = 0;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START_PLAY:
                        mLivePlayer.start();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_livebf);
        initView();
        initData();
    }

    private void initView() {
        mMediaContorLive = (BFMediaPlayerControllerLive) findViewById(R.id.media_controller_live);
        mLivePlayer = (LivePlayer) mMediaContorLive.getPlayer();
        //设置直播地址
        mLivePlayer.setDataSource(mUrl);

        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnHide = (Button) findViewById(R.id.btn_hide);

        mBarrage = new MxlBarrage(mGcid);//加载预设的弹幕
        //注册弹幕的监听
        mBarrage.registBarrageListener(new MxlBarrage.BarrageListener() {
            @Override
            public void onMessage(String changeId, String msg) {
                if (mMediaContorLive != null)
                    mMediaContorLive.addDanmaku(false, msg);
            }
        });
    }

    private void initData() {

    }
    @Override
    protected void onPause() {
        if (mBarrage != null && mBarrage.getIsWorking())
            mBarrage.stop();
        mLivePlayer.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBarrage != null && !mBarrage.getIsWorking())
            mBarrage.start();
        super.onResume();
    }

    @Override
    protected void onStart() {
        mHandler.sendEmptyMessageDelayed(START_PLAY, 300);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mLivePlayer.stop();
        mLivePlayer.release();
        try {
            mMediaContorLive.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
