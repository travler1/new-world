package myproject;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloWorldApplication {

	public static void main(String[] args) {

		SpringApplication.run(HelloWorldApplication.class, args);
	}

	//@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Spring Boot에서 로드된 빈의 이름:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}

	//프록시 객체를 json으로 생성 가능. (build.grade에도 설정)
	//기본적으로 초기화 된 프록시 객체만 노출, 초기화 되지 않은 프록시 객체는 노출 안함
	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		return new Hibernate5JakartaModule();
	}
	//강제로 지연 로딩
	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
		//강제 지연 로딩 설정
		hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING,true);
		return hibernate5JakartaModule;
	}

}
