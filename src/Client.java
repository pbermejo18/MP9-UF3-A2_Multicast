import java.io.IOException;
import java.net.*;
import java.util.*;

public class Client {
    private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    private NetworkInterface netIf;
    private InetSocketAddress group;

    public Client(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
    }

    public void runClient() throws IOException{
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];

        socket.joinGroup(group,netIf);
        System.out.printf("Connectat a %s:%d%n",group.getAddress(),group.getPort());

        while(continueRunning){
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);
            try{
                socket.receive(packet);
                continueRunning = getData(packet.getData(), packet.getLength());
            }catch(SocketTimeoutException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group,netIf);
        socket.close();
    }

    protected boolean getData(byte[] data, int length) {
        String p = new String(data,0, length);
        //int caracteres = p.length();

        StringTokenizer st = new StringTokenizer(p);
        if (st.countTokens() > 8) {
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("-- " + p);
            System.out.println("-- Número de palabras: " + st.countTokens());
            System.out.println("-----------------------------------------------------------------------");
        } else {
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("-- " + p);
            System.out.println("-- Aquesta es una frase de menys de 8 paraules");
            System.out.println("-----------------------------------------------------------------------");
        }

        return true;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(5557, "224.0.18.180");
        client.runClient();
        System.out.println("Parat!");
    }
}