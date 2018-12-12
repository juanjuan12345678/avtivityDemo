	 *      act_ge_bytearray:
	 *        1、英文解释
	 *           act:activiti
	 *           ge:general
	 *           bytearray:二进制
	 *        2、字段
	 *           name_:文件的路径加上名称
	 *           bytes_:存放内容
	 *           deployment_id_:部署ID
	 *        3、说明：
	 *             如果要查询文件(bpmn和png)，需要知道deploymentId
	 *      act_re_deployment
	 *        1、解析
	 *           re:repository
	 *           deployment:部署  用户描述一次部署
	 *        2、字段
	 *            ID_：部署ID  主键
	 *      act_re_procdef
	 *        1、解释
	 *            procdef: process definition  流程定义
	 *        2、字段
	 *            id_:pdid:pdkey:pdversion:随机数
	 *            name:名称
	 *            key:名称
	 *            version:版本号
	 *                如果名称不变，每次部署，版本号加1
	 *                如果名称改变，则版本号从1开始计算
	 *            deployment_id_:部署ID