package runtracker.android.bignerdranch.com.runtracker;

import java.util.Date;

/**
 * Created by sheng on 16/10/12.
 * 旅程信息
 */
public class Run  {

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }
/**
 * chapter34 区分每一个旅程的id
 * */
    private long mId;
    public Date getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }
/**
 * 开始日期
 * */
    private Date mStartDate;
    public Run(){
        mStartDate=new Date();
    }
/**
 * 持续时间
 * */
    public int getDurationSeconds(long endMillis){
        return (int)((endMillis-mStartDate.getTime())/1000);

    }
/**
 * 将持续时间格式化显示为字符串信息
 * */
    public static String formatDuration(int durationSeconds){
        int seconds=durationSeconds%60;
        int minutes=((durationSeconds-seconds)/60) % 60;
        int hours=(durationSeconds-(minutes*60) -seconds)/3600;
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
}
