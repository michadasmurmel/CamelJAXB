package com.michael.jaxb.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class MyActivitiTest {

	@Rule
	public ActivitiRule activitiRules = new ActivitiRule();

	@Test
	@Deployment(resources = { "MyProcess.bpmn20.xml" })
	public void myProcessTest() {
		ProcessInstance processInstance = this.activitiRules.getRuntimeService()
				.startProcessInstanceByKey("financialReport");
		assertNotNull(processInstance);
		Task task = this.activitiRules.getTaskService().createTaskQuery().singleResult();
		assertEquals("Write monthly financial report", task.getName());
		this.activitiRules.getTaskService().complete(task.getId());
		assertEquals(this.activitiRules.getTaskService().createTaskQuery().singleResult().getName(),
				"Verify monthly financial report");
	}

	@Test
	@Deployment(resources = { "MyProcess2.bpmn20.xml" })
	public void myProcess2Test() {
		ProcessInstance processInstance = this.activitiRules.getRuntimeService()
				.startProcessInstanceByKey("financialReport");
		assertNotNull(processInstance);
		Task task = this.activitiRules.getTaskService().createTaskQuery().singleResult();
		assertEquals("Write monthly financial report", task.getName());
		this.activitiRules.getTaskService().complete(task.getId());
		TaskQuery taskQuery = this.activitiRules.getTaskService().createTaskQuery().active();
		List<Task> list = taskQuery.list();
		assertEquals(list.get(0).getName(), "Verify monthly financial report");
		assertEquals(list.get(1).getName(), "DoStuff");
	}
}
