import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class client {
public static void main(String a[]) throws NoSuchAlgorithmException, NoSuchPaddingException, UnknownHostException, IOException, CertificateException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
	String host = "localhost";
	int port = 2999;
	Socket s = new Socket(host, port);
    
	ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
    InputStream inStream = new FileInputStream("server.crt");
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
    inStream.close();
    //Read the certificate file
    
    System.out.println("The detail of the certificate file is:");
    System.out.println(cert.toString());
    //Print the contents of the certificate

    Date date = cert.getNotAfter();
    if(date.after(new Date()))
    {
    	System.out.println("The certificate is current.");//expiration date
    	System.out.println("It is valid from "+ cert.getNotBefore()+ " to "+cert.getNotAfter()) ;
    }
    else
    {     
    	System.out.println("The certificate is expired.");
    }

    try
    {
	   cert.checkValidity();//Check server's signature
	   System.out.println("The certificate is valid.");
    } catch (Exception e){
	   System.out.println(e);   
    }
    
    System.out.println("Please input a string:");
    Scanner input = new Scanner(System.in);
    String message= input.nextLine();
    
    RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();
    //Retrieve the public key from the certificate
    
    //Encrypt : server's public key 
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, eServer);
    byte[] cipherText = cipher.doFinal(message.getBytes());
    System.out.println("The ciphertext is: " + cipherText);
    os.writeObject(cipherText);
	os.flush();
	os.close();
    s.close();
    input.close();
}
}
