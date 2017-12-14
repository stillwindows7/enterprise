package io.github.enterprise.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dozer 配置文件
 * 
 * Created by Sheldon on 2017年12月12日
 *
 */
@Configuration
public class DozerConfig {
	
    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        DozerBeanMapper dozerBean = new DozerBeanMapper();
        return dozerBean;
    }
    
}
