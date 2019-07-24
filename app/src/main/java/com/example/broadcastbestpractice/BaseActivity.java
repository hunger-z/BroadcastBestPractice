package com.example.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {                //所有活动的爹
    private ForceOfflineReciever reciever;
    class ForceOfflineReciever extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("warning!!");
            builder.setMessage("You are forced offline");
            builder.setCancelable(false);                //对话框不可取消
            builder.setPositiveButton("ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finishAll();
                    Intent intent=new Intent(context,LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onResume() {                           //重写onresume，注册ForceOfflineReciever接收器
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE");
        reciever=new ForceOfflineReciever();
        registerReceiver(reciever,intentFilter);
    }

    @Override                                   //重写，取消注册接收器，只在onPause和onResume可以保证只有交互界面出现强制下线广播
    protected void onPause() {
        super.onPause();
        if(reciever!=null){
            unregisterReceiver(reciever);
            reciever=null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
