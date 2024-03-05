package cn.tockey.config;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
//@Slf4j // 使用lombok的日志注解
@ServerEndpoint("/webSocket/{sid}") // ws://localhost:8080/webSocket/userId;
public class WebSocketServer {
    // 当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    // 所有在线的客户端
    private static Map<String, Session> onlineSecClientMap = new ConcurrentHashMap<>();
    // sid
    private String sid;
    private Session session;


    // 连接建立成功事件 【前端 new WebSocket 触发】
    @OnOpen
    public void onOpen(@PathParam("sid") String sid, Session session) {
        onlineSecClientMap.put(sid, session);
        onlineCount.incrementAndGet();
        this.sid = sid;
        this.session = session;
        //sendToOne();
        System.out.println("【websocket消息】有新的连接，总数为:" + onlineCount);
    }

    // 连接关闭的时间
    @OnClose
    public void onClose(@PathParam("sid") String sid, Session session) {
        onlineSecClientMap.remove(sid);
        onlineCount.decrementAndGet(); // -1
        System.out.println("【websocket消息】连接断开，总数为:" + onlineCount);
    }

    // 收到客户端消息事件
    @OnMessage
    public void OnMessage(String message, Session session) {
        System.out.println("【websocket消息】收到客户端消息:" + message+" [sec]:"+session);
        //System.out.println(session.getAsyncRemote().sendText("2295"));
    }

    // 错误事件
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }

    // 封装发送事件 【群发】 广播消息
    public void sendToAll(String message) {
        onlineSecClientMap.forEach((onlineSid, toSession) -> {
            // 不发给自己
            if (!sid.equalsIgnoreCase(onlineSid)) {
                System.out.println("服务端向客户端发送消息");
                toSession.getAsyncRemote().sendText(message);
            }
        });
    }

    // 封装发送事件 【指定发送】
    public void sendToOne(String toSid, String message) {
        Session toSession = onlineSecClientMap.get(toSid);
        // 目标是否存在
        if (toSession == null) {
            System.out.println("【websocket消息】目标不存在");
            return;
        }
        System.out.println("【websocket消息】 单独发送:" + message);
        // 异步发送
        toSession.getAsyncRemote().sendText(message);
        // 同步发送
        //toSession.getBasicRemote().sendText(message);
    }
}


/*
 * 当你在一个类上使用 @Slf4j 注解时，Lombok 会自动生成一个名为 log 的静态成员变量，
 * 该变量是 SLF4J Logger 接口的一个实例。然后，你可以通过这个 log 变量来记录日志，
 * 而无需手动创建和配置 Logger 实例。
 *
    共4个模块，总完成60%
    核心1：前端词集学习模式，已完成80%
    核心2：后端springSecurity的权限控制，已完成70%
    核心3：前端班级聊天室，已完成20%
 * */