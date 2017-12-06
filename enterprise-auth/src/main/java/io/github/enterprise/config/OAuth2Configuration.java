package io.github.enterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

/**
 * Created by Sheldon on 2017/12/06
 */
@Configuration
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()  // 使用 in-memory 存储
                .withClient("client")   // client_id
                .secret("secret")   // client_secret
                .authorizedGrantTypes("authorization_code") // 该 client 允许的授权类型
                .scopes("app"); // 允许的授权范围
    }
}
