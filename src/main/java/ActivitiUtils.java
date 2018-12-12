import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * Created by Administrator on 2018/7/6.
 */
public class ActivitiUtils {

    /**
     * 部署流程
     */
    @Test
    public void startDeployTest(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .name("会签测试11")
                .addClasspathResource("bpmn/dongtai.bpmn")
                .deploy();
    }

    /**
     * 启动流程实例
     *    可以设置一个流程变量
     */
    @Test
    public void testStartPI(){
        /**
         * 流程变量
         *   给<userTask id="请假申请" name="请假申请" activiti:assignee="#{student}"></userTask>
         *     的student赋值
         */
        Map<String, Object> variables = new HashMap<String, Object>();
        
        List<String> pers = new ArrayList<>();
        pers.add("99");
        pers.add("88");
        pers.add("77");
        
        variables.put("pers", pers);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService()
                .startProcessInstanceById("duoshili98:1:4",variables);
    }


    /**
     * 在完成请假申请的任务的时候，给班主任审批的节点赋值任务的执行人
     */
    @Test
    public void testFinishTask_Teacher(){

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("2513"); //完成任务的同时设置流程变量
        
    }


    /**
     * 在完成班主任审批的情况下，给教务处节点赋值
     */
    @Test
    public void testFinishTask_Manager(){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("manger", "毛国生");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("17503", variables); //完成任务的同时设置流程变量
    }


    /**
     * 结束流程实例
     */
    @Test
    public void testFinishTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("22503");
    }

    @Test
    public void testDelete(){
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //第一个参数是部署的流程的ID，第二个true表示是进行级联删除
        processEngine.getRepositoryService()
                .deleteDeployment("12501",true);
    }



}

