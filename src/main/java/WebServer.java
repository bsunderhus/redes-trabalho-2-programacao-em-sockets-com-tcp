import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {
    private static int PORT = 3000;
    private static void respondSocket(Socket connectionSocket) throws IOException{
        System.out.println("Starting to process request...");
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        String firstLine = inFromClient.readLine();
        if (firstLine == null) return;
        StringTokenizer tokenizedLine = new StringTokenizer(firstLine);
        String method = tokenizedLine.nextToken().toUpperCase();
        String path = tokenizedLine.nextToken();
        String protocol = tokenizedLine.nextToken();
        if (!protocol.startsWith("HTTP")) {
            System.out.println("Not an HTTP Request");
            return;
        }
        System.out.println(protocol + " " + method + " Request:");
        System.out.println("  " + firstLine);
        String restOfLines;
        while ((restOfLines = inFromClient.readLine()).length() > 0) System.out.println("  " + restOfLines);
        if (method.equals("GET")) writeFile(path, outToClient, false);
        else if (method.equals("HEAD")) writeFile(path, outToClient, true);
        else writeBadRequest(outToClient);
        connectionSocket.close();
    }

    private static void writeFile (String fileName, DataOutputStream outToClient, boolean isHead) throws IOException {
        if (fileName.startsWith("/")) fileName = fileName.substring(1);
        if (fileName.length() == 0) fileName = "index.html";
        if (isHead) System.out.println("HTTP/1.0 HEAD Response:");
        else System.out.println("HTTP/1.0 GET Response:");
        try {
            URL url = WebServer.class.getClassLoader().getResource(fileName);
            String filePath = url.getPath();
            File file = new File(filePath);
            int numOfBytes = (int) file.length();
            byte[] fileInBytes = new byte[numOfBytes];
            FileInputStream inFile = new FileInputStream(filePath);
            inFile.read(fileInBytes);
            String output = "HTTP/1.0 200 Document Follows\r\n";
            outToClient.writeBytes(output);
            System.out.print("  " + output);
            if (fileName.endsWith("jpg")) {
                output = "Content-Type: image/jpeg\r\n";
                outToClient.writeBytes(output);
                System.out.print("  " + output);
            }
            if (fileName.endsWith("gif")) {
                output = "Content-Type: image/gif\r\n";
                outToClient.writeBytes(output);
                System.out.print("  " + output);
            }
            if (fileName.endsWith("txt")) {
                output = "Content-Type: text/plain\r\n";
                outToClient.writeBytes(output);
                System.out.print("  " + output);
            }
            System.out.print("  Content-length: " + numOfBytes + "\r\n");
            outToClient.writeBytes("Content-length: " + numOfBytes + "\r\n");
            System.out.print("  \r\n");
            outToClient.writeBytes("\r\n");
            if (!isHead) outToClient.write(fileInBytes, 0, numOfBytes);
        } catch (Exception e) {
            String output = "HTTP/1.0 404 Not Found\r\n";
            outToClient.writeBytes(output);
            System.out.print("  " + output);
            System.out.print("  \r\n");
            outToClient.writeBytes("\r\n");
        }
    }

    private static void writeBadRequest (DataOutputStream outToClient) throws  IOException {
        System.out.println("HTTP/1.0 Bad Request Response:");
        System.out.print("  HTTP/1.0 400 Bad Request\r\n");
        outToClient.writeBytes("HTTP/1.0 400 Bad Request\r\n");
        System.out.print("  Content-length: " + 0 + "\r\n");
        outToClient.writeBytes("Content-length: " + 0 + "\r\n");
        System.out.print("  \r\n");
        outToClient.writeBytes("\r\n");
    }

    public static void main(String args[]) throws IOException {
        ServerSocket listenSocket = new ServerSocket(PORT);
        System.out.println("Web server listening to port 3000...");
        int aux = 1;
        while (true) {
            Socket socket = listenSocket.accept();
            System.out.println("Creating Thread to attend request");
            new RespondThread(socket, aux++).start();
        }
    }
    static class RespondThread extends Thread {
        Socket socket;
        int id;
        public RespondThread(Socket socket, int id) {
            System.out.println("Thread " + id + " created successfully");
            this.socket = socket;
            this.id = id;
        }
        public void run() {
            System.out.println("Starting Thread " + id);
            try {
                WebServer.respondSocket(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Stopping Thread " + id);
        }
    }
}
