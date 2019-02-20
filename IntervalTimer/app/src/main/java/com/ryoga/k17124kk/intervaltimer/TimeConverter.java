package com.ryoga.k17124kk.intervaltimer;


public class TimeConverter {
    private int hour = 0;//表示する　時
    private int minute = 0;
    private int second = 0;

    private int hour_Evacuate = 0;//退避　時
    private int minute_Evacuate = 0;
    private int second_Evacuate = 0;

    private int _s = 0;//合計秒数


    public TimeConverter() {
    }

    public TimeConverter(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }


    public TimeConverter(int s) {
        this._s = s;
    }


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
        culculationTotalSecond();
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
        culculationTotalSecond();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
        culculationTotalSecond();
    }


    public void setTimes(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }


    public int get_S() {
        return _s;
    }

    public void set_S(int ms) {
        this._s = ms;
    }

    //時分秒からトータルの秒に変換
    public void culculationTotalSecond() {
        _s = this.hour * 360 + this.minute * 60 + this.second;
    }

    public void setTotalSecond(int totalSecond) {
        _s = totalSecond;
    }

    public int getTotalSecond() {
        return this._s;
    }


    //トータルの秒から時分秒に変換
    public void distribution() {
        this.hour = (int) Math.floor(_s / 360);
        _s = _s - (this.hour * 360);

        this.minute = (int) Math.floor(_s / 60);
        _s = _s - (this.minute * 60);

        this.second = _s;
    }

    public void evacuateTime() {
        this.hour_Evacuate = this.hour;
        this.minute_Evacuate = this.minute;
        this.second_Evacuate = this.second;
    }

    //退避させていた各時間から設定を戻す
    public void returnSetting() {
        this.hour = this.hour_Evacuate;
        this.minute = this.minute_Evacuate;
        this.second = this.second_Evacuate;
    }


    @Override
    public String toString() {
        return "TimeConverter{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", _s=" + _s +
                '}';
    }
}
