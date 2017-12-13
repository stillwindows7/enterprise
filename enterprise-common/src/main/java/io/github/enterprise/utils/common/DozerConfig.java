package io.github.enterprise.utils.common;

import org.dozer.DozerBeanMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Dozer 配置文件
 * 
 * Created by Sheldon on 2017年12月12日
 *
 */
@EnableAutoConfiguration
public class DozerConfig {
	
    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        DozerBeanMapper dozerBean = new DozerBeanMapper();
        return dozerBean;
    }
    
}
