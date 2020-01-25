package com.zpffly.crush.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.List;

/**
 * 两次MD5加密的目的
 *
 * 加盐的md5会比直接md5的难度大很多，因为md5是不可逆的。
 * 所谓破解都只是暴力破解，暴力破解会从常用密码开始破解，加盐会使得暴力破解难度变大。
 *
 * 1 明文密码不能在网络上传输
 * 2 没有第二次加密的话，黑客通过解析前端js文件，可以知道md5加密的过程，如果黑客能够黑掉用户数据库，就知道破解用户的密码。
 *   但是此时我们要是再次加入随机盐值和传输来的密码的md5组合，黑客是虽然能看到随机盐值，
 *   但是它无法得知随机盐和传输来的密码的再次加盐组合方式，从而无法知道密码。
 *
 */
public class MD5Util {

    //客户端固定Salt
    //随便写，只要resources/static/js/common.js中的g_passsword_salt一致即可
    private static final String salt = "zpfflylovehsy";

    public static String md5(String password) {
        return DigestUtils.md5Hex(password);
    }

    //客户端固定加盐
    public static String clientEncoding(String password) {
        String str = salt + password;//加盐
        return md5(str);
    }

    //服务端随机加盐
    public static String serverEncoding(String clientCode, String salt) {
        //服务器端随机加盐
        String str = salt + clientCode;
        return md5(str);
    }

    //明文密码--->客户端密码
    public static String password2ServerEncoding(String password, String salt) {
        String clientCode = clientEncoding(password);
        String serverCode = serverEncoding(clientCode, salt);
        return serverCode;
    }

    public static void main(String[] args) {
        String password = "123456";

        String clientCode = password2ServerEncoding(password, "aaa");
        System.out.println(clientCode);// febba268158a9269633162fbc0c95054
//        System.out.println("febba268158a9269633162fbc0c95054".equals("febba268158a9269633162fbc0c95054"));
        List<Integer> a = Arrays.asList(1, 3,4);
    }

}
