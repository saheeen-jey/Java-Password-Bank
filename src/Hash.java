

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//https://www.youtube.com/watch?v=hNKfEwTO3AQ
public class Hash {


	private final static byte[] salt = {-53,6,110,-66,-103,-105,-87,10,124,-96,67,-70,-13,-82,81,39};
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToStringHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length*2];
		for(int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j*2] = hexArray[v >>> 4];
			hexChars[j*2+1] = hexArray[v & 0x0F];
		}
		// TODO Auto-generated method stub
		return new String(hexChars);
	}
	
	public static byte[] createSalt() {
		byte[] bytes = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
	}
	
	//https://www.baeldung.com/java-password-hashing
	public static String hashPassword(char[] password) {
		//https://security.stackexchange.com/questions/172576/why-should-passwords-be-compared-by-means-of-a-byte-array
		byte[] hash = null;
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
		SecretKeyFactory factory = null;
		

		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			hash = factory.generateSecret(spec).getEncoded();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String hashedString = bytesToStringHex(hash);
		return hashedString;
	}
	
	public static void printSalt() {
		System.out.print("Hash Salt: ");
		
		for(int i=0; i < salt.length -1; i++) {
			System.out.print(salt[i] + ",");
		}
		System.out.print(salt[salt.length-1]);
	}
	
	
	
	
	
}
