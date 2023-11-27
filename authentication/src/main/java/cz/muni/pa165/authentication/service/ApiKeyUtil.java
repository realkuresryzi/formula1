package cz.muni.pa165.authentication.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Util for en/decrypting.
 */
public class ApiKeyUtil {

  private static final String ALGORITHM = "AES";
  private static final String SECRET_KEY = "MySuperSecretKey";

  /**
   * Encrypt api key in database using AES.
   *
   * @param apiKey api key value to be encrypted
   * @return encrypted api key
   */
  public static String encryptApiKey(String apiKey) {
    try {
      SecretKey secretKey = generateSecretKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] encryptedBytes = cipher.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Decrypt api key in database using AES.
   *
   * @param encryptedApiKey api key value to be decrypted
   * @return encrypted api key
   */
  public static String decryptApiKey(String encryptedApiKey) {
    try {
      SecretKey secretKey = generateSecretKey();
      Cipher cipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedApiKey);
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static SecretKey generateSecretKey() {
    return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  }

  /**
   * Generates api key from session token.
   *
   * @param input session token value
   * @return generated api key.
   */
  public static String generateApiKey(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }
}
