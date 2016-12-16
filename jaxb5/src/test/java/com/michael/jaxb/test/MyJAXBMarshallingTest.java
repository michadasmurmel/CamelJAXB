package com.michael.jaxb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.michael.jaxb.Currency;
import com.michael.jaxb.Manufacturer;
import com.michael.jaxb.Price;
import com.michael.jaxb.Product;
import com.michael.jaxb.ProductList;

public class MyJAXBMarshallingTest {

	@Test
	public void marshallTest() throws JAXBException, IOException {
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			ProductList productList = new ProductList();

			Product product = new Product();
			product.setName("MyProduct");

			Manufacturer manufacturer = new Manufacturer();
			manufacturer.setName("MyManufacturer");

			Price price = new Price();
			price.setAmount(9.99f);
			price.setCurrency(Currency.EUR);

			product.setManufacturer(manufacturer);
			product.setPrice(price);

			productList.getProducts().add(product);
			productList.getProducts().add(product);

			File file = new File("src/test/resources/xml/NewProducts.xml");
			file.getParentFile().mkdirs();
			file.createNewFile();

			JAXBContext jaxbContext = JAXBContext.newInstance(ProductList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(productList, file);
		} catch (JAXBException e) {
			System.out.println(e);
			throw e;
		}
	}

	@Test
	public void unmarshallTest() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ProductList.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		File file = new File("src/main/resources/xml/Products.xml");
		ProductList productList = (ProductList) unmarshaller.unmarshal(file);
		assertNotNull(productList);
	}

	@Test
	public void ftpDownload() {
		final CamelContext camelContext = new DefaultCamelContext();
		PropertiesComponent pc = camelContext.getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:ftp.properties");

		String uri = "{{bplaced.ftp.client}}";
		String incoming = "{{bplaced.ftp.path.incoming}}";
		String outgoing = "{{bplaced.ftp.path.outgoing}}";
		String otherwise = "{{bplaced.ftp.path.otherwise}}";
		String username = "{{bplaced.ftp.username}}";
		String password = "{{bplaced.ftp.password}}";

		final String from = new StringBuilder(uri).append("/").append(outgoing).append("?").append(username).append("&")
				.append(password).toString();
		final String to = new StringBuilder(uri).append("/").append(incoming).append("?").append(username).append("&")
				.append(password).toString();
		final String other = new StringBuilder(uri).append("/").append(otherwise).append("?").append(username)
				.append("&").append(password).toString();

		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// this.from(from).filter()
					// .xpath("/ProductList/Product/manufacturer[data/@attrib='name'="
					// + filterManufacturer + "]")
					// .xpath("/ProductList/Product[@name='MySecondProduct']").to(to);

					this.from(from).choice().when().xpath("/ProductList/Product[@name='MySecondProducts']").to(to)
							.otherwise().to(other).end();
				}
			});
			camelContext.start();
			Thread.sleep(5000);
			camelContext.stop();
		} catch (Exception camelException) {
			System.out.println(String.format("Exception trying to copy files - %s", camelException.toString()));
			fail(camelException.toString());
		}
	}

	@Test
	public void convert() {
		final CamelContext camelContext = new DefaultCamelContext();
		PropertiesComponent pc = camelContext.getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:ftp.properties");

		String uri = "{{bplaced.ftp.client}}";
		String incoming = "{{bplaced.ftp.path.incoming}}";
		String outgoing = "{{bplaced.ftp.path.outgoing}}";
		String username = "{{bplaced.ftp.username}}";
		String password = "{{bplaced.ftp.password}}";

		final String from = new StringBuilder(uri).append("/").append(outgoing).append("?").append(username).append("&")
				.append(password).toString();
		final String to = new StringBuilder(uri).append("/").append(incoming).append("?").append(username).append("&")
				.append(password).toString();

		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					 from(from).to("xslt:xslt/ProductsToLisasProducts.xsl").to(to);
				}
			});
			camelContext.start();
			Thread.sleep(5000);
			camelContext.stop();
		} catch (Exception camelException) {
			System.out.println(String.format("Exception trying to copy files - %s", camelException.toString()));
			fail(camelException.toString());
		}
	}
}
