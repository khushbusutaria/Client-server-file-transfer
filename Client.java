import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client {
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String path = "/Users/khushbu/Documents/Client/";

	public static void main(String[] args) throws Exception {
		Socket sock = new Socket("10.241.39.46", 11111);

		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
		OutputStream ostream = sock.getOutputStream();
		PrintWriter pwrite = new PrintWriter(ostream, true);

		InputStream istream = sock.getInputStream();
		BufferedReader receivedRead = new BufferedReader(new InputStreamReader(istream));
		System.out.println("Client is ready.");
		System.out.println("Enter command:get filename, put filename or quit.");

		String receiveMessage, sendMessage;
		while (true) {
			String text = null;
			sendMessage = keyRead.readLine();
			if (sendMessage.startsWith("put")) {
				System.out.println("Message = " + sendMessage);
				String[] parameters = sendMessage.split(" ");
				if (parameters.length == 2) {

					text = readFile(path + parameters[1], Charset.defaultCharset());
					System.out.println("File content =" + text);

					pwrite.println(sendMessage + " " + text);
					System.out.println("Sending File to the server");
					pwrite.flush();
				}
			} else if (sendMessage.startsWith("get")) {
				System.out.println("Message =" + sendMessage);
				pwrite.println(sendMessage);
				System.out.println("Getting File From Server");
				pwrite.flush();
			} else if (sendMessage.equals("quit")) {
				System.out.println("Connection terminated.");
				sock.close();
			} else {
				System.out.println("Please enter correct command.");
			}

			if ((receiveMessage = receivedRead.readLine()) != null) {
				System.out.println(receiveMessage);
				if (receiveMessage.startsWith("get")) {
					System.out.println("Message = " + receiveMessage);
					String[] paramemeters = receiveMessage.split(" ");
					System.out.println("filename = " + paramemeters[1]);
					if (paramemeters.length > 2) {
						try (PrintWriter out = new PrintWriter(path + paramemeters[1])) {
							String txt = "";
							for (int i = 2; i < paramemeters.length; i++) {
								txt += paramemeters[i] += " ";
							}
							System.out.println("Content to be written=" + txt);
							System.out.println("File  Write Operation Completed");
							out.println(txt);
							out.close();
						}
					}
				} else {
					System.out.println(receiveMessage);
				}

			}
		}
	}
}