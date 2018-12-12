import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */
public class AutoActiviti {


    public ProcessEngine processEngine;

    public AutoActiviti(){
        if(null == processEngine){
            this.processEngine = getProcessEngine();
        }
    }


    protected static ProcessEngine getProcessEngine(){
        ProcessEngine processEngine=ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setDatabaseSchemaUpdate("true")
                .setJobExecutorActivate(false)
                .buildProcessEngine();
        return processEngine;

    }


    /**
     * 创建开始节点
     * @return
     */
    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    /**
     * 创建任务节点
     * @throws Exception
     */
    protected UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }


    /**
     * 创建结束节点
     * @throws Exception
     */
    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }


    /**
     * 创建连线
     * @param from
     * @param to
     * @return
     */
    protected
    SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }



    @Test
    public void testDynamicDeploy() throws Exception {

        // 1. 开始自定义流程
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);

        process.setId("my-process");
        process.addFlowElement(createStartEvent());
        process.addFlowElement(createUserTask("task1", "张三", "fred"));
        process.addFlowElement(createUserTask("task2", "李四", "john"));
        process.addFlowElement(createEndEvent());

        process.addFlowElement(createSequenceFlow("start", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "task2"));
        process.addFlowElement(createSequenceFlow("task2", "end"));

        // 2. 生成图形信息
        new BpmnAutoLayout(model).execute();

        // 3. 将进程部署到引擎
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment")
                .deploy();

        // 4. 启动进程实例
        ProcessInstance processInstance = getProcessEngine().getRuntimeService()
                .startProcessInstanceByKey("my-process");

        // 5. 检查任务是否可用
        List tasks = processEngine.getTaskService().createTaskQuery()
                .processInstanceId(processInstance.getId()).list();


        // 6. Save process diagram to a file
        InputStream processDiagram = processEngine.getRepositoryService()
                .getProcessDiagram(processInstance.getProcessDefinitionId());
        FileUtils.copyInputStreamToFile(processDiagram, new File("target/diagram.png"));




        // 7. Save resulting BPMN xml to a file
        InputStream processBpmn = processEngine.getRepositoryService().
                getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
        FileUtils.copyInputStreamToFile(processBpmn, new File("target/process.bpmn20.xml"));

    }

}
