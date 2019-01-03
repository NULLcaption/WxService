package com.cxg.weChat;

import com.cxg.weChat.crm.pojo.TestDo;
import com.cxg.weChat.crm.pojo.TestDo2;
import com.cxg.weChat.crm.service.PlanActivitySrevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatApplicationTests {

	@Autowired
	PlanActivitySrevice planActivitySrevice;

	@Test
	public void contextLoads() {
		String detailId = "137022";
		List<TestDo> list = planActivitySrevice.getItemIdList(detailId);
		if (list != null) {
			int count = 0;
			for (TestDo test : list) {
				TestDo2 test2 = new TestDo2();
				test2.setItemId(test.getItemId());
				test2.setActualSales(test.getActualSales());
				count = planActivitySrevice.updateImptentByUpdate(test2);
				if (count ==0 ) {
					System.out.println("更新失败");
				}
				count++;
			}
			System.out.println("更新成功："+count);
		}
	}



}
