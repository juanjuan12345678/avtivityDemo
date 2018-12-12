import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 创建任务调度
 * Created by Administrator on 2018/12/5.
 */
public class MainScheduler {

    private static String filePath;

    public MainScheduler(String filePath) {
        this.filePath = filePath;
    }

    //创建调度器
    public static Scheduler getScheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }

    //绑定job任务
    public static void schedulerJob() throws SchedulerException {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1", "group1").build();

        // 设置扫描路径
        jobDetail.getJobDataMap().put("filePath",filePath);

        //创建触发器 每3秒钟执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */3 * * * ?"))
                .build();
        Scheduler scheduler = getScheduler();
        //将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail, trigger);
        //调度器开始调度任务
        scheduler.start();
    }


    public static void main(String[] args) throws SchedulerException {
        MainScheduler mainScheduler = new MainScheduler("C:\\Users\\Administrator\\Desktop\\ic\\pic");
        mainScheduler.schedulerJob();
        System.out.println("==========================");
    }


}
