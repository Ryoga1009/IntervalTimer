package com.ryoga.k17124kk.intervaltimer;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class TopFragment extends Fragment {

    private Handler handler;
    private MediaPlayer mediaPlayer;


    public TopFragment() {
        // Required empty public constructor
    }


    public static TopFragment newInstance() {
        return new TopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);

    }

    //onCreateViewの後に呼ばれるメソッド
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //hundlerのコンストラクタ呼び出し
        this.handler = new Handler();
        //タイマーセット
        this.setTimer();

    }

    //Activityと接続したことを通知
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mediaPlayer = MediaPlayer.create(context, R.raw.alerm);
        if (context instanceof MainActivity) {
            ((MainActivity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setTimer() {
        Timer timer = new Timer();
        //遅延０ms  10000msごとに呼び出し　
        timer.scheduleAtFixedRate(new Task(), 0, 1000);
        Log.d("myError", "setTimer");

    }


    class Task extends TimerTask {
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


        public Task() {

            timeConverter = new TimeConverter();


            //ViewからとるのでgetView()
            button_start = getView().findViewById(R.id.button_start);
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

            button_stop = getView().findViewById(R.id.button_stop);
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


            spinner_h = getView().findViewById(R.id.spinner_h);
            spinner_m = getView().findViewById(R.id.spinner_m);
            spinner_s = getView().findViewById(R.id.spinner_s);
            spinner_interval = getView().findViewById(R.id.spinner_interval);

        }

        //SpinnerのEnable変更
        public void setEnable(boolean b) {
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
                    textView_time = getView().findViewById(R.id.textView_time);

                    if (status.equals("SETTING")) {
                        inspectionSpinner();

                        //各時間の設定
                        setTimeConverter_HMS();
                        //各時間を退避
                        timeConverter.evacuateTime();
                        textView_time.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());

                    } else if (status.equals("COUNTDOWN")) {
                        //トータル時間から１秒減らす
                        timeConverter.setTotalSecond(timeConverter.getTotalSecond() - 1);
                        //トータル時間から時分秒に変換
                        timeConverter.distribution();


                        textView_time.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());
                        Log.d("MYE", timeConverter.toString());
                        if (timeConverter.get_S() <= 0) {
                            status = "INTERVAL";
                        }

                    } else if (status.equals("INTERVAL")) {
                        //音の再生
                        mediaPlayer.start();

                        //背景色をランダムに変える
                        backgroundColorChange();

                        interval--;

                        if (interval < 0) {
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

        public void inspectionSpinner() {
            if (spinner_h.getSelectedItemId() == 0 && spinner_m.getSelectedItemId() == 0 && spinner_s.getSelectedItemId() == 0) {
                button_start.setVisibility(View.INVISIBLE);
            } else {
                button_start.setVisibility(View.VISIBLE);
            }
        }


        //各時間をスピナーから設定
        public void setTimeConverter_HMS() {
            Log.d("MYE", "setTimeConvert");
            timeConverter.setHour(spinner_h.getSelectedItemPosition());
            timeConverter.setMinute(spinner_m.getSelectedItemPosition());
            timeConverter.setSecond(spinner_s.getSelectedItemPosition());
            interval = spinner_interval.getSelectedItemPosition();
        }


        //背景の色をデフォルトの白に
        public void backgroundColorChange_default() {
            ConstraintLayout constraintLayout = getView().findViewById(R.id.constrainLayout);
            constraintLayout.setBackgroundColor(Color.WHITE);
        }

        //ランダムに背景の色を変える
        public void backgroundColorChange() {
            Random rnd = new Random();
            int color = Color.RED;

            ConstraintLayout constraintLayout = getView().findViewById(R.id.constrainLayout);
            switch (rnd.nextInt(6)) {
                case 0: {
                    color = Color.RED;
                    break;
                }
                case 1: {
                    color = Color.GREEN;
                    break;
                }
                case 2: {
                    color = Color.BLUE;
                    break;
                }
                case 3: {
                    color = Color.CYAN;
                    break;
                }
                case 4: {
                    color = Color.MAGENTA;
                    break;
                }
                case 5: {
                    color = Color.YELLOW;
                    break;
                }
            }
            constraintLayout.setBackgroundColor(color);
        }
    }

}
