package com.mobigen.framework.utility.restful;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

@Component
public class HttpInterfaceFactoryBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //AutoRegisterApi 어노테이션 사용하는 Interface 검색
        Set<BeanDefinition> beanDefinitions = new HttpInterfaceClassFinder().findBeanDefinitions(beanFactory.getBean(Environment.class));

        // Interface를 ProxyFactory에 등록하는 클래스
        SimpleHttpInterfaceFactory httpInterfaceFactory = new SimpleHttpInterfaceFactory();

        beanDefinitions.stream()
                .filter(v -> StringUtils.hasText(v.getBeanClassName()))
                .forEach(v -> findClassAndRegisterAsSingletonBean(beanFactory, httpInterfaceFactory, v));
    }

    private void findClassAndRegisterAsSingletonBean(
            ConfigurableListableBeanFactory beanFactory,
            SimpleHttpInterfaceFactory factory,
            BeanDefinition beanDefinition) {

        // 빈을 Class이름으로 등록
        beanFactory.registerSingleton(
                Objects.requireNonNull(beanDefinition.getBeanClassName()),
                factory.create(findHttpInterfaceClass(beanDefinition))
        );
    }

    private Class<?> findHttpInterfaceClass(BeanDefinition beanDefinition) {
        try {
            return ClassUtils.forName(beanDefinition.getBeanClassName(), this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
