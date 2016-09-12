package com.zheng0716.restful_demo.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.*;

/**
 * 加密.
 * Created by zhengxiaoyao0716 on 2016/9/11.
 */
public final class SecurityUtil {
    private final static SecurityUtil instance = new SecurityUtil("Secret key for Restful Demo, gen@2016/9/11");

    public static SecurityUtil getInstance(String accesskey) {
        return instance;
    }

    private final String key;

    private SecurityUtil(String key) {
        this.key = key;
    }

    /**
     * 验证.
     *
     * @param content 签名后的Json
     * @return 校验成败
     */
    public boolean verify(Map<String, Object> content) {
        String sign = (String) content.get("_sign");
        return sign != null && sign.equals(hmacSha256Hex(sortMap(content)));
    }

    /**
     * 签名.
     *
     * @param content 要签名的Json
     * @return 签名后的Json
     */
    public Map<String, Object> signed(Map<String, Object> content) {
        String sign = hmacSha256Hex(sortMap(content));
        content.put("_sign", sign);
        return content;
    }

    private String sortMap(Map<String, Object> content) {
        List<String> keyList = new ArrayList<>(content.keySet());

        Collections.sort(keyList, new Comparator<String>() {
            private Collator collator = Collator.getInstance(Locale.CHINA);

            @Override
            public int compare(String s, String t1) {
                return collator.compare(s, t1);
            }
        });

        String sortedValues = "";
        for (String key : keyList) {
            if (key.charAt(0) == '_') {
                continue;
            }
            Object value = content.get(key);
            // 防止嵌套无序map
            if (value instanceof Map) {
                //noinspection unchecked
                sortedValues += sortMap((Map<String, Object>) value);
            } else if (value != null) {
                sortedValues += value.toString();
            }
        }
        return sortedValues;
    }

    /**
     * HMAC-SHA256算法加密再转化小写形式的十六进制字符串.
     *
     * @param message 需要加密的信息
     * @return 加密后的密文
     */
    public String hmacSha256Hex(String message) {
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (mac == null) return null;
        try {
            mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        char[] hexChars;
        try {
            hexChars = encodeHex(mac.doFinal(message.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return new String(hexChars);
    }

    /**
     * 编码成小写形式十六进制字符串.
     *
     * @param data 要编码的数据
     * @return 编码后的结果
     */
    private char[] encodeHex(final byte[] data) {
        char[] toDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
}