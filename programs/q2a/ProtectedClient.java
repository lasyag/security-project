import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		Random rn = new Random();
		double num1 = rn.nextDouble();
		long time1 = new Date().getTime();

		out.writeUTF(user);
		out.writeDouble(num1);
		out.writeLong(time1);

		Protection p = new Protection();
		byte[] hash1 = p.makeDigest(user,password,time1,num1);

		double num2 = rn.nextDouble();
		long time2 = new Date().getTime();

		out.writeDouble(num2);
		out.writeLong(time2);

		byte[] hash2 = p.makeDigest(hash1,time2,num2);

		out.writeInt(hash2.length);
		out.write(hash2);

		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
	String host = "cse01.cse.unt.edu";
		int port = 7959;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}
