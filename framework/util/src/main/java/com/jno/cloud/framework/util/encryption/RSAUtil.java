package com.jno.cloud.framework.util.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RAS加密工具类
 * 公开密钥密码体制，即是非对称加密算法
 */
public class RSAUtil {

    public static final String CHARSET = "UTF-8";
    //非对称密钥算法
    public static final String RSA_ALGORITHM = "RSA";
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";
    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    public static Map<String, String> createKeys(int keySize){
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try{
            //实例化密钥对生成器
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("找不到这个加密算法-->[" + RSA_ALGORITHM + "]");
        }
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put(PUBLIC_KEY, publicKeyStr);
        keyPairMap.put(PRIVATE_KEY, privateKeyStr);
        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     * @param contentBytes 待加密数据
     * @param key 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublickey(byte[] contentBytes, byte[] key) throws Exception{
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(contentBytes);
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublickey(String data, String publicKey) throws Exception {
        //得到公钥
        RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
        Cipher ci = Cipher.getInstance(RSA_ALGORITHM);
        ci.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        byte[] bytes = data.getBytes();
        int inputLen = bytes.length;
        int offLen = 0;//偏移量
        int i = 0;

//        byte[] bb = rsaSplitCodec(ci,Cipher.ENCRYPT_MODE,Base64.decodeBase64(data),rsaPublicKey.getModulus().bitLength());

        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte [] cache;
            if(inputLen - offLen > 117){
                cache = ci.doFinal(bytes, offLen,117);
            }else{
                cache = ci.doFinal(bytes, offLen,inputLen - offLen);
            }
            bops.write(cache);
            i++;
            offLen = 117 * i;
        }
        bops.close();
        byte[] encryptedData = bops.toByteArray();
        String encodeToString = Base64.encodeBase64URLSafeString(encryptedData);
        return encodeToString;
    }

    /**
     * 私钥解密
     * @param contentBytes 待解密数据
     * @param key 私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] contentBytes, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(contentBytes);
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        // 得到私钥
        RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
        Cipher ci = Cipher.getInstance(RSA_ALGORITHM);
        ci.init(Cipher.DECRYPT_MODE,rsaPrivateKey);

        byte[]  bytes = Base64.decodeBase64(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new String(byteArray,CHARSET);
    }

    public static void main (String[] args) throws Exception {
        Map<String, String> keyMap = RSAUtil.createKeys(1024);
        String  publicKey = keyMap.get(RSAUtil.PUBLIC_KEY);
        String  privateKey = keyMap.get(RSAUtil.PRIVATE_KEY);
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "admin";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RSAUtil.encryptByPublickey(str, publicKey);
        System.out.println("密文：\r\n" + encodedData);
        String decodedData = RSAUtil.decryptByPrivateKey(encodedData, privateKey);
        System.out.println("解密后文字: \r\n" + decodedData);

    }


}