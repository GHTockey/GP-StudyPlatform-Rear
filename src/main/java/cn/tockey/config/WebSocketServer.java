package cn.tockey.config;


import cn.tockey.service.UserService;
import cn.tockey.vo.SocketMessageVo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
//@Slf4j // 使用lombok的日志注解
@ServerEndpoint("/webSocket/{uid}") // ws://localhost:8080/webSocket/userId;
public class WebSocketServer {
    // 当前在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    // 所有在线的客户端
    private static Map<String, Session> onlineSecClientMap = new ConcurrentHashMap<>();
    // 用户会话
    private String uid;
    private Session session;


    // 连接建立成功事件 【前端 new WebSocket 触发】
    @OnOpen
    public void onOpen(@PathParam("uid") String uid, Session session) {
        onlineSecClientMap.put(uid, session);
        onlineCount.incrementAndGet();
        this.uid = uid;
        this.session = session;
        //sendToOne();
        System.out.println("【websocket消息】有新的连接，总数为:" + onlineCount);

        // 有新的连接相当于用户上线 （给前端发送系统消息通知前端在线用户的数据刷新）
        SocketMessageVo messageVo = new SocketMessageVo();
        messageVo.setType(2);
        ArrayList<String> onlineUidList = new ArrayList<>();
        onlineSecClientMap.forEach((uidTemp,sessionTemp)->{
            onlineUidList.add(uidTemp);
        });
        messageVo.setMessage(JSON.toJSONString(onlineUidList));
        sendToAll(messageVo);
    }

    // 连接关闭的时间
    @OnClose
    public void onClose(@PathParam("uid") String uid, Session session) {
        onlineSecClientMap.remove(uid);
        onlineCount.decrementAndGet(); // -1
        System.out.println("【websocket消息】连接断开，总数为:" + onlineCount);
    }

    // 错误事件
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }

    // 收到客户端消息事件 (后端收到前端所有用户的消息按约定来处理给哪个用户发送)
    @OnMessage
    public void OnMessage(String msgVo, Session session) {
        System.out.println("【websocket消息】收到客户端消息:" + msgVo + " [sec]:" + session);
        //System.out.println(session.getAsyncRemote().sendText("2295"));
        JSONObject msgObj = JSON.parseObject(msgVo);
        //sendToOne(msgObj.getString("receiver_id"), msgObj.getString("message"));
        SocketMessageVo messageVo = new SocketMessageVo(
                msgObj.getString("sender_id"),
                msgObj.getString("receiver_id"),
                msgObj.getString("message"),
                msgObj.getInteger("type")
        );
        sendToOne(messageVo);
    }

    // 发送事件 【群发】 广播消息
    private void sendToAll(SocketMessageVo socketMessageVo) {
        onlineSecClientMap.forEach((onlineUid, toSession) -> {
            // 不发给自己
            if (!uid.equalsIgnoreCase(onlineUid)) {
                System.out.println("服务端向客户端发送消息");
                toSession.getAsyncRemote().sendText(JSON.toJSONString(socketMessageVo));
            }
        });
    }

    // 发送事件 【指定发送】
    private void sendToOne(SocketMessageVo msgVo) {
        // 在在线客户端获取目标会话
        Session toSession = onlineSecClientMap.get(msgVo.getReceiver_id());
        // 目标是否存在
        if (toSession == null) {
            System.out.println("【websocket消息】目标不存在");
            return;
        }
        System.out.println(
                "【websocket消息】" +
                        msgVo.getSender_id() + "=>" + msgVo.getReceiver_id()
                        + "单独发送:" + msgVo
        );
        // 异步发送
        toSession.getAsyncRemote().sendText(JSON.toJSONString(msgVo));
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