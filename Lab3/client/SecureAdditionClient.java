// A client-side class that uses a secure TCP/IP socket

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.Scanner;
import javax.net.ssl.*;

public class SecureAdditionClient {
	private InetAddress host;
	private int port;
	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "./client/LIUkeystore.ks";
	static final String TRUSTSTORE = "./client/LIUtruststore.ks";
	static final String KEYSTOREPASS = "123456";
	static final String TRUSTSTOREPASS = "abcdef";
  
	
	// Constructor @param host Internet address of the host where the server is located
	// @param port Port number on the host where the server is listening
	public SecureAdditionClient( InetAddress host, int port ) {
		this.host = host;
		this.port = port;
	}
	
  // The method used to start a client object
	public void run() {
		try {
			// Here the keystore and truststore are created
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), KEYSTOREPASS.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), TRUSTSTOREPASS.toCharArray() );
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, KEYSTOREPASS.toCharArray() );
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );
			// TLS, for extra security
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			// SSL handshake
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLSocketFactory sslFact = sslContext.getSocketFactory();

			SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
			//set encryption methods
			client.setEnabledCipherSuites( new String[]{"TLS_AES_256_GCM_SHA384"});
			//set client authentication true
			client.setNeedClientAuth(true);

			System.out.println("\n>>>> SSL/TLS handshake completed");

			//client and server "sockets"
			DataInputStream socketIn = new DataInputStream( client.getInputStream());
			DataOutputStream socketOut = new DataOutputStream( client.getOutputStream() );

			//Get input from terminal
			System.out.println("Do you want to\n 1. Download file \n 2. Upload file \n 3. Delete file\n from/to/on the server\n");

			//Parse input
			Scanner sc = new Scanner(System.in);
			int inputValue;
			if(sc.hasNextInt()){
				inputValue = sc.nextInt();
			}else{
				throw new Exception("Input a number between 1-3");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String fileName;
			//Check what user wants to do
			switch (inputValue) {
				case 1:
					//DownloadFile
					System.out.print("Enter filename\n");
					fileName = reader.readLine();
					downloadFile(fileName, socketIn,socketOut, inputValue);
					break;

				case 2:
					//uploadFile();
					System.out.println("Enter filename");
					fileName = reader.readLine();
					uploadFile(fileName, socketOut, inputValue);
					break;

				case 3:
					//deleteFile();
					System.out.println("Enter filename");
					fileName = reader.readLine();
					deleteFile(fileName, socketOut, inputValue);
					break;

				default:
					throw new Exception("Input a number between 1-3");

			}
		}
		catch( Exception x ) {
			System.out.println(x);
			x.printStackTrace();
		}
	}
	
	
	// The test method for the class @param args Optional port number and host name
	public static void main( String[] args ) {
		try {
			InetAddress host = InetAddress.getLocalHost();
			int port = DEFAULT_PORT;
			if ( args.length > 0 ) {
				port = Integer.parseInt( args[0] );
			}
			if ( args.length > 1 ) {
				host = InetAddress.getByName( args[1] );
			}
			SecureAdditionClient addClient = new SecureAdditionClient( host, port );
			addClient.run();
		}
		catch ( UnknownHostException uhx ) {
			System.out.println( uhx );
			uhx.printStackTrace();
		}
	}

	private void downloadFile(String fileName, DataInputStream socketIn, DataOutputStream socketOut, int inputValue) throws IOException{
		//write option and filename to server
		socketOut.writeInt(inputValue);
		socketOut.writeUTF(fileName);

		//read file from server
		int fileLength = socketIn.readInt();
		byte[] fileData = new byte[fileLength];
		socketIn.read(fileData);

		//write data to file
		FileOutputStream fos = new FileOutputStream("./client/" + fileName);
		fos.write(fileData);
		fos.close();

		System.out.println("The file has been downloaded");
		
	}

	private void uploadFile(String fileName, DataOutputStream socketOut, int inputValue) throws IOException{
		//write filename and option to server
		socketOut.writeInt(inputValue);
		socketOut.writeUTF(fileName);

		//read data from file
		FileInputStream fis = new FileInputStream("./client/" + fileName);
		byte[] fileData = fis.readAllBytes();

		socketOut.writeInt(fileData.length);
		socketOut.write(fileData);

		fis.close();

		System.out.println("The file has been upload");
	}

	private void deleteFile(String fileName, DataOutputStream socketOut, int inputValue) throws IOException{
		//send name of file to delete to server
		socketOut.writeInt(inputValue);
		socketOut.writeUTF(fileName);

		System.out.println("The file has been deleted");
	}

}
