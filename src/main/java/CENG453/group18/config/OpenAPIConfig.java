package CENG453.group18.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenAPIConfig {
    License license = new License().name("Apache License").url("https://www.apache.org/licenses/LICENSE-2.0.html");
    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
                .title("Spring Boot Swagger")
                .version("1.0")
                .description("The game 'Catan'")
                .contact(contact())
                .license(license);
        return new OpenAPI().info(info);
    }
    private Contact contact() {
        return new Contact()
                .name("Group-18")
                .url("https://github.com/togishan/CENG453_20231_Group18_backend")
                ;
    }
}
