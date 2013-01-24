package net.unit8.sastruts.oauth.provider.util;

import org.seasar.framework.util.Base64Util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created with IntelliJ IDEA.
 * User: uu034251
 * Date: 13/01/07
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class RandomUtil {
    public static String generateKeys(int size) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] keys = new byte[size];
            random.nextBytes(keys);
            String encodedKeys = Base64Util.encode(keys).replaceAll("\\W", "");
            return encodedKeys;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
