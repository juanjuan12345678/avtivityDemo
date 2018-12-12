import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2018/7/4.
 * @desc 用于进行演示Activiti的首例程序，即描述如何在代码中实现学生进行请假申请，班主任审核，教务处审核
 */
public class ActivityTest {

    /**
     * 1、部署流程
     * 2、启动流程实例
     * 3、请假人发出请假申请
     * 4、班主任查看任务
     * 5、班主任审批
     * 6、最终的教务处Boss审批
     */


    /**
     * 1：部署一个Activiti流程
     * 运行成功后，查看之前的数据库表，就会发现多了很多内容
     */
    @org.junit.Test
    public void creatActivitiTask(){
        //加载的那两个内容就是我们之前已经弄好的基础内容哦。
        //得到了流程引擎
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("请假申请流程")
                .addClasspathResource("bpmn/shenqing.bpmn")
                .addClasspathResource("activiti_png/shenqing.png")
                .deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称:"+deployment.getName());
        /**
         * act_re_procdef  已部署的流程定义 会增加一条记录
         */
    }

    /****************Activiti流程部署的方法**********************/

    /**
     * 2. 通过 inputstream完成部署
     *
     */
    @org.junit.Test
    public void testDeployFromInputStream(){
        InputStream bpmnStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/shenqing.bpmn");
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addInputStream("bpmn/shenqing.bpmn", bpmnStream)
                .deploy();
    }

    /**
     * 3: 通过zipinputstream完成部署
     *  注意：这个的话，需要将bpmn和png文件进行压缩成zip文件，然后放在项目src目录下即可(当然其他目录也可以)
     */
    @org.junit.Test
    public void testDeployFromZipinputStream(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("shenqing.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    /**************************************/



    /**
     * 2：启动流程实例
     */
    @org.junit.Test
    public void testStartProcessInstance(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService()
                .startProcessInstanceById("1");
        /**
         *  act_ru_task 运行时任务会新增一条记录
         */
    }


    /**
     * 3: 班主任 小毛查询当前正在执行任务
     */
    @org.junit.Test
    public void testQueryTask(){
        //下面代码中的小毛，就是我们之前设计那个流程图中添加的班主任内容
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("小毛")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     * 4: 完成请假申请
     */
    @org.junit.Test
    public void testQingjia(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("2504");
    }



    /**
     * 5 ： 班主任小毛完成任务
     */
    @org.junit.Test
    public void testFinishTask_manager(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getTaskService()
                .complete("202"); //查看act_ru_task数据表
    }

    /**
     * 6 ： 教务处的大毛完成的任务
     */
    @org.junit.Test
    public void testFinishTask_Boss(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("302");  //查看act_ru_task数据表
    }

    /**
     * 删除已经部署的Activiti流程
     */
    @org.junit.Test
    public void testDelete(){
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //第一个参数是部署的流程的ID，第二个true表示是进行级联删除
        processEngine.getRepositoryService()
                .deleteDeployment("1001",true);
    }


    /**************API测试********************/

    /**
     * 根据名称查询流程部署
     */
    @org.junit.Test
    public void testQueryDeploymentByName(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> deployments = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()//按照部署时间排序
                .desc()//按照降序排序
                .deploymentName("请假申请流程")
                .list();
        for (Deployment deployment : deployments) {
            System.out.println(deployment.getId());
        }
    }

    /**
     * 查询所有的部署流程
     */
    @org.junit.Test
    public void queryAllDeplyoment(){
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> lists = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()//按照部署时间排序
                .desc()//按照降序排序
                .list();
        for (Deployment deployment:lists) {
            System.out.println(deployment.getId() +"    部署名称" + deployment.getName());
        }
    }

    /**
     * 查询所有的流程定义
     */
    @org.junit.Test
    public void testQueryAllPD(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> pdList = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition pd : pdList) {
            System.out.println(pd.getName());
        }
    }

    /**
     * 查看流程图
     * 根据deploymentId和name(在act_ge_bytearray数据表中)
     */
    @org.junit.Test
    public void testShowImage() throws Exception{
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream inputStream = processEngine.getRepositoryService()
                /**
                 * deploymentID  部署ID查看
                 * 文件的名称和路径
                 */
                .getResourceAsStream("701","activiti_png/shenqing.png");
        OutputStream outputStream3 = new FileOutputStream("e:/processimg.png");
        int b = -1 ;
        while ((b=inputStream.read())!=-1){
            outputStream3.write(b);
        }
        inputStream.close();
        outputStream3.close();
    }

    /**
     * 根据 流程定义ID 查看图片(在act_re_procdef数据表中)
     * @throws Exception
     */
    @org.junit.Test
    public void testShowImage2() throws Exception{
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream inputStream = processEngine.getRepositoryService()
                .getProcessDiagram("shenqing:1:5");
        OutputStream outputStream = new FileOutputStream("e:/11.png");
        int b = -1 ;
        while ((b=inputStream.read())!=-1){
            outputStream.write(b);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 查看bpmn文件(在act_re_procdef数据表中)
     */
    @org.junit.Test
    public void testShowBpmn() throws Exception{
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream inputStream = processEngine.getRepositoryService()
                .getProcessModel("shenqing:1:5");
        OutputStream outputStream = new FileOutputStream("e:/processimg.bpmn");
        int b = -1 ;
        while ((b=inputStream.read())!=-1){
            outputStream.write(b);
        }
        inputStream.close();
        outputStream.close();
    }

}
