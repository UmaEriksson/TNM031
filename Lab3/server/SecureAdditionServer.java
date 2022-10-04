
// An example class that uses the secure server socket class

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;


public class SecureAdditionServer {
	private int port;
	// This is not a reserved port number
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "Lab3/server/LIUkeystore.ks";
	static final String TRUSTSTORE = "Lab3/server/LIUtruststore.ks";
	static final String KEYSTOREPASS = "123456";
	static final String TRUSTSTOREPASS = "abcdef";
	public final static int fileSize = 6022386; // hardcoded- to be changes
	
	/** Constructor
	 * @param port The port where the server
	 *    will listen for requests
	 */
	SecureAdditionServer( int port ) {
		this.port = port;
	}
	
	/** The method that does the work for the class */
	public void run() {
		try {
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), KEYSTOREPASS.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), TRUSTSTOREPASS.toCharArray() );
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, KEYSTOREPASS.toCharArray() );
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			
			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket)sss.accept();

			DataInputStream socketIn = new DataInputStream(incoming.getInputStream());
			DataOutputStream socketOut = new DataOutputStream(incoming.getOutputStream());
			FileInputStream fileInputStream = null;
			FileOutputStream fileOutputStream = null;

			int option = socketIn.readInt();
			System.out.println("Option: " + option);
			String fileName = "";    

			switch (option) {
				case 1:
					fileDownload(fileInputStream, socketIn, socketOut, fileName);
					break;
				case 2:
					fileUpload(fileOutputStream, socketIn, socketOut, fileName);
					break;
				case 3:
					fileDeletion(fileInputStream, socketIn, socketOut, fileName);
					break;


			}
			
			//close socket
			incoming.close();
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	
	private void fileDeletion(FileInputStream fileInputStream, DataInputStream socketIn, DataOutputStream socketOut,
			String fileName) throws IOException{
				fileName = socketIn.readUTF();

				File f = new File("./" + fileName);

				f.delete();
	}

	private void fileUpload(FileOutputStream fileOutputStream, DataInputStream socketIn, DataOutputStream socketOut,
			String fileName) throws IOException{
				fileName = socketIn.readUTF();
				
				int fileLength = socketIn.readInt();
				byte[] fileData = new byte[fileLength];

				fileOutputStream = new FileOutputStream(new File("./" + fileName));
				socketIn.read(fileData);
				fileOutputStream.write(fileData);
				
				fileOutputStream.close();	
	}

	private void fileDownload(FileInputStream fileInputStream, DataInputStream socketIn, DataOutputStream socketOut,
			String fileName) throws IOException{
			fileName = socketIn.readUTF();

			fileInputStream = new FileInputStream(new File("./" + fileName));
			
			byte[] fileData = new byte[fileInputStream.available()];

			fileInputStream.read(fileData);
			fileInputStream.close();

			socketOut.writeInt(fileData.length);
			socketOut.write(fileData);
	}

	/** The test method for the class
	 * @param args[0] Optional port number in place of
	 *        the default
	 */
	public static void main( String[] args ) {
		int port = DEFAULT_PORT;
		if (args.length > 0 ) {
			port = Integer.parseInt( args[0] );
		}
		SecureAdditionServer addServe = new SecureAdditionServer( port );
		addServe.run();
	}
}

