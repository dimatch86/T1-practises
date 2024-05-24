package com.t1.httpinterceptor.analyzer;

import com.t1.httpinterceptor.exception.InterceptorPropertyException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.text.MessageFormat;

public class PropertyFailureAnalyzer extends AbstractFailureAnalyzer<InterceptorPropertyException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, InterceptorPropertyException cause) {
        return new FailureAnalysis(MessageFormat.format("Exception when try to set properties: {0}", cause.getMessage()),
                "set-application-properties", cause);
    }
}
