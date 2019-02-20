package com.ryoga.k17124kk.intervaltimer;




import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {


    private Handler handler;
    private MediaPlayer mediaPlayer;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //スリープしないよう固定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mediaPlayer = MediaPlayer.create(this,R.raw.alerm);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);




        //hundlerのコンストラクタ呼び出し
        this.handler = new Handler();

        //タイマーセット
        this.setTimer();
    }

    private void setTimer() {
        Timer timer = new Timer();
        //遅延０ms  10000msごとに呼び出し　
        timer.scheduleAtFixedRate(new Task(), 0, 1000);
        Log.d("myError", "setTimer");

    }


    class Task extends TimerTask{
        private TextView textView_time; //カウント時間表示
        private Spinner spinner_h;//時:設定用Spinner
        private Spinner spinner_m;//分:設定用Spinner
        private Spinner spinner_s;//秒:設定用Spinner
        private Spinner spinner_interval;//インターバル:設定用Spinner

        private Button button_start;//スタートボタン
        private Button button_stop;//ストップボタン



        private int interval;//カウントダウンループのインターバル


        private TimeConverter timeConverter;
        String status = "SETTING";//"COUNTDOWN"  "INTERVAL"





        public Task(){

            timeConverter = new TimeConverter();


            button_start = findViewById(R.id.button_start);
            button_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = "COUNTDOWN";

                    button_start.setVisibility(button_start.INVISIBLE);
                    button_stop.setVisibility(button_stop.VISIBLE);
                    //Spinnerを無効に
                    setEnable(false);
                }
            });

            button_stop = findViewById(R.id.button_stop);
            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = "SETTING";

                    button_start.setVisibility(button_start.VISIBLE);
                    button_stop.setVisibility(button_stop.INVISIBLE);
                    //Spinnerを有効に
                    setEnable(true);

                    backgroundColorChange_default();
                }
            });


            spinner_h = findViewById(R.id.spinner_h);
            spinner_m = findViewById(R.id.spinner_m);
            spinner_s = findViewById(R.id.spinner_s);
            spinner_interval = findViewById(R.id.spinner_interval);

        }

        //SpinnerのEnable変更
        public void setEnable(boolean b){
            spinner_h.setEnabled(b);
            spinner_m.setEnabled(b);
            spinner_s.setEnabled(b);
            spinner_interval.setEnabled(b);
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView_time = findViewById(R.id.textView_time);

                    if(status.equals("SETTING")){
                        inspectionSpinner();

                        //各時間の設定
                        setTimeConverter_HMS();
                        //各時間を退避
                        timeConverter.evacuateTime();

                        textView_time.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());

                    }else if(status.equals("COUNTDOWN")){
                        //トータル時間から１秒減らす
                        timeConverter.setTotalSecond(timeConverter.getTotalSecond() - 1);
                        //トータル時間から時分秒に変換
                        timeConverter.distribution();


                        textView_time.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());
                        Log.d("MYE",timeConverter.toString());
                        if(timeConverter.get_S() <= 0){
                            status = "INTERVAL";
                        }

                    }else if(status.equals("INTERVAL")){
                        //音の再生
                        mediaPlayer.start();

                        //背景色をランダムに変える
                        backgroundColorChange();

                        interval--;

                        if(interval < 0){
                            status = "COUNTDOWN";
                            setTimeConverter_HMS();

                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0); // 再生位置を0ミリ秒に指定

                            //背景色を白に戻す
                            backgroundColorChange_default();
                        }
                    }
                }
            });
        }

        public void inspectionSpinner(){
            if(spinner_h.getSelectedItemId()==0 && spinner_m.getSelectedItemId()==0 && spinner_s.getSelectedItemId()==0){
                button_start.setVisibility(View.INVISIBLE);
            }else{
                button_start.setVisibility(View.VISIBLE);
            }
        }


        //各時間をスピナーから設定
        public void setTimeConverter_HMS(){
            Log.d("MYE","setTimeConvert");
            timeConverter.setHour(spinner_h.getSelectedItemPosition());
            timeConverter.setMinute(spinner_m.getSelectedItemPosition());
            timeConverter.setSecond(spinner_s.getSelectedItemPosition());
            interval = spinner_interval.getSelectedItemPosition();
        }




        //背景の色をデフォルトの白に
        public void backgroundColorChange_default() {
            ConstraintLayout constraintLayout = findViewById(R.id.constrainLayout);
            constraintLayout.setBackgroundColor(Color.WHITE);
        }

        //ランダムに背景の色を変える
        public void backgroundColorChange(){
            Random rnd = new Random();
            int color = Color.RED;

            ConstraintLayout constraintLayout = findViewById(R.id.constrainLayout);
            switch(rnd.nextInt(6)){
                case 0:{
                    color = Color.RED;
                    break;
                }
                case 1:{
                    color = Color.GREEN;
                    break;
                }
                case 2:{
                    color = Color.BLUE;
                    break;
                }
                case 3:{
                    color = Color.CYAN;
                    break;
                }
                case 4:{
                    color = Color.MAGENTA;
                    break;
                }
                case 5:{
                    color = Color.YELLOW;
                    break;
                }
            }
            constraintLayout.setBackgroundColor(color);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


    }
}
