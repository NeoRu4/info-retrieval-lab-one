package lab.inforetrieval;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.util.Arrays;

import lab.dataset.MovieImporterSingleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InfoRetrievalLabApplication  {

	public static void main(String[] args) throws Exception {

		ApplicationContext applicationContext = SpringApplication.run(InfoRetrievalLabApplication.class, args);
		MovieImporterSingleton.getInstance().makeNewDataSet();

		System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println("\t" + beanName);
			}

		};
	}


}
