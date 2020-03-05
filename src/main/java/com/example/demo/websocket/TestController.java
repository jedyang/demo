package com.example.demo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/websocket")
public class TestController {

    @RequestMapping("/demo")
    public String page2(){
        return "wsDemo/demo";
    }

    @GetMapping("/sendMsg/{id}")
    @ResponseBody
    public String sendMsg(@PathVariable String id){
        MyWebSocket webSocket = MyWebSocket.getWebSocketMap().get(id);
        try {
            webSocket.sendMessage("this msg from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "sendMsg";
    }
}