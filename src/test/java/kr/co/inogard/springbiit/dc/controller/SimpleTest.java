package kr.co.inogard.springbiit.dc.controller;

import kr.co.inogard.springboot.dc.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SimpleTest {

	@Test
	public void test() throws Exception {
		Thread.sleep(30000); // 30ÃÊ Áö¿¬
	}
}
