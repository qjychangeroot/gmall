package com.atguigu.gmall.cart.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Data
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    private PublicKey publicKey;

    private String publicKeyPath;

    private String userKeyName;

    private Integer expire;

    private String cookieName;

    @PostConstruct
    private void init(){
        try {
            //3. 读取秘钥
            publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！！！");
            e.printStackTrace();
        }
    }

}
