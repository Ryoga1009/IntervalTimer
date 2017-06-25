package com.example.ryouga.intervaltimer;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private Button startButton, stopButton;
    private TextView timerText;
    private MediaPlayer mediaPlayer;
    Spinner spinner_h;
    Spinner spinner_m;
    Spinner spinner_s;
    Spinner spinner_interval;
    LinearLayout  linerLayout;
    Random rnd;
    private int a=0;



    //スレッド管理用Handler
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //画面 ON 固定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        startButton = (Button) findViewById(R.id.button_Start);
        stopButton = (Button) findViewById(R.id.Stop_button);

        timerText = (TextView) findViewById(R.id.timer);
        timerText.setText("0:00.0");

        timerText.setTextSize(70);

        mediaPlayer = MediaPlayer.create(this,R.raw.alerm);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        Toast.makeText(this, "注意\n" + "インターバルを必ず1秒以上で設定してください。\n" +
                "アプリを終了するときはホームに戻るだけでなく、タスクから切るようにしてください。", Toast.LENGTH_LONG).show();


        Log.d("myError","onCreate");


        //hundlerのコンストラクタ呼び出し
        this.handler = new Handler();

        //タイマーセット
        this.setTimer();
    }

    private void setTimer() {
        Timer timer = new Timer();
        //遅延０ms  10000msごとに呼び出し　
        timer.scheduleAtFixedRate(new TestTask(), 0, 1000);
        Log.d("myError", "setTimer");

    }


    class TestTask extends TimerTask {

        int m, s, h;
        int m2, s2, h2, m3, s3, h3;
        int sum = 3,sum2, state = 0;  //時間の合計(秒),入力とタイマー識別用state
        int Case = 0;//タイマーとアラーム(インターバル)認識用
        int Interval = 1, Interval2;
        int Flag = 1;
        String State = "set";






        public TestTask() {
            Log.d("myError", "TestTask");
            spinner_h = (Spinner) findViewById(R.id.spinner_h);
            spinner_m = (Spinner) findViewById(R.id.spinner_m);
            spinner_s = (Spinner) findViewById(R.id.spinner_s);
            spinner_interval = (Spinner) findViewById(R.id.spinner_inter);
            linerLayout = (LinearLayout)findViewById(R.id.liner);
            rnd = new Random();




            /*
            setadapter(spinner_h);
            setadapter(spinner_m);
            setadapter(spinner_s);
            setadapter(spinner_interval);
*/
            Log.d("myError","setadopter");

        }


        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    //メインループ

                    Log.d("myError","run_degin");

                    switch (State) {

                        case "set": {

                            Log.d("myError","set");

                            //背景色を変更
                            linerLayout.setBackgroundColor(Color.TRANSPARENT);



                            //Spinnerからの値を変換して取得
                            Interval = Change_Style("interval");
                            m = Change_Style("m");
                            s = Change_Style("s");
                            h = Change_Style("h");







                            //インターバルと時間の設定忘れによる誤動作を防ぐ
                            if(Interval==0&&h==0&&m==0&&s==0){
                                startButton.setVisibility(startButton.INVISIBLE);
                            }else if(Interval!=0&&h==0&&m==0&&s==0) {
                                startButton.setVisibility(startButton.INVISIBLE);
                            }else if(Interval==0&&(h!=0||m!=0||s!=0)){
                                startButton.setVisibility(startButton.INVISIBLE);
                            }
                            if(Interval!=0&&(h!=0||m!=0||s!=0)){
                                startButton.setVisibility(startButton.VISIBLE);
                            }


                            startButton.setOnClickListener(new View.OnClickListener() {//Startボタン
                                @Override
                                public void onClick(View v) {
                                    state = 1;
                                    spinner_h.setVisibility(spinner_m.INVISIBLE);
                                    spinner_m.setVisibility(spinner_m.INVISIBLE);
                                    spinner_s.setVisibility(spinner_s.INVISIBLE);
                                    //  spinner_interval.setVisibility(spinner_interval.INVISIBLE);

                                    //カウントダウンへ
                                    State="CountDown";

                                    //値のコピー保管
                                    m2 = m;
                                    s2 = s;
                                    h2 = h;
                                    Interval2=Interval;
                                    //全時間を秒に変換
                                    sum = h*3600+m * 60 + s;
                                    sum2=sum;

                                    Log.d("myError","onClick_start");

                                  //  timerText.setText(h2 + ":" + m2 + "." + s2);

                                }
                            });

                            //timeの描画
                            timerText.setText(h + ":" + m + "." + s);


                            break;
                        }

                        case "CountDown": {
                            Log.d("myError","CountDown");

                            //背景色を変更
                            linerLayout.setBackgroundColor(Color.TRANSPARENT);

                            //timeの描画とsum-1
                            sum2=View_time(sum2);


                            //カウントが0になったらアラームへ
                            if(sum2 < 0){
                                State="interval_alerm";
                                //値 2を戻す
                                sum2 = sum;
                                Interval2=Interval;

                            }


                            stopButton.setOnClickListener(new View.OnClickListener() {//Stopボタン
                                @Override
                                public void onClick(View v) {
                                    State = "set";

                                    timerText.setText(h + ":" + m + "." + s);
                                    spinner_h.setVisibility(spinner_m.VISIBLE);
                                    spinner_m.setVisibility(spinner_m.VISIBLE);
                                    spinner_s.setVisibility(spinner_s.VISIBLE);
                                    //  spinner_interval.setVisibility(spinner_interval.VISIBLE);

                                    mediaPlayer.pause();
                                    mediaPlayer.seekTo(0); // 再生位置を0ミリ秒に指定
                                    Log.d("myError","onClick_stop");


                                }
                            });


                            break;
                        }

                        case "loop": {

                            //背景色を変更
                            linerLayout.setBackgroundColor(Color.TRANSPARENT);

                            Log.d("myError","loop");

                            sum2 = View_time(sum2);

                            //カウントが0になったらアラームへ
                            if(sum2 < 0){
                                State = "interval_alerm";
                                //値 2を戻す
                                sum2 = sum;
                                Interval2 = Interval;

                            }



                            stopButton.setOnClickListener(new View.OnClickListener() {//Stopボタン
                                @Override
                                public void onClick(View v) {
                                    State = "set";

                                    timerText.setText(h + ":" + m + "." + s);
                                    spinner_h.setVisibility(spinner_m.VISIBLE);
                                    spinner_m.setVisibility(spinner_m.VISIBLE);
                                    spinner_s.setVisibility(spinner_s.VISIBLE);
                                    //  spinner_interval.setVisibility(spinner_interval.VISIBLE);

                                    mediaPlayer.pause();
                                    mediaPlayer.seekTo(0); // 再生位置を0ミリ秒に指定

                                    Log.d("myError","onClick_stop");

                                }
                            });


                            break;
                        }

                        case "interval_alerm": {
                            Log.d("myError","alerm");



                            Interval2 -= 1;

                                //音の再生
                                mediaPlayer.start();
                                Log.d("myError","media.start");


                            stopButton.setOnClickListener(new View.OnClickListener() {//Stopボタン
                                @Override
                                public void onClick(View v) {
                                    State = "set";

                                    timerText.setText(h + ":" + m + "." + s);
                                    spinner_h.setVisibility(spinner_m.VISIBLE);
                                    spinner_m.setVisibility(spinner_m.VISIBLE);
                                    spinner_s.setVisibility(spinner_s.VISIBLE);
                                    //  spinner_interval.setVisibility(spinner_interval.VISIBLE);

                                    mediaPlayer.pause();
                                    mediaPlayer.seekTo(0); // 再生位置を0ミリ秒に指定

                                    Log.d("myError","onClick_stop");
                                }
                            });

                            //Interval分まわしたらloopへ
                            if(Interval2 == 0){
                                State = "loop";
                                sum2=sum;

                            }


                            switch(a){
                                case 0:{
                                    //背景色を変更
                                    linerLayout.setBackgroundColor(Color.RED);
                                    a+=1;
                                    break;
                                }
                                case 1:{
                                    //背景色を変更
                                    linerLayout.setBackgroundColor(Color.GREEN);
                                    a+=1;
                                    break;
                                }
                                case 2:{
                                    //背景色を変更
                                    linerLayout.setBackgroundColor(Color.CYAN);
                                    a=0;
                                    break;
                                }
                                case 3:{
                                     //背景色を変更
                                    linerLayout.setBackgroundColor(Color.MAGENTA);
                                    a=0;
                                    break;
                                }
                            }


                            break;
                        }


                    }

                }
            });

        }
    }


    public String get_Timer_h(){

      //  Spinner spinner_h=(Spinner)findViewById(R.id.spinner_h);
        Log.d("myError","Spinner");

        String item=(String)spinner_h.getSelectedItem();

        Log.d("myError","Set_Timer_h");

        return item;
    }

    public String get_Timer_m(){
        //Spinner spinner_m=(Spinner)findViewById(R.id.spinner_m);
        Log.d("myError","Spinner");

        String item=(String)spinner_m.getSelectedItem();

        Log.d("myError","Set_Timer_m");

        return item;

    }
    public String get_Timer_s(){
      //  Spinner spinner_s=(Spinner)findViewById(R.id.spinner_s);
        Log.d("myError","Spinner");

        String item=(String)spinner_s.getSelectedItem();

        Log.d("myError","Set_Timer_s");

        return item;

    }

    public String get_Interval(){

      //  Spinner spinner_interval = (Spinner)findViewById(R.id.spinner_inter);
       Log.d("myError","getInterval");
        String item = (String)spinner_interval.getSelectedItem();

        return item;

    }



    //型変換
    public int Change_Style(String type){

        int i=0;

        switch(type) {

            case "h":{
                i=Integer.parseInt(get_Timer_h());
                break;
            }
            case "m":  {
                i = Integer.parseInt(get_Timer_m());

                break;

            }
            case "s": {
                i = Integer.parseInt(get_Timer_s());
                break;
            }
            case "interval":{
                i = Integer.parseInt(get_Interval());
                break;

            }


        }
        Log.d("myError","Change_style");


        return i;
    }


    public int View_time(int sum){
        int time=sum;

        //秒から時間分秒の割り出し
        int m3,s3,h3;
        h3=time/3600;
        time=time-h3*3600;
        m3 = time/60;
        time=time-m3*60;
        s3 = time%60;
        if(s3<0){
            s3+=1;
        }

        //描画
        timerText.setText(h3 + ":" + m3 + "." + s3);

        Log.d("myError","View_time");


        //sum-1を返す
        return sum-1;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();

        this.finish();
        this.moveTaskToBack(true);

        //画面 ON 固定解除
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }
    public void setadapter(Spinner spinner){

        // アダプターに項目を追加
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, list);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
    }
}





