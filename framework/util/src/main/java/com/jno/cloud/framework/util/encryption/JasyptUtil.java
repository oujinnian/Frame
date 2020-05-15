package com.jno.cloud.framework.util.encryption;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Jasypt加密解密工具类
 *
 */
@Slf4j
public class JasyptUtil {

    /**
     * 定义使用的算法为:PBEWITHMD5andDES算法
     */
    public static final String ALGORITHM = "PBEWITHMD5ANDDES";

    /**
     * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES<测试的时候报错>
     * PBEWITHSHAANDDESEDE<测试的时候报错> PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1<测试的时候报错>
     * */

    /**
     * 本地测试通过：<code>PBEWITHMD5ANDDES</code>,<code>PBEWITHSHA1ANDRC2_40</code>
     */
    /**
     * 定义迭代次数为1000次,次数越多，运算越大，越不容易破解之类。
     */
    private static final int ITERATIONCOUNT = 190;//origin use 1000
    @Autowired
    static StringEncryptor stringEncryptor;
    private  String saltStr;

    public static void main(String[] args) throws Exception {

        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("jno");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("root");
        System.out.println("username:"+username);
        System.out.println("password:"+password);





    }

    /**
     * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节的倍数
     *
     * @return byte[] 盐值
     * */
    private  byte[] getSalt() throws Exception {
        // 实例化安全随机数  // 产出盐
        SecureRandom random = new SecureRandom();
        return random.generateSeed(32);
    }

    /**
     * 根据PBE密码生成一把密钥
     *
     * @param password
     *            生成密钥时所使用的密码
     * @return Key PBE算法密钥
     * */

    private Key getPBEKey(String password) throws Exception {
        // 实例化使用的算法
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        // 设置PBE密钥参数
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        // 生成密钥
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        return secretKey;
    }

    /**
     * 加密明文字符串
     *
     * @param plaintext
     *            待加密的明文字符串
     * @param password
     *            生成密钥时所使用的密码
     *            盐值
     * @return 加密后的密文字符串
     * @throws Exception
     */
    public  String encrypt(String plaintext, String password)
            throws Exception {

        Key key = getPBEKey(password);
        byte[] salt = getSalt();
        saltStr = new Base64().encodeToString(salt);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,ITERATIONCOUNT);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte encipheredData[] = cipher.doFinal(plaintext.getBytes("UTF-8"));

        return new Base64().encodeToString(encipheredData);
    }

    public String getSaltStr(){
        return saltStr;
    }

    /**
     * 解密密文字符串
     *
     * @param ciphertext
     *            待解密的密文字符串
     * @param password
     *            生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
     *            盐值(如需解密,该参数需要与加密时使用的一致)
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public  String decrypt(String ciphertext, String password, String saltStr)
            throws Exception {

        Key key = getPBEKey(password);
        byte[] salt = new Base64().decode(saltStr);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
                ITERATIONCOUNT);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] passDec = cipher.doFinal(new Base64().decode(ciphertext));
        return new String(passDec);
    }
}