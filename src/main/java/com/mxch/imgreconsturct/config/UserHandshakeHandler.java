package com.mxch.imgreconsturct.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Map;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 获取用户id
        String query = request.getURI().getQuery();
        String userId = null;
        if(query != null){
            for(String param : query.split("&")){
                String[] kv = param.split("=");
                if(kv.length == 2 && "userId".equals(kv[0])){
                    try {
                        userId = URLDecoder.decode(kv[1], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
        final String finalUserId = userId;

        return () -> finalUserId;
    }
}
