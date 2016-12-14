package com.michael.business;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;

public class Foo {
	@EndpointInject(uri = "activemq:foo.bar")
	ProducerTemplate producer;

	public void doSomething() {
		this.producer.sendBody("<hello>world!</hello>");
	}
}