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
		Thread.sleep(60000); // 1분(60초) 지연(Cron으로 1분마다 실행하는 서비스를 테스트하니까 최대 1분 지연)
	}
}
