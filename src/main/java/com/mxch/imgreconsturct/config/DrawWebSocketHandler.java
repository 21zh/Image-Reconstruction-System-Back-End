package com.mxch.imgreconsturct.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DrawWebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, Set<WebSocketSession>> ROOM_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getParam(session, "roomId");
        ROOM_MAP.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        ROOM_MAP.get(roomId).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String roomId = getParam(session,"roomId");
        for (WebSocketSession webSocketSession : ROOM_MAP.get(roomId)) {
            if (webSocketSession.isOpen() && !webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getParam(session,"roomId");
        if (ROOM_MAP.containsKey(roomId)) {
            ROOM_MAP.get(roomId).remove(session);
        }
    }

    /**
     * 获取请求参数
     * @param session   网络连接
     * @param key   关键词
     * @return
     */
    private String getParam(WebSocketSession session, String key) {
        URI uri = session.getUri();
        if (uri == null) {
            return null;
        }

        for (String param : uri.getQuery().split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals(key)) {
                return pair[1];
            }
        }

        return null;
    }
}
