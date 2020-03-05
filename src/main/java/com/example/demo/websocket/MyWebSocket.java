package com.example.demo.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: 登录结果的websocket
 * @author: yunsheng
 * @createDate: 2020/3/5
 * @version: 1.0
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket/{id}")
public class MyWebSocket {
    /**
     * 静态变量 用来记录当前在线连接数
     */
    private static int onlineCount = 0;

    /**
     * 服务端与单一客户端通信 使用Map来存放 其中标识Key为id
     */
    private static ConcurrentMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<>();
    //不需要区分可使用set
    //private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

    public static ConcurrentMap<String, MyWebSocket> getWebSocketMap() {
        return webSocketMap;
    }

    /**
     * 与某个客户端的连接会话 需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数 session为与某个客户端的连接会话 需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;

        webSocketMap.put(id, this);

        addOnlineCount();
        log.info("有新连接加入，当前在线数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        Map<String, String> map = session.getPathParameters();
        webSocketMap.remove(Integer.parseInt(map.get("id")));

        subOnlineCount();
        log.info("有一连接关闭！当前在线数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端 " + session.getId() + " 的消息:" + message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("LoginResultWebSocket 发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

}
