package com.uhome.haier.bfvideodemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import bf.cloud.android.playutils.VideoService;
import bf.cloud.android.playutils.VodPlayer;
import bf.cloud.BFMediaPlayerControllerVod;

public class MainActivity extends Activity {
    private VodPlayer mVodPlayer = null;
    private BFMediaPlayerControllerVod mMediaController = null;
    private String[] mUrls = {
        "servicetype=1&uid=4995606&fid=D754D209A442A6787962AB1552FF9412",
        "servicetype=1&uid=10279577&fid=7099A94CAA19F4EF2B3760D2395E2CD8"};
    private int mVideoIndex = 0;
    private VideoService mVideoService = null;
    private ServiceConnection mConnection = null;
    private long mHistory = -1;
    private static final int START_PLAY = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_PLAY:
                    mVodPlayer.stop();
                    if (mHistory > 0) {
                        mVodPlayer.start((int) mHistory);
                    } else {
                        mVodPlayer.start();
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getServiceConnection();
        initView();
    }

    private void getServiceConnection() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mVideoService = ((VideoService.VideoBinder) service).getService();
            }
        };
    }

    private void initView() {
        mVideoIndex = 0;
        mMediaController = (BFMediaPlayerControllerVod) findViewById(R.id.vod_media_controller_vod);
        mVodPlayer = (VodPlayer) mMediaController.getPlayer();
        mVodPlayer.setDataSource(mUrls[0]);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mHistory = mVodPlayer.getCurrentPosition();
        mVodPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onStart() {
        mHandler.sendEmptyMessageDelayed(START_PLAY, 300);
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        try {
            mMediaController.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
