package com.uhome.haier.bfvideodemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_bfvideo);
        initView();
    }

    private void initView() {
        TextView mVideoPlayer = (TextView) findViewById(R.id.video_player);
        TextView mLivePlayer = (TextView) findViewById(R.id.live_player);
        mLivePlayer.setOnClickListener(this);
        mVideoPlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.video_player:
                startActivity(new Intent(this,VideoBFActivity.class));
                break;
            case R.id.live_player:
                startActivity(new Intent(this ,LiveBFActivity.class));
                break;
            default:
                break;
        }
    }
}
