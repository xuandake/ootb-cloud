package com.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;


/**
 * @ClassName RSAUtil
 * @Description 实现RSA加密工具类
 * @Date 20-07-14 9:20
 **/

@Slf4j
public class RSAUtil {

    /** 算法名称 */
    private static final String ALGORITHM = "RSA";

    /** RSA签名算法 */
    private static final String RSA_SIGNATURE_ALGORITHM = "SHA256WithRSA";

    /** 默认秘钥大小*/
    private static final int KEY_SIZE = 2048;

    /** 最大解密长度 */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /** 用来指定保存秘钥对的文件名和存储的名称*/
    private static final String PUBLIC_KEY_NAME = "publicKey";
    private static final String PRIVATE_KEY_NAME = "privateKey";
    private static final String PUBLIC_FILENAME = "public.properties";
    private static final String PRIVATE_FILENAME = "private.properties";
    private static Properties pubProperties;
    private static Properties priProperties;

    /** 秘钥对生成器 */
    private static KeyPairGenerator keyPairGenerator = null;
    private static KeyFactory keyFactory = null;

    /** 缓存的秘钥对 */
    private static KeyPair keyPair = null;

    /** Base64 编码/解码器 JDK1.8 */
    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Base64.Encoder encoder = Base64.getEncoder();

    /** 初始化秘钥工厂 */
    static {
        try{
           keyPairGenerator =  KeyPairGenerator.getInstance(ALGORITHM);
           keyFactory = KeyFactory.getInstance(ALGORITHM);

           getInstanceForPub();
           getInstanceForPri();
        }catch (NoSuchAlgorithmException | IOException e){
            //log.info(e.getMessage(), e);
        }
    }

    /** 初始化公钥config */
    private static Properties getInstanceForPub() throws IOException {
        if(pubProperties == null){
            Resource res = new ClassPathResource(PUBLIC_FILENAME);
            pubProperties = new Properties();
            pubProperties.load(res.getInputStream());
        }
        return pubProperties;
    }

    /** 初始化私钥config */
    private static Properties getInstanceForPri() throws IOException {
        if(priProperties == null){
            Resource resource = new ClassPathResource(PRIVATE_FILENAME);
            priProperties = new Properties();
            priProperties.load(resource.getInputStream());
        }
        return priProperties;
    }

    private RSAUtil(){}

    /**
     * @Description 将指定的秘钥字符串保存到文件，文件不存在创建
     * @Param [keyString(秘钥的Base64编码字符串), keyName(保存在文件中的名称), properties目标文件, fileName文件名称]
     * @return void
     **/
    private static void storeKey(String keyString, String keyName, Properties properties, String fileName){
        try{
            Resource resource = new ClassPathResource(fileName);
            FileOutputStream oFile = new FileOutputStream(resource.getFile(), false);
            properties.setProperty(keyName, keyString);
            properties.store(oFile, keyName);
            oFile.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @Description 获取秘钥字符串
     * @Param [keyName(需要获取的秘钥名), properties(秘钥文件)]
     * @return java.lang.String
     **/
    private static String getKeyString(String keyName, Properties properties){
        return properties.getProperty(keyName);
    }

    /**
     * 生成秘钥对
     * 将秘钥分别用Base64编码保存到public.properties和private.properties文件中
     * 保存的默认名称分别为publicKey和privateKey
     * */
    public static synchronized void generateKeyPair(){
        try{
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            keyPair = keyPairGenerator.generateKeyPair();
        }catch (InvalidParameterException e){
            throw e;
        }catch (NullPointerException e){
            throw e;
        }

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyString = encoder.encodeToString(rsaPublicKey.getEncoded());
        String privateKeyString = encoder.encodeToString(rsaPrivateKey.getEncoded());

        System.out.println("公钥：" + publicKeyString);
        System.out.println("私钥：" + privateKeyString);

        storeKey(publicKeyString, PUBLIC_KEY_NAME, pubProperties, PUBLIC_KEY_NAME);
        storeKey(privateKeyString, PRIVATE_KEY_NAME, priProperties, PRIVATE_KEY_NAME);
    }

    /**
     * @Description 从文件中取出公钥
     * @return java.security.interfaces.RSAPublicKey
     **/
    public static RSAPublicKey getPublicKey(){
        try{
            byte[] keyBytes = decoder.decode(getKeyString(PUBLIC_KEY_NAME, pubProperties));
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            return rsaPublicKey;
        }catch (InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description //从文件中获取私钥
     * @return java.security.interfaces.RSAPrivateKey
     **/
    public static RSAPrivateKey getPrivateKey()   {
        try{
            byte[] keyBytes = decoder.decode(getKeyString(PRIVATE_KEY_NAME, priProperties));
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        }catch (InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description //RSA公钥加密
     * @Param [data]
     * @return byte[]
     **/
    public static byte[] encryptByPublicKey(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        //对数据分段加密
        while (inputLen - offSet  > 0){
            if(inputLen - offSet > 177){
                cache = cipher.doFinal(data, offSet, 177);
            }else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i*177;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * @Description //私钥解密
     * @Param [encryptedData]
     * @return byte[]
     **/
    public static byte[] decryptByPrivateKey(byte[] encryptedData) throws Exception{
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(getPrivateKey().getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;

        //对数据分段解密
        while (inputLen - offSet > 0){
            if(inputLen - offSet > MAX_DECRYPT_BLOCK){
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            }else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i*MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * @Description //公钥验签
     * @Param data 代签字符串 sign公钥 base64编码
     * @return 验签结果
     **/
    public static boolean verify(byte[] data, String sign) throws Exception{
        Signature signature = Signature.getInstance(RSA_SIGNATURE_ALGORITHM);
        signature.initVerify(getPublicKey());
        signature.update(data);
        return signature.verify(Base64Utils.decodeFromUrlSafeString(sign));
    }

    /**
     * @Description //私钥签名： 签名方式SHA1WithRSA
     * @Param [data] 待签名byte[]
     * @return byte[]
     **/
    public static byte[] sign(byte[] data) throws Exception{
        Signature signature = Signature.getInstance(RSA_SIGNATURE_ALGORITHM);
        signature.initSign(getPrivateKey());
        signature.update(data);
        return signature.sign();
    }

    //将char转byte类型
    private static byte toByte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    public static int getValidLength(byte[] bytes){
        int i = 0;
        if(null == bytes || 0 == bytes.length)
            return i;
        for (; i < bytes.length; i++){
            if(bytes[i] == '\0')
                break;;
        }
        return i + 1;
    }

    public static KeyPair getKeyPair(){
        return new KeyPair(getPublicKey(), getPrivateKey());
    }
}
