package com.cumulocity.lpwan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.cumulocity.lpwan"})
public class LpwanCOnfiguration {

   /* @Bean
    public DeviceCodecService getCodecService(){
        return new DeviceCodecService();
    }*/
}
