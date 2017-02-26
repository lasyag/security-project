import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Arrays;
import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javax.crypto.CipherInputStream;

public class CipherServer {
	public static void main(String[] args) throws Exception {
		int port = 9999;
		ServerSocket s = new ServerSocket(port);
	     Socket  soc = s.accept();
//integrity	

	ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
		byte[] em = (byte[]) in.readObject(); //reads cipher text
		System.out.println(em);
		ObjectInputStream in2 = new ObjectInputStream(soc.getInputStream());
		RSAPublicKey cc = (RSAPublicKey) in2.readObject(); //gets client public key
		System.out.println(em);
		KeyPairGenerator keys = KeyPairGenerator.getInstance("RSA");
		Cipher integs =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integs.init(Cipher.DECRYPT_MODE,cc);                 //decrypts with the clients public key
		 byte[] dmsg = integs.doFinal(em);
		 String msg=new String(dmsg);
		 System.out.println(" original message in integrity : \n"+ msg);

		 
		 //confidentiality		
		KeyPair rsakeys = keys.generateKeyPair();
		RSAPublicKey BPu =(RSAPublicKey)rsakeys.getPublic();
		ObjectOutputStream in3 = new ObjectOutputStream(soc.getOutputStream());
		in3.writeObject(BPu);
		ObjectInputStream in4 = new ObjectInputStream(soc.getInputStream());
		byte[] em2 = (byte[]) in4.readObject();   //gets the ciphertext
			RSAPrivateKey BPr=(RSAPrivateKey)rsakeys.getPrivate();
		Cipher integs1=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integs1.init(Cipher.DECRYPT_MODE,BPr);  //decrypts with privatekey
		 byte[] dmsg1 = integs1.doFinal(em2);
		 String msg1=new String(dmsg1);
		 System.out.println(" original message in confidentiality : \n"+ msg1);
		 
		 //both
		 ObjectInputStream in5 = new ObjectInputStream(soc.getInputStream());
		byte[] em3 = (byte[]) in4.readObject(); 
		 Cipher integs2=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integs2.init(Cipher.DECRYPT_MODE,cc);    // decrypting with its public key
		byte[] dmsg2 = integs1.doFinal(em3);
		ObjectInputStream in6 = new ObjectInputStream(soc.getInputStream());
		RSAPrivateKey sp=(RSAPrivateKey)in6.readObject(); //reading alice private key
		 Cipher integs3=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integs3.init(Cipher.DECRYPT_MODE,sp); 
		byte[] dmsg3 = integs1.doFinal(dmsg2); //decrypting with private key
		String msg2=new String(dmsg3);
		 System.out.println(" original message in confidentiality+integrity : \n"+ msg2);
		
		
		
	}
		
}
