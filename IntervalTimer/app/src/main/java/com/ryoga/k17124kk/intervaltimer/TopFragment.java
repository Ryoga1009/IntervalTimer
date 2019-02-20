package com.ryoga.k17124kk.intervaltimer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ryoga.k17124kk.intervaltimer.databinding.FragmentTopBinding;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.ryoga.k17124kk.intervaltimer.TimeConverter.TimerStatus.COUNTDOWN;
import static com.ryoga.k17124kk.intervaltimer.TimeConverter.TimerStatus.INTERVAL;
import static com.ryoga.k17124kk.intervaltimer.TimeConverter.TimerStatus.SETTING;


public class TopFragment extends Fragment {

    private Handler handler;
    private MediaPlayer mediaPlayer;

    private FragmentTopBinding binding;

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


        //Fragmentをバインディング -> findViewById()等省略可能に
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top, container, false);

        return binding.getRoot();
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
        //渡されたcontextはMainActivityを継承しているかチェック
        if (context instanceof MainActivity) {
            //MainActivityにキャストして音の設定をする
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


    }


    class Task extends TimerTask {

        private int interval;//カウントダウンループのインターバル

        private TimeConverter timeConverter;
//        String status = "SETTING";//"COUNTDOWN"  "INTERVAL"


        public Task() {

            timeConverter = new TimeConverter();
            binding.setTimeconverter(timeConverter);

            //ViewからとるのでgetView()
            binding.buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //スピナーが全部0かどうかのチェック
                    if (!isSpinnerSelected()) {
                        timeConverter.updateStatus(COUNTDOWN);
                    } else {
                        Toast.makeText(getContext(), "時間を設定してください", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            binding.buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    timeConverter.updateStatus(SETTING);
                    backgroundColorChange_default();
                }
            });


        }


        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("MYE", "run");

                    if (timeConverter.getCurrentStatus() == SETTING) {

                        //各時間の設定
                        setTimeConverter_HMS();
                        //各時間を退避
                        timeConverter.evacuateTime();
                        timeConverter.updateTime();
                        timeConverter.culculationTotalSecond();

                    } else if (timeConverter.getCurrentStatus() == COUNTDOWN) {
                        //トータル時間から１秒減らす
                        timeConverter.setTotalSecond(timeConverter.getTotalSecond() - 1);
                        //トータル時間から時分秒に変換
                        timeConverter.distribution();
                        timeConverter.updateTime();

                        if (timeConverter.get_S() <= 0) {
                            timeConverter.updateStatus(INTERVAL);
                        }

                    } else if (timeConverter.getCurrentStatus() == INTERVAL) {
                        //音の再生
                        mediaPlayer.start();

                        //背景色をランダムに変える
                        backgroundColorChange();

                        interval--;

                        if (interval < 0) {
//                            status = "COUNTDOWN";
                            timeConverter.updateStatus(COUNTDOWN);
                            setTimeConverter_HMS();
                            timeConverter.culculationTotalSecond();

                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0); // 再生位置を0ミリ秒に指定

                            //背景色を白に戻す
                            backgroundColorChange_default();

                        }
                    }
                }
            });
        }

        public boolean isSpinnerSelected() {
            return (binding.spinnerH.getSelectedItemId() == 0 && binding.spinnerM.getSelectedItemId() == 0 && binding.spinnerS.getSelectedItemId() == 0);
        }


        //各時間をスピナーから設定
        public void setTimeConverter_HMS() {
            timeConverter.setTimes(binding.spinnerH.getSelectedItemPosition(), binding.spinnerM.getSelectedItemPosition(), binding.spinnerS.getSelectedItemPosition());
            interval = binding.spinnerInterval.getSelectedItemPosition();
        }


        //背景の色をデフォルトの白に
        public void backgroundColorChange_default() {
            binding.frameLayout.setBackgroundColor(Color.WHITE);
        }

        //ランダムに背景の色を変える
        public void backgroundColorChange() {
            Random rnd = new Random();
            int color = Color.RED;

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
            binding.frameLayout.setBackgroundColor(color);
        }
    }

}
