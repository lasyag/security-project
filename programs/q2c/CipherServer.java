import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import java.util.Base64;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CipherServer
{
	private static Cipher dcipher;


	public static void main(String[] args) throws Exception 
	{
		int port = 9999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();

		ObjectInputStream in = new ObjectInputStream(s.getInputStream());
		 Key key = (Key)in.readObject();
		ObjectInputStream in2 = new ObjectInputStream(s.getInputStream());
		byte[] em= (byte[])in2.readObject();

		dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		dcipher.init(Cipher.DECRYPT_MODE, key);

		String decrypted = decrypt(em);
		System.out.println(decrypted);
	}

	public static String decrypt(byte[] str) {

		try {
			
			byte[] utf8 = dcipher.doFinal(str);
			return new String(utf8);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
