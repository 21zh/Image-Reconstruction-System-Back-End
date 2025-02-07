package com.mxch.imgreconsturct.util;

import com.mxch.imgreconsturct.pojo.User;
import freemarker.template.utility.StringUtil;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "mxch";

    // 生成token字符串
    public static String createToken(User user) {
        String token = Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("user",user)
                .signWith(SignatureAlgorithm.HS512,tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    // 从token中获取用户
    public static User getUser(String token){
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        User user = (User) claims.get(token);
        return user;
    }

    // 判断token是否过期
    public static boolean isExpiration(String token) {
        try {
            boolean isExpire = Jwts.parser().setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
            // 没有过期，返回false
            return isExpire;
        }catch (Exception e){
            // 出现异常，返回true
            return true;
        }
    }

    // 刷新token
    public String refreshToken(String token) {
        String refreshToken;
        try {
            final Claims claims  = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody();
            refreshToken = JwtHelper.createToken(getUser(token));
        }catch (Exception e){
            refreshToken = null;
        }
        return refreshToken;
    }


}
