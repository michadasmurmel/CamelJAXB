package com.michael.business;

import org.apache.camel.Produce;

public class MyBean {
	@Produce(uri = "activemq:foo")
	protected MyListener producer;

	public void doSomething() {
		// lets send a message
		String response = this.producer.sayHello("James");
	}
}