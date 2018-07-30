package net.jahanco.jahandaru.Helpers;

import android.os.Environment;

import net.jahanco.jahandaru.App;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EHelper
{

  public static String decrypt(InputStream paramInputStream)
    throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(get_pass3().getBytes(), "AES");
    Cipher localCipher = Cipher.getInstance("AES");
    localCipher.init(2, localSecretKeySpec);
    return getStringFromInputStream(new CipherInputStream(paramInputStream, localCipher));
  }
  

  
  public static void encrypt()
    throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
  {
    FileInputStream localFileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/p2");
    Object localObject1 = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/p22");
    Object localObject2 = new SecretKeySpec(EHelper.get_pass2().getBytes(), "AES");
    Cipher localCipher = Cipher.getInstance("AES");
    localCipher.init(1, (Key)localObject2);
    localObject1 = new CipherOutputStream((OutputStream)localObject1, localCipher);
    localObject2 = new byte[8];
    for (;;)
    {
      int i = localFileInputStream.read((byte[])localObject2);
      if (i == -1) {
        break;
      }
      ((CipherOutputStream)localObject1).write((byte[])localObject2, 0, i);
    }
    ((CipherOutputStream)localObject1).flush();
    ((CipherOutputStream)localObject1).close();
    localFileInputStream.close();
  }
  public static void encrypt(String value)
          throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
  {
    FileInputStream localFileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/p2");
    Object localObject1 = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/p22");
    Object localObject2 = new SecretKeySpec(EHelper.get_pass2().getBytes(), "AES");
    Cipher localCipher = Cipher.getInstance("AES");
    localCipher.init(1, (Key)localObject2);
    localObject1 = new CipherOutputStream((OutputStream)localObject1, localCipher);
    localObject2 = new byte[8];
    for (;;)
    {
      int i = localFileInputStream.read((byte[])localObject2);
      if (i == -1) {
        break;
      }
      ((CipherOutputStream)localObject1).write((byte[])localObject2, 0, i);
    }
    ((CipherOutputStream)localObject1).flush();
    ((CipherOutputStream)localObject1).close();
    localFileInputStream.close();
  }
  private static String getStringFromInputStream(CipherInputStream is) {

    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();

    String line;
    try {

      br = new BufferedReader(new InputStreamReader(is));
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }
  public static String get_pass(){
    int[] array= App.imageBlur();
    String res="";
    for (int item :
            array) {
      char co=(char)item;
      res+=co;
    }
    return res;
  }
  public static String get_pass2(){
    int[] array= App.imageResize();
    String res="";
    for (int item :
            array) {
      char co=(char)item;
      res+=co;
    }
    return res;
  }
  public static String get_pass3(){
    int[] array= App.imageScale();
    String res="";
    for (int item :
            array) {
      char co=(char)item;
      res+=co;
    }
    return res;
  }



  public static SecretKey generateKey(String key)
          throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    return new SecretKeySpec(key.getBytes(), "AES");
  }

  public static byte[] encryptMsg(String message, SecretKey secret)
          throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
  {
   /* Encrypt the message. */
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] cipherText = cipher.doFinal(message.getBytes());
    return cipherText;
  }

  public static String decryptMsg(byte[] cipherText, SecretKey secret)
          throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
  {
    /* Decrypt the message, given derived encContentValues and initialization vector. */
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret);
    String decryptString = new String(cipher.doFinal(cipherText));
    return decryptString;
  }

}
