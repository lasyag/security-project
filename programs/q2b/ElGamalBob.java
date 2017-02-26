import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		BigInteger m = new BigInteger(message.getBytes());
		BigInteger y_ = y.modPow(a,p);
		BigInteger a_ = a.modPow(b,p);
		BigInteger ya = y_.multiply(a_);
		BigInteger lhs = ya.mod(p);
		BigInteger rhs = g.modPow(m,p);

		if(lhs.compareTo(rhs)==0)
			return true;
		return false;
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 9099;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}