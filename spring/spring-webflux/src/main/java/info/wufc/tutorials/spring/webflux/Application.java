package info.wufc.tutorials.spring.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    @RequestMapping("/employees")
    public class EmployeeController {

        @GetMapping("/{id}")
        private Mono<String> getEmployeeById(@PathVariable String id) {
            return Mono.just("001");
        }

    }

}
