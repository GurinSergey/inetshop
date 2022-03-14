package com.webstore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webstore.base.MainMessage;
import com.webstore.base.ResponseMessage;
import com.webstore.base.CommonNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Funki on 25.08.2016.
 */
@Controller
public class MessageController {
    private SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(value = {"/testmess"}, method = RequestMethod.GET)
    public String testMessage(Model model, HttpServletRequest request) throws JsonProcessingException {

        return "testmess_v1";
    }

    @MessageMapping("/chat")
    // @SendTo("/topic/greetings")
    public /*ResponseMessage*/ void greeting(MainMessage message, Principal principal) throws Exception {

        template.convertAndSendToUser(principal.getName(),"/queue/messages",new ResponseMessage("me: " + message.getName()));

        if (!message.getUsername().isEmpty() || !message.getUsername().equals("") ) {
            template.convertAndSendToUser(message.getUsername(), "/queue/messages", new ResponseMessage(principal.getName() + ": " + message.getName()));
        }
        //  return new ResponseMessage(message.getName());
    }

    @MessageMapping("/commonNotifications")
    public void sendNotifications(CommonNotification commonNotification) throws Exception {

        template.convertAndSend("/queue/commonSubscribes",commonNotification);
    }

    @MessageMapping("/send/message")
    public void onRecMess(String message) throws Exception {

        this.template.convertAndSend("/chats",new SimpleDateFormat("HH:mm:ss").format(new Date())+" - "+message);
    }


}