package Encryption;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionHelper {

	private static String BOUNCY_CASTLE_PROVIDER_IDENTIFIER = "BC";	
	private Cipher cipher;
	
	byte[] keyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
	private SecretKey key = new SecretKeySpec(keyBytes, "AES");
	private static final int IV_SIZE = 16; // Size of the IV for AES
	
	public EncryptionHelper() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BOUNCY_CASTLE_PROVIDER_IDENTIFIER);		
	}
	
	public byte[] encrypt(byte[] plainText) throws Exception {
        byte[] initializationVector = new byte[IV_SIZE];
        new SecureRandom().nextBytes(initializationVector); // Generate random IV

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
        byte[] cipherText = cipher.doFinal(plainText);

        // Combine IV and cipherText for storage
        byte[] combined = new byte[initializationVector.length + cipherText.length];
        System.arraycopy(initializationVector, 0, combined, 0, initializationVector.length);
        System.arraycopy(cipherText, 0, combined, initializationVector.length, cipherText.length);

        return combined; // Store this combined array
    }
	public byte[] decrypt(byte[] combinedCipherText) throws Exception {
	    // Check for null or empty input
	    if (combinedCipherText == null || combinedCipherText.length == 0) {
	        throw new IllegalArgumentException("Combined cipher text cannot be null or empty");
	    }

	    // Ensure the combinedCipherText is long enough to contain the IV
	    if (combinedCipherText.length < IV_SIZE) {
	        throw new IllegalArgumentException("Combined cipher text is too short to contain IV");
	    }

	    // Extract IV
	    byte[] initializationVector = Arrays.copyOf(combinedCipherText, IV_SIZE);
	    byte[] cipherText = Arrays.copyOfRange(combinedCipherText, IV_SIZE, combinedCipherText.length);
	    
	    // Initialize cipher for decryption
	    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));

	    // Perform decryption
	    return cipher.doFinal(cipherText);
	}
}