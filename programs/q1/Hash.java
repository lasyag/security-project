import java.security.MessageDigest;
import javax.crypto.*;
import java.io.*;
import java.util.Base64;

public class Hash {

	public static void main(String[] args) throws Exception {
		String password = "123456";

        MessageDigest md = MessageDigest.getInstance("SHA");
        MessageDigest md1 = MessageDigest.getInstance("MD5");

        md.update(password.getBytes());
        md1.update(password.getBytes());


        byte byteData[] = md.digest();
        byte byteDatam[]= md1.digest();
        
		//convert the byte to hex format method 1(SHA)
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Digest of SHA(in hex format):: " + sb.toString());
         
		 //convert the byte to hex format method 2(MD5)
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < byteDatam.length; i++) {
         sb1.append(Integer.toString((byteDatam[i] & 0xff) + 0x100, 16).substring(1));
        }

        //System.out.println("Digest(in hex format):: " + sb.toString());

        
    	System.out.println("Digest of MD5(in hex format):: " + sb1.toString());
	}
}
