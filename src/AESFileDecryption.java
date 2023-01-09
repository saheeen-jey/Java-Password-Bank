


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
//https://javapapers.com/java/java-file-encryption-decryption-using-aes-password-based-encryption-pbe/

public class AESFileDecryption {
	public static void decrypt(char[] password, String username, String fileExtension) throws Exception {

		if(!Util.fileExists(username + fileExtension + "encryptedfile.des"))
			return;
		
		// reading the salt
		// user should have secure mechanism to transfer the
		// salt, iv and password to the recipient
		FileInputStream saltFis = new FileInputStream("salt" + username + fileExtension + ".enc");
		byte[] salt = new byte[8];
		saltFis.read(salt);
		saltFis.close();

		// reading the iv
		FileInputStream ivFis = new FileInputStream("iv" + username + fileExtension + ".enc");
		byte[] iv = new byte[16];
		ivFis.read(iv);
		ivFis.close();

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(password, salt, 65536,
				128);
		SecretKey tmp = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		// file decryption
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		FileInputStream fis = new FileInputStream(username + fileExtension + "encryptedfile.des");
		
		FileOutputStream fos = new FileOutputStream(username + fileExtension +".txt");
		byte[] in = new byte[64];
		int read;
		while ((read = fis.read(in)) != -1) {
			byte[] output = cipher.update(in, 0, read);
			if (output != null)
				fos.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null)
			fos.write(output);
		fis.close();
		fos.flush();
		fos.close();
		Util.deleteFile(username + fileExtension +"encryptedfile.des");	
		//https://blog.dbi-services.com/java-cannot-delete-file/
		System.gc();
		Util.deleteFile(username + fileExtension +"encryptedfile.des");
		System.out.println("File Decrypted.");
	}
}