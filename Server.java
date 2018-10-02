import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String path = "/Users/khushbu/Documents/Server/";

	public static void main(String[] args) throws Exception {
		ServerSocket sersock = new ServerSocket(11111);
		System.out.println("Server  is ready");
		Socket sock = sersock.accept();

		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
		OutputStream ostream = sock.getOutputStream();
		PrintWriter pwrite = new PrintWriter(ostream, true);

		InputStream istream = sock.getInputStream();
		BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

		String receiveMessage, sendMessage;
		while (true) {
			String text = null;
			if ((receiveMessage = receiveRead.readLine()) != null) {
				if (receiveMessage.startsWith("get")) {
					String[] parameters = receiveMessage.split(" ");
					if (parameters.length == 2) {
						//System.out.println("File Name= " + parameters[1]);
						// Scanner scanner = new Scanner( new File(path+parameters[1]), "UTF-8" );
						text = readFile(path + parameters[1], Charset.defaultCharset());
						// String text = scanner.next();
						System.out.println("File content = " + text);
						receiveMessage.concat(" " + text);
						//System.out.println("Final Message = " + receiveMessage);
						// scanner.close();
						pwrite.println(receiveMessage + " " + text);
						System.out.println("Sending File to Client");
						pwrite.flush();
					}
				} else if (receiveMessage.startsWith("put")) {
					System.out.println("Message = " + receiveMessage);
					String[] paramemeters = receiveMessage.split(" ");
					//System.out.println("filename = "+ paramemeters[1]);
					if (paramemeters.length > 2) {
						try (PrintWriter out = new PrintWriter(path + paramemeters[1])) {
							String txt = "";
							for (int i = 2; i < paramemeters.length; i++) {
								txt += paramemeters[i] += " ";
							}
							System.out.println("Content to be written-" + txt);
							out.println(txt);
							out.close();
							System.out.println("Storing File From Client");
							pwrite.println("File Stored");
							pwrite.flush();
							//System.out.println(receiveMessage);
						}
					}
				}
				
			}
//			sendMessage = keyRead.readLine();
//			if (sendMessage.startsWith("put")) {
//				System.out.println("Message = " + sendMessage);
//				String[] parameters = sendMessage.split(" ");
//				if (parameters.length == 2) {
//					//System.out.println("File Name= " + parameters[1]);
//					// Scanner scanner = new Scanner( new File(path+parameters[1]), "UTF-8" );
//					text = readFile(path + parameters[1], Charset.defaultCharset());
//					// String text = scanner.next();
//					System.out.println("File content = " + text);
//					sendMessage.concat(" " + text);
//					//System.out.println("Final Message = " + sendMessage);
//					System.out.println("Sending file to client");
//					// scanner.close();
//				}
//			}else if (sendMessage.startsWith("get")) {
//				//System.out.println("Message =" + text);
//				pwrite.println(sendMessage);
//				System.out.println("Getting File From Server");
//				pwrite.flush();
//			}
			
		}
	}
}
