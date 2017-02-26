import java.io.FileInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Server {
	public static void main(String a[]) throws InvalidKeyException, ClassNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String aliasname="client";
        char[] password="finalkeystore".toCharArray();
		
        int port = 2999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		//Read the keystore and retrieve the server's private key
	    
		
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("clientkeystore"), password);
        PrivateKey dServer = (PrivateKey)ks.getKey(aliasname,"finalkeystore".toCharArray());
       
        //Decrypt: server's private key 
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] in = (byte[]) is.readObject();
		cipher.init(Cipher.DECRYPT_MODE, dServer);
		byte[] plaintText = cipher.doFinal(in);
		System.out.println("The plaintext is: " + new String(plaintText));
		server.close();
		
        
	}

}
