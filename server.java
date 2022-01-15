import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;

/**
 * Hosts Server capable of outputting Data from Linux Commands from Multithreaded Client Requests
 * @author Shariah Haque, Ethan Coco, David Tovar
 */
public class server {
  /**
  * Main
  */
  public static void main(String[] args) {
    //[18-25] Variables
    Scanner scan = new Scanner(System.in);
    String clientChoice;
    Socket socket;
    InputStream inClient;
    OutputStream outServer;
    BufferedReader myReader;
    PrintWriter writer;
    //[26-28] Print Output, Scan Input
    System.out.print("Enter the port number: ");
    int port = scan.nextInt();
    //[29-30] Try to establish serverSocket connection
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server is listening on port " + port + " ...");
      while (true) {
        socket = serverSocket.accept();
        System.out.println();
        System.out.println("New Client Connected");
        inClient = socket.getInputStream();
        outServer = socket.getOutputStream();
        myReader = new BufferedReader((new InputStreamReader(inClient)));
        writer = new PrintWriter(outServer, true);
        clientChoice = myReader.readLine();
        System.out.println("Client chose: " + clientChoice);
        //[42-115] if Statements using Linux Commands to Output Data based on Client choice
        if (clientChoice.equals("1")) {
          System.out.println();
          System.out.println("Client Request: Date and Time");
          String date = new Date().toString();
          writer.println(date);
          System.out.println("[" + date + "]");
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("2")) {
          System.out.println();
          System.out.println("Client Request: Uptime");
          RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
          long uptime = TimeUnit.MILLISECONDS.toSeconds(rtBean.getUptime());
          writer.println("Uptime: " + uptime + " s");
          System.out.println("[Uptime: " + uptime + " s]");
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("3")) {
          System.out.println();
          System.out.println("Client Request: Memory Use");
          Runtime runtime = Runtime.getRuntime();
          long memoryInUse = runtime.totalMemory() - runtime.freeMemory();
          writer.println("Total Memory on Server: " + runtime.totalMemory() + " Bytes");
          writer.println("Memory in Use: " + memoryInUse + " Bytes");
          System.out.println("[Total Memory on Server: " + runtime.totalMemory() + " Bytes]");
          System.out.println("[Memory in Use: " + memoryInUse + " Bytes]");
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("4")) {
          System.out.println();
          System.out.println("Client Request: Netstat");
          String cmd = "netstat --all";
          Process netStatProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader netstatReader = new BufferedReader(new InputStreamReader(netStatProcess.getInputStream()));
          String line;
          while ((line = netstatReader.readLine()) != null) {
            writer.println(line);
            System.out.println(line);
          }
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("5")) {
          System.out.println();
          System.out.println("Client Request: Current Users");
          String cmd = "who -H";
          Process listUsersProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader listUsersReader = new BufferedReader(new InputStreamReader(listUsersProcess.getInputStream()));
          String listUsers;
          while ((listUsers = listUsersReader.readLine()) != null) {
            writer.println(listUsers);
            System.out.println(listUsers);
          }
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("6")) {
          System.out.println();
          System.out.println("Client Request: Running Processes");
          String cmd = "ps -ef";
          Process psProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader psReader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()));
          String ps;
          while ((ps = psReader.readLine()) != null) {
            writer.println(ps);
            System.out.println(ps);
          }
          System.out.println("Client: Client Finished\n");
        } else if (clientChoice.equals("7")) {
          System.out.println();
          System.out.println("Client Request: Closing Server and Ending Program");
          System.out.println();
          System.out.println("Server closed");
          System.out.println();
          writer.close();
          myReader.close();
          socket.close();
          serverSocket.close();
          System.exit(0);
        }
        writer.close();
        myReader.close();
      }
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}