import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static File logFile = new File("chat.log");

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
        logMessage(message);
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast(client.getUsername() + " left the chat.");
    }

    private static void logMessage(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
