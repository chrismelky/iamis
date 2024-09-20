package tz.go.zanemr.auth.core;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import tz.go.zanemr.auth.modules.authority.Authority;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    // Load authorities
    @Async()
    public void initializeAuthorities() {
        try {
            requestMappingHandlerMapping
                    .getHandlerMethods()
                    .forEach(
                            (requestMappingInfo, handlerMethod) -> {
                                NoAuthorization noAuthorization =
                                        handlerMethod.getMethodAnnotation(NoAuthorization.class);

                                if (!requestMappingInfo.getPatternValues().isEmpty()) {
                                    List<String> urls = new ArrayList<>(requestMappingInfo.getPatternValues());

                                    if (!requestMappingInfo.getMethodsCondition().getMethods().isEmpty()
                                            && urls.size() == 1
                                            && urls.getFirst().contains("api")
                                            && noAuthorization == null) {

                                        String resourceName =
                                                handlerMethod.getBean().toString().replace("Resource", "");

                                        resourceName =
                                                resourceName.substring(0, 1).toUpperCase() + resourceName.substring(1);
                                        String actionName = handlerMethod.getMethod().getName();

                                        String actionDisplayName = Utils.splitCamelCase(actionName);
                                        String authName = resourceName.toUpperCase().concat("_").concat(actionName.toUpperCase());
                                        actionDisplayName =
                                                actionDisplayName.substring(0, 1).toUpperCase()
                                                        + actionDisplayName.substring(1);

                                        Authority authority = new Authority();
                                        authority.setName(authName);
                                        authority.setDescription(resourceName.concat(" ").concat(actionDisplayName));

                                        try {
                                            kafkaTemplate.send("authority.create", authority);
                                            logger.info("**************Published authority {} to kafka", authName);
                                        } catch (Exception e) {
                                            logger.error(e.getMessage());
                                        }
                                    }
                                }
                            });
        } catch (Exception e) {
            logger.error("############# Error on initializing authorities #####################", e);
            System.exit(1);
        }
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        logger.info("######beofre");
        initializeAuthorities();
        logger.info("######after");
    }
}
