package com.dominator;

import com.dominator.AAAconfig.TaskThreadPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@MapperScan("com.dominator.mapper")
//@ServletComponentScan
@EnableScheduling
@Controller
@EnableAsync
@EnableConfigurationProperties({TaskThreadPoolConfig.class} )
public class PmsCoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws IOException{
		Properties properties = new Properties();
		InputStream in = PmsCoreApplication.class.getClassLoader()
				.getResourceAsStream("application.yml");
		properties.load(in);
		SpringApplication app = new SpringApplication(PmsCoreApplication.class);
		app.setDefaultProperties(properties);
		app.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		builder.sources(this.getClass());
		return super.configure(builder);
	}

	@GetMapping("/")
	public String start(){
		return "start";
	}
}
