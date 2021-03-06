package com.example.lavanduc.messagefacebook;

import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

public class MainActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    NotificationBadge mBadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBubble();
        Button btn= (Button) findViewById(R.id.btnAddBubble);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addNewBubble();
            }
        });

        if(Build.VERSION.SDK_INT>=23){
            if(!Settings.canDrawOverlays(MainActivity.this)){
                Intent intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Uri.parse("package"+getPackageName());
                startActivityForResult(intent,1000);
            }
        }else{
            Intent intent=new Intent(MainActivity.this, Service.class);
            startService(intent);
        }

    }

    private void initBubble() {
        bubblesManager=new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();

        bubblesManager.initialize();
    }

    private void addNewBubble() {
       BubbleLayout bubbleView=(BubbleLayout)LayoutInflater.from(MainActivity.this)
               .inflate(R.layout.bubble_layout_item,null);


        mBadge=(NotificationBadge)bubbleView.findViewById(R.id.count);
        mBadge.setNumber(35);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                bubblesManager.recycle();
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });




        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView,60,20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }
}
