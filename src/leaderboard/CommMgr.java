package leaderboard;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CommMgr{
	private static CommMgr commMgr = null;

	private static final String HOST = "192.168.2.2"; //Raspberry Pi ip address

	private static final int PORT = 5201;   //Raspberry Pi port

	public static final String MSG_TYPE_ANDROID = "a ";
	public static final String MSG_TYPE_ARDUINO = "h ";

	private static Socket conn = null;

	private static BufferedOutputStream bos = null;
	private static OutputStreamWriter osw = null;
	private static PrintWriter pw = null;
	private static BufferedWriter bw = null;
	private static BufferedReader br = null;
	private static Scanner sc = null;

	private CommMgr(){

	}

	public static CommMgr getCommMgr(){
		if (commMgr == null){
			commMgr = new CommMgr();
		}
		return commMgr;
	}

	public boolean setConnection(){
		try{
			conn = new Socket(HOST, PORT);
			//conn.connect(new InetSocketAddress(HOST, PORT), timeoutInMs);
			//conn.setSoTimeout(timeoutInMs);

			bos = new BufferedOutputStream(conn.getOutputStream());
			osw = new OutputStreamWriter(bos);
			pw = new PrintWriter(bos, true);
			bw = new BufferedWriter(osw);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			sc = new Scanner(conn.getInputStream());

			System.out.println("setConnection() -->"+" Connection established successfully!");

			return true;
		} catch (UnknownHostException e){
			System.out.println("setConnection --> UnknownHostException");
		} catch (IOException e){
			System.out.println("setConnection --> IO Exception");
		} catch (Exception e){
			System.out.println("setConnection --> Exception");
		}

		System.out.println("Failed to establish connection!");
		return false;
	}

	public void closeConnection(){
		try {
			if (bos != null){
				bos.close();
			}
			if (osw != null){
				osw.close();
			}
			if (br != null){
				br.close();
			}

			if (conn != null){
				conn.close();
				conn = null;
			}
		} catch (IOException e){
			System.out.println("closeConnection --> IO Exception");
		} catch (NullPointerException e){
			System.out.println("closeConnection --> Null Pointer Exception");
		} catch (Exception e){
			System.out.println("closeConnection --> Exception");
		}
	}

	public boolean sendMsg(String msg, String msgType){
		try {
			String outputMsg = msgType + msg + " |" + "\n";

			//outputMsg = String.format("%-128s", outputMsg);
			

			// osw.write(outputMsg);
			// osw.flush();

			// pw.print(outputMsg);
			// pw.flush();

			bw.write(outputMsg);
			bw.flush();
			System.out.print("Sending out message: " + outputMsg);

			return true;

		} catch (IOException e){
			System.out.println("send message --> IO Exception");
		} catch (Exception e){
			System.out.println("send message --> Exception");
		}

		return false;
	}

	public String recvMsg(){
		try{
			// String input = br.readLine();

			String input = sc.nextLine();

			// System.out.println(input);
			System.out.print("Received Message: ");
			System.out.println(input);

			if (input != null && input.length() > 0){
				return input;
			}
		} catch (Exception e){
			System.out.println("receive message --> Exception");
		}

		return null;
	}

	public boolean isConnected(){
		return conn.isConnected();
	}



}

