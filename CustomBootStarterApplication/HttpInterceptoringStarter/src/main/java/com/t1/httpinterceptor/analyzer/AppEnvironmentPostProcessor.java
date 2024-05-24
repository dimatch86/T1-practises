package com.t1.httpinterceptor.analyzer;

import com.t1.httpinterceptor.exception.InterceptorPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
public class AppEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String loggingEnable = environment.getProperty("rest-interceptor.enable");
        String logsDestination = environment.getProperty("rest-interceptor.logger.destination");
        String logsPath = environment.getProperty("rest-interceptor.logger.path");
        String logsFilename = environment.getProperty("rest-interceptor.logger.filename");
        String logsLevel = environment.getProperty("rest-interceptor.logger.level");

        boolean enabled = Boolean.parseBoolean(loggingEnable);
        if (enabled) {
            check(logsDestination, logsFilename, logsPath, logsLevel);
        }
    }

    private void check(String logsDestination, String logsFilename, String logsPath, String logsLevel) {
        boolean isInvalidDestination = !StringUtils.hasText(logsDestination) ||
                (!Objects.equals(logsDestination, "FILE") && !Objects.equals(logsDestination, "CONSOLE"));
        if (isInvalidDestination) {
            throw new InterceptorPropertyException("Property 'rest-interceptor.logger.destination' must be FILE or CONSOLE");
        }

        if (Objects.equals(logsDestination, "FILE") && !StringUtils.hasText(logsFilename)) {
            throw new InterceptorPropertyException("Property 'rest-interceptor.logger.filename' must contain any file name");
        }
        if (Objects.equals(logsDestination, "FILE") && !StringUtils.hasText(logsPath)) {
            throw new InterceptorPropertyException("Property 'rest-interceptor.logger.path' must contain path to logs");
        }

        if (!StringUtils.hasText(logsLevel) || !logsLevel.matches("trace|debug|info|warn|error")) {
            throw new InterceptorPropertyException("Property 'rest-interceptor.logger.level' must be trace, debug, info, warn or error");
        }
    }
}
