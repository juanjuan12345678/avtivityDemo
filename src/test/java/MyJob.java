import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 创建要被定执行的任务类
 * Created by Administrator on 2018/12/5.
 */
public class MyJob implements Job {

    // 时间格式化
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String filePaht = dataMap.getString("filePath");
        System.out.println(sdf.format(new Date()) + " ====定期删除文件,路径为： ===== " + filePaht);
        if (StringUtils.isNotBlank(filePaht)) {
            File srcFile = new File(filePaht);
            try {
                if (srcFile.exists()) {
                    deleteDir(srcFile);
                } else {
                    System.out.println(" ================= 删除文件父路径不存在 ============= ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" ================= 文件删除错误 ============= ");
            }

        } else {
            System.out.println(" ================= 删除文件参数不存在 ============= ");
        }
    }

    /**
     * 删除文件
     *
     * @param dir
     * @return
     */
    private static void deleteDir(File dir) throws Exception {
        String now = getFirstDate();
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                try {
                    String fileName = file.getName();
                    if (file.isDirectory() && !"base".equalsIgnoreCase(fileName)) {
                        if (isValidDate(fileName)) {
                            Date old = sdf.parse(fileName);
                            Date newT = sdf.parse(getLast12Months(2));
                            if (old.getTime() < newT.getTime()) {
                                System.out.println( "["+file.getName()+ "]删除成功");
                                FileUtils.deleteDirectory(file);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("===================文件 ： " + file.getName() + " 删除错误");
                }
            }
        }
    }


    /**
     * 判断文件格式是否正确
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = false;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
            convertSuccess = true;
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }


    /**
     * 获取当前月第一天
     *
     * @return
     */
    private static String getFirstDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return sdf.format(c.getTime());
    }


    /**
     * 获取当前月前两个月(当前时间前两个月)
     *
     * @param i
     * @return
     */
    public static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        c.set(Calendar.DATE, 1);        //设置为该月第一天
        Date m = c.getTime();
        return sdf.format(m);
    }


    /**
     * 获取当前月前两个月(自然月)
     *
     * @param
     * @return
     */
    public static String getLastdayMonth(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH,-2);
        return getFirstday_Lastday_Month(calendar1.getTime());
    }

    public static String getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last);

        return endStr.toString();
    }


    public static void main(String[] args) {
        System.out.println(getLast12Months(2));
    }

}
