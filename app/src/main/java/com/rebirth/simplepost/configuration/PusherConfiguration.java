package com.rebirth.simplepost.configuration;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfiguration {

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher("244788", "40e1db2f5dcb09daa268", "70e227401368da98094e");
        pusher.setCluster("mt1");
        pusher.setEncrypted(true);
        return pusher;
    }


}
