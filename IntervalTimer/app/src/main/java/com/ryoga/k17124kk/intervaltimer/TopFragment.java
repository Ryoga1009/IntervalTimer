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

import com.ryoga.k17124kk.intervaltimer.databinding.FragmentTopBinding;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


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

        // Inflate the layout for this fragment
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

        private int interval;//カウントダウンループのインターバル

        private TimeConverter timeConverter;
        String status = "SETTING";//"COUNTDOWN"  "INTERVAL"


        public Task() {

            timeConverter = new TimeConverter();


            //ViewからとるのでgetView()
            binding.buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = "COUNTDOWN";

                    binding.buttonStart.setVisibility(INVISIBLE);
                    binding.buttonStart.setVisibility(VISIBLE);
                    //Spinnerを無効に
                    setEnable(false);
                }
            });

            binding.buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = "SETTING";

                    binding.buttonStart.setVisibility(VISIBLE);
                    binding.buttonStop.setVisibility(INVISIBLE);
                    //Spinnerを有効に
                    setEnable(true);

                    backgroundColorChange_default();
                }
            });


        }

        //SpinnerのEnable変更
        public void setEnable(boolean b) {
            binding.spinnerH.setEnabled(b);
            binding.spinnerM.setEnabled(b);
            binding.spinnerS.setEnabled(b);
            binding.spinnerInterval.setEnabled(b);
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (status.equals("SETTING")) {
                        inspectionSpinner();

                        //各時間の設定
                        setTimeConverter_HMS();
                        //各時間を退避
                        timeConverter.evacuateTime();
                        binding.textViewTime.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());

                    } else if (status.equals("COUNTDOWN")) {
                        //トータル時間から１秒減らす
                        timeConverter.setTotalSecond(timeConverter.getTotalSecond() - 1);
                        //トータル時間から時分秒に変換
                        timeConverter.distribution();


                        binding.textViewTime.setText(timeConverter.getHour() + " : " + timeConverter.getMinute() + " : " + timeConverter.getSecond());
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
            if (binding.spinnerH.getSelectedItemId() == 0 && binding.spinnerM.getSelectedItemId() == 0 && binding.spinnerS.getSelectedItemId() == 0) {
                binding.buttonStart.setVisibility(INVISIBLE);
            } else {
                binding.buttonStart.setVisibility(VISIBLE);
            }
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
