package com.mobigen.framework.utility.restful;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * AutoRegisterApi 어노테이션을 찾는 클래스
 */
public class HttpInterfaceClassFinder {
    public Set<BeanDefinition> findBeanDefinitions(Environment environment) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface()
                        && beanDefinition.getMetadata().hasAnnotation(AutoRegisterApi.class.getName());
            }
        };

        scanner.addIncludeFilter(new AnnotationTypeFilter(AutoRegisterApi.class));

        // basePackage 지정
        return scanner.findCandidateComponents("com.mobigen");
    }
}
