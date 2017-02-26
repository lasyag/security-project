import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);

		String username = in.readUTF();
		String password = lookupPassword(username);

		Double num1 = in.readDouble();
		Long time1 = in.readLong();

		Protection p = new Protection();
		byte[] hash1 = p.makeDigest(username,password,time1,num1);

		Double num2 = in.readDouble();
		Long time2 = in.readLong();

		byte[] hash2 = p.makeDigest(hash1,time2,num2);

		int len = in.readInt();
		byte[] hash = new byte[len];
		in.readFully(hash);

		return MessageDigest.isEqual(hash2, hash);
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7959;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}
