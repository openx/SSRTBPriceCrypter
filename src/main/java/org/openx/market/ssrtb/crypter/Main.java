// Copyright OpenX Limited 2010. All Rights Reserved.
package org.openx.market.ssrtb.crypter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Main {
  
  private static enum Op {
    Exit,
    Encrypt,
    Decrypt
  }

  protected static BufferedReader in = null;

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    in = new BufferedReader(new InputStreamReader(System.in));

    SecretKeySpec encryption = getKeySpec("Please enter your encryption key: ");
    SecretKeySpec integrity  = getKeySpec("Please enter your integrity key:  ");

    SsRtbCrypter crypter = new SsRtbCrypter();

    Op op = Op.Exit;
    while ((op = getOperation()) != Op.Exit) {
      long price = -1;

      try {
        switch (op) {
        case Encrypt:
          System.out.print("Please enter the price (in micros) to encrypt: ");
          String priceStr = in.readLine();
          price = Long.parseLong(priceStr);
          System.out.println("Encrypted: " + crypter.encryptEncode(price, encryption, integrity));
          break;
        case Decrypt:
          System.out.print("Please enter the encrypted string: ");
          String encStr = in.readLine();
          price = crypter.decodeDecrypt(encStr, encryption, integrity);
          System.out.println("Price: " + price);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.out.println("Exiting");
  }

  protected static SecretKeySpec getKeySpec(String prompt) throws Exception {
    System.out.print(prompt);
    String keyStr = in.readLine();

    byte[] keyBytes = null;
    if (keyStr.length() == 44) {
      keyBytes = Base64.decodeBase64(keyStr.getBytes("US-ASCII"));
    } else if (keyStr.length() == 64) {
      keyBytes = Hex.decodeHex(keyStr.toCharArray());
    }

    return new SecretKeySpec(keyBytes, "HmacSHA1");
  }

  protected static Op getOperation() throws Exception {
    String opStr = null;

    StringBuilder sb = new StringBuilder();
    sb.append("Enter");
    for (Op val : Op.values()) {
      sb.append(" ").append(val.ordinal()).append(") to ").append(val.name());
    }
    sb.append(": ");
    String options = sb.toString();

    System.out.print(options);
    while ((opStr = in.readLine()) != null) {
      try {
        int opInt = Integer.parseInt(opStr);
        for (Op val : Op.values()) {
          if (opInt == val.ordinal()) return val;
        }
      } catch (NumberFormatException nfe) {
      }
      System.out.print(options);
    }

    return Op.Exit;
  }
}
