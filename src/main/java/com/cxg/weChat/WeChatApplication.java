package com.cxg.weChat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.cxg.weChat.*.mapper")
@SpringBootApplication
public class WeChatApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WeChatApplication.class, args);
		System.out.println("<-------Megatron Running----------> \n"+
				" __  __               _                \n" +
				"|  \\/  |___ __ _ __ _| |_ _ _ ___ _ _  \n" +
				"| |\\/| / -_) _` / _` |  _| '_/ _ \\ ' \\ \n" +
				"|_|  |_\\___\\__, \\__,_|\\__|_| \\___/_||_|\n" +
				"           |___/                       ");
	}

}
