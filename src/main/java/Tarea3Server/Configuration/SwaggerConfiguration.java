package Tarea3Server.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    private static final String GROUP_NAME = "wecc-products";
    private static final String PATH_REGEX = "/p.*";
    private static final String API_TITLE = "Product service";
    private static final String API_DESCRIPTION = "This API gives support to Product services";

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();
    }

    private Predicate<String> paths() {
        return Predicates.or(PathSelectors.regex(PATH_REGEX));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .build();
    }
}
