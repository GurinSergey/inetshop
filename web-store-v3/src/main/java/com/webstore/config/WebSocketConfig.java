package com.webstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by Funki on 25.08.2016.
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue","/user","/topic","/chats");
        config.setApplicationDestinationPrefixes("/app");

    }

    ///resources/core/js/sockjs-0.3.4.js
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //  registry.addEndpoint("/chat","/activeUsers").withSockJS().setClientLibraryUrl("http://176.36.250.254:7777/resources/core/js/sockjs-0.3.4.js").setSessionCookieNeeded(true);
        registry.addEndpoint("/chat","/commonNotifications","/activeUsers").setAllowedOrigins("*").withSockJS()
               /* .setClientLibraryUrl("http://176.36.250.254:8555/resources/js/sockjs.js")*/
                .setSessionCookieNeeded(true);
         //       .setDisconnectDelay(30 * 1000);;

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(2).maxPoolSize(3);
    }

}
