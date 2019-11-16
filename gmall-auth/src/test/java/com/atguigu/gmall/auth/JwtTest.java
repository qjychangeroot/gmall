package com.atguigu.gmall.auth;

import com.atguigu.core.utils.JwtUtils;
import com.atguigu.core.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTest {
	private static final String pubKeyPath = "C:\\0615javaee\\javaeestudy\\workspace_idea01\\tmp\\rsa.pub";

    private static final String priKeyPath = "C:\\0615javaee\\javaeestudy\\workspace_idea01\\tmp\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "hasdfkjJHJKKjsahdjfk123789^*&");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "11");
        map.put("username", "liuyan");
        // 生成token
        String token = JwtUtils.generateToken(map, privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE1NzM3MTE0Nzd9.Ouo6oCHI6lP47-TzPawn6WoO3JznDjtLHmdo_RbLfvIX1_3XkgukR82slPECWbmuZ1VJfkwshfxoerQOzbMawwOvz3qXuTX_4YtIo31-Lu_Uxl5Kj4G_sUtGXfK3ApEZOqmLaM5sVNZgEmAy9md09woxjS4_ytNT2ZF2QDbd6Fh--hzFVJzO5lxXzMj15PkctgSoEAYfxWSPPz8LeWR3Ayswwzo2zq85kIHKmnAkvpvodir4PVUqKOybnTMVha98PI66pPDrKWHC790IYkPEt_QTxkNHSsfQ1eNHTOR5CDNNKQqYML9R5fa0y-lUffECkUlXDUfm_qc_tkr25OccYg";

        // 解析token
        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + map.get("id"));
        System.out.println("userName: " + map.get("username"));
    }
}