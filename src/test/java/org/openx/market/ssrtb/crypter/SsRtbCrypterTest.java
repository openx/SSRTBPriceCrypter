// Copyright OpenX Limited 2010. All Rights Reserved.
package org.openx.market.ssrtb.crypter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Joel Meyer (joel.meyer@openx.org)
 */
public class SsRtbCrypterTest {
  private static final String HMAC_SHA1 = "HmacSHA1";

  public static final byte[] encryptionKeyBytes = {
      (byte) 0xb0, (byte) 0x8c, (byte) 0x70, (byte) 0xcf, (byte) 0xbc,
      (byte) 0xb0, (byte) 0xeb, (byte) 0x6c, (byte) 0xab, (byte) 0x7e,
      (byte) 0x82, (byte) 0xc6, (byte) 0xb7, (byte) 0x5d, (byte) 0xa5,
      (byte) 0x20, (byte) 0x72, (byte) 0xae, (byte) 0x62, (byte) 0xb2,
      (byte) 0xbf, (byte) 0x4b, (byte) 0x99, (byte) 0x0b, (byte) 0xb8,
      (byte) 0x0a, (byte) 0x48, (byte) 0xd8, (byte) 0x14, (byte) 0x1e,
      (byte) 0xec, (byte) 0x07
  };

  public static final byte[] integrityKeyBytes = {
      (byte) 0xbf, (byte) 0x77, (byte) 0xec, (byte) 0x55, (byte) 0xc3,
      (byte) 0x01, (byte) 0x30, (byte) 0xc1, (byte) 0xd8, (byte) 0xcd,
      (byte) 0x18, (byte) 0x62, (byte) 0xed, (byte) 0x2a, (byte) 0x4c,
      (byte) 0xd2, (byte) 0xc7, (byte) 0x6a, (byte) 0xc3, (byte) 0x3b,
      (byte) 0xc0, (byte) 0xc4, (byte) 0xce, (byte) 0x8a, (byte) 0x3d,
      (byte) 0x3b, (byte) 0xbd, (byte) 0x3a, (byte) 0xd5, (byte) 0x68,
      (byte) 0x77, (byte) 0x92
  };

  protected SecretKey encryptKey;
  protected SecretKey integrityKey;

  @Before
  public void setup() {
    encryptKey = new SecretKeySpec(encryptionKeyBytes, HMAC_SHA1);
    integrityKey = new SecretKeySpec(integrityKeyBytes, HMAC_SHA1);
  }

  @Test
  public void testEncrypts() throws Exception {
    SsRtbCrypter crypter = new SsRtbCrypter();

    long value = 709959680;

    String ciphered = crypter.encryptEncode(value, encryptKey, integrityKey);
    long unciphered = crypter.decodeDecrypt(ciphered, encryptKey, integrityKey);

    Assert.assertEquals(value, unciphered);
  }

  @Test
  public void testDecrypts() throws Exception {
    Assert.assertEquals(709959680, new SsRtbCrypter().decodeDecrypt("SjpvRwAB4kB7jEpgW5IA8p73ew9ic6VZpFsPnA==", encryptKey, integrityKey));
  }
}
