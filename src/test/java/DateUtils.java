import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/10.
 */
public class DateUtils {

    /**
     * 字符串转时间
     */
    public static Date stringToDate(Date date, String time) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(date);
        dateStr += " " + time;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            date = sf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取提前/推迟分钟数
     */
    public static Date getHandleTime(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的前后N天时间
     */
    public static Date getAroundDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }


    /**
     * 比较两个Time日期字符串大小
     */
    public static int compareTime(String startTime, String endTime) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date d1 = dateFormat.parse(startTime);
        Date d2 = dateFormat.parse(endTime);
        return d1.compareTo(d2);
    }


    /**
     * Time字符串增加分钟
     * @param startTime
     * @param minute
     * @throws Exception
     */
    public static String timeerAddMinute(String startTime, int minute) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date d1 = dateFormat.parse(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        calendar.add(Calendar.MINUTE, minute);
        return dateFormat.format(calendar.getTime());
    }




    public static void main(String[] args) throws Exception{
        System.out.println(timeerAddMinute("22:00:00",-900));

        /**
         * step1 : 先比较time 大小 确认Date  如果结束时间小于开始时间则结束时间Date+1
         * step2 : 将time转成Date确定基础日期
         * step3 : 获取延时分钟数的Date
         * step4 : 确定当前传入时间是否在区间内
         *
         * 固
         *  if([ startTime  ---  endTime  ]){
         *      if(存在记录){
         *          // 更新第二次记录，重新设置值
         *          // XB
         *      }else{
         *          // 记录第一次时间
         *          if(time in [XB] ){
         *              则记录为XB
         *          }else{
         *              SB 插入一条记录
         *          }
         *      }
         *  }else{
         *      log.i("当前无效")
         *  }
         *
         *
         * 弹
         *  if(time in [ 1,2,3,4,5]){
         *      if(当时记录存在){
         *          XB 记录时长
         *      }else{
         *          SB
         *      }
         *
         *  }else{
         *      Log.i(无效)
         *  }
         *
         *
         *
         */

    }


}
