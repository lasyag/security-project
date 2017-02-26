import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import java.util.Base64;

public class CipherClient
{
	private static Cipher ecipher;
	private static Key key;

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
	String host = "localhost";
		int port = 9999;
		Socket s = new Socket(host, port);

		key = KeyGenerator.getInstance("DES").generateKey();
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject(key);
		ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		ecipher.init(Cipher.ENCRYPT_MODE, key);

		
		
		//System.out.print("Enter message: ");
		byte[] encrypted = encrypt(message);
		
		ObjectOutputStream out1 = new ObjectOutputStream(s.getOutputStream());
		out1.writeObject(encrypted);    
		System.out.print("sent encrypted mesage  "+encrypted);
			}

	public static byte[] encrypt(String str) {

		try {
			
			byte[] utf8 = str.getBytes();
			byte[] enc = ecipher.doFinal(utf8);
			//enc = Base64.getEncoder().encode(enc);
			return enc;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
