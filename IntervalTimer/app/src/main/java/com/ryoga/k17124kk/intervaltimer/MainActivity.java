package com.ryoga.k17124kk.intervaltimer;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //スリープしないよう固定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getSupportFragmentManager().beginTransaction().replace(R.id.constrainLayout, TopFragment.newInstance()).commit();


    }
}
