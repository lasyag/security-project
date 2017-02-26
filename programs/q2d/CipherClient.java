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

public class CipherClient {
	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 9999;
        Socket s = new Socket(host, port);
		// YOU NEED TO DO THESE STEPS:
		// -Integrity
		
		KeyPairGenerator key_gen = KeyPairGenerator.getInstance("RSA");
		KeyPair key = key_gen.generateKeyPair();
		
		RSAPrivateKey AP =(RSAPrivateKey)key.getPrivate(); 
		// client gets  private key 
		
		Cipher integ =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integ.init(Cipher.ENCRYPT_MODE,AP);
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		byte em[]=integ.doFinal(message.getBytes()); //encrypts msg with private key
		out.writeObject(em);
		
		RSAPublicKey Apu =(RSAPublicKey)key.getPublic();
		
		ObjectOutputStream out1 = new ObjectOutputStream(s.getOutputStream());
		out1.writeObject(Apu);                 // sends clients public key
		System.out.println("original msg :"+message);
	    System.out.println("encrypted message sent");
		System.out.println(" encrypted message for integrity "+em);
		System.out.println(" sent public key"+Apu);
		
			
		//confidentiality
		
		ObjectInputStream in1 = new ObjectInputStream(s.getInputStream());
		RSAPublicKey sb=(RSAPublicKey)in1.readObject(); // gets servers public key 
		//Cipher integ1 =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		Cipher integ1 =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integ1.init(Cipher.ENCRYPT_MODE,sb);
		ObjectOutputStream out2 = new ObjectOutputStream(s.getOutputStream());
		byte em1[]=integ1.doFinal(message.getBytes()); //encrypts msg with public key
		out2.writeObject(em1);
		System.out.println(" encrypted msg for confidentiaity "+em1);
		
		//both
		 Cipher integ2 =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integ2.init(Cipher.ENCRYPT_MODE,AP);    //encrypts with its private key
		byte em2[]=integ2.doFinal(message.getBytes()); 
		 Cipher integ3 =Cipher.getInstance("RSA/ECB/PKCS1Padding");
		integ3.init(Cipher.ENCRYPT_MODE,sb);
		byte em3[]=integ3.doFinal(em2); 		
		ObjectOutputStream out3 = new ObjectOutputStream(s.getOutputStream());
		out3.writeObject(em3);
		System.out.println(" encrypted msg for confidentiaity +integrity  "+em3);
		ObjectOutputStream out4 = new ObjectOutputStream(s.getOutputStream());
		out4.writeObject(AP);
		System.out.println("\n private key "+AP);
		
		out.close();
	}
}
