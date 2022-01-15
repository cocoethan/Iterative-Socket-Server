import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.net.Socket;
/**
*Enables Client Multithreading
*@extends Thread
*@author Shariah Haque, Ethan Coco, David Tovar 
*/
class clientMultiThread extends Thread {
  //[11-16] Class Variables
  String IPAddress = client.IPAddress;
  int port = client.port;
  static long totalTime = 0;
  String serverOut = "";
  int clientChoice = client.clientChoice;
  /**
  *Run Client Threads & Measure turnAroundTime
  */
  public void run() {
   // totalTime = 0;
    long turnAroundTime;
    long start;
    long end;
    try {
      Socket socket = new Socket(IPAddress, port);
      PrintWriter myWriter = new PrintWriter(socket.getOutputStream(), true);
      InputStream input = socket.getInputStream();
      BufferedReader myReader = new BufferedReader(new InputStreamReader(input));
      myWriter.println(clientChoice);
      //[31-37] Measure TurnAroundTime
      start = System.currentTimeMillis();
      while ((serverOut = myReader.readLine()) != null) {
        serverOut = myReader.readLine();
      }
      end = System.currentTimeMillis();
      //[37-40] Calculate (turnAroundTime & totalTime)
      turnAroundTime = end - start;
      totalTime = totalTime + turnAroundTime;
      System.out.println(turnAroundTime + "ms");
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
/**
*Connects to Server, Starts and Joins Client Threads, Calculate and Prints avgTime
*@author Shariah Haque, Ethan Coco, David Tovar
*/
public class client {
  //[52-57] Class Variables
  static String IPAddress = "";
  static int port;
  static int numClients;
  static int clientChoice = 0;
  /**
  *
  *@throws IOException, InterruptedException
  */
  public static void main(String[] args) throws IOException, InterruptedException {
    //[61-62] Variables
    Scanner scan = new Scanner(System.in);
    //[63-70] Print Output, and Scan Input
    System.out.println();
    System.out.print("What is the network address? (IP Address) : ");
    IPAddress = scan.nextLine();
    System.out.print("What is the desired port? : ");
    port = scan.nextInt();
    scan.nextLine();
    System.out.println();
    //[71-89] Print Menu, and Scan Input
    do {
      System.out.println(" _____________________________________________________ ");
      System.out.println("|                                                     |");
      System.out.println("|                     ~ Menu ~                        |");
      System.out.println("|_____________________________________________________|");
      System.out.println("|                                                     |");
      System.out.println("| Please choose one of the following options...       |");
      System.out.println("|                                                     |");
      System.out.println("| 1 - Date and Time                                   |");
      System.out.println("| 2 - Uptime                                          |");
      System.out.println("| 3 - Memory Use                                      |");
      System.out.println("| 4 - Netstat                                         |");
      System.out.println("| 5 - Current Users                                   |");
      System.out.println("| 6 - Running Processes                               |");
      System.out.println("| 7 - End Program                                     |");
      System.out.println("|_____________________________________________________|");
      clientChoice = scan.nextInt();
      scan.nextLine();
      //[90-96] Catch Input Errors
      if (clientChoice < 1 || clientChoice > 7) {
        System.out.println("Option not available. Please select a vaild option...\n");
        numClients = scan.nextInt();
        scan.nextLine();
        continue;
      }
      //[97-105] End Program Choice
      if (clientChoice == 7) {
	      clientMultiThread clientThreads = new clientMultiThread();
		    clientThreads.start();
		    clientThreads.join();
        System.out.println("Thank you! Have a great day.");
        System.out.println();
       	System.exit(0);
      }
      //[106-122] Spawn Clients
      System.out.println();
      System.out.println("How many clients do you want to spawn?");
      System.out.println("Please select 1, 5, 10, 15, 20, or 25.");
      numClients = scan.nextInt();
      scan.nextLine();
      System.out.println("Spawning " + numClients + " clients...");
      clientMultiThread[] clientThreads = new clientMultiThread[numClients];
      for (int i = 0; i < numClients; i++) {
        clientThreads[i] = new clientMultiThread();
      }
      for (int i = 0; i < numClients; i++) {
        clientThreads[i].start();
      }
      for (int i = 0; i < numClients; i++) {
        clientThreads[i].join();
      }
      //[123-124] Calculate (avgTime)
      long avgTime = clientMultiThread.totalTime / numClients;
      //[125-129] Print (Total Time & Average Time)
      System.out.println();
      System.out.println("Total turn around time: " + clientMultiThread.totalTime + " ms");
      System.out.println("Average turn around time: " + avgTime + " ms");
      System.out.println();
      clientMultiThread.totalTime = 0;
    } while (clientChoice != 7);
  }
}
