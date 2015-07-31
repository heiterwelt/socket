import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class SocketServer
{
    public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    public static int number = 0;
    public static void main(String[] args)
    {
        try
        {
            ServerSocket server = new ServerSocket(22222);
            Socket socket;
            int count = 0;
            while (true)
            {
                socket = server.accept();
                count = count + 1;
                System.out.println(socket.getInetAddress().getHostAddress());
                ReadThread readThread = new ReadThread(socket);
                readThread.start();
                socketList.add(socket);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
class ReadThread extends Thread
{
    private Socket socket;
    public ReadThread(Socket socket)
    {
        this.socket = socket;
    }
    public void run()
    {
        try
        {
            
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String read=new String();
            while(true)
            {
                //System.out.println("thread is starting!");
                read=br.readLine();
                System.out.println(socket.getInetAddress().getHostAddress() +"client: "+read);
                ArrayList<Socket> socketList = SocketServer.socketList;
                for(int i = 0; i < socketList.size(); i ++)
                {
                    Socket socket = socketList.get(i);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println(read);
                    pw.flush();
                }
                if(read.equals("bye"))
                {
                    break;
                }
            }
            br.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
