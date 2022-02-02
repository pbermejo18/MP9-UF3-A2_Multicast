import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.List;

public class Servidor {
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    private boolean continueRunning = true;
    private List<String> llistaFrases;

    public Servidor(List<String> frases, int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        llistaFrases = frases;
    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData;

        while(continueRunning){
            sendingData = getParaula();
            packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
            socket.send(packet);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }
        }
        socket.close();
    }

    private byte[] getParaula() {
        String frase = llistaFrases.get((int)(Math.random()*llistaFrases.size()));
        System.out.println(frase);
        return frase.getBytes();
    }

    public static void main(String[] args) throws IOException {
        List<String> frases = Arrays.asList("La forma més clàssica de comunicar dispositius digitals és aplicant el model de client-servidor.",
                "A les xarxes informàtiques, la informació que cal transmetre s’embolcalla dins de 'paquets virtuals'.",
                "Per a la generació, la transmissió i la recepció dels senyals es fan servir elements físics com ara les targes de xarxes o els cables.",
                "Usats en aquest nivell són UDP i TCP.",
                "UDP utilitza la longitud de les dades i un valor de comprovació anomenat checksum per a realitzar la verificació de la coherència.",
                "L’explicació en tot detall del protocol TCP.");
        Servidor servidor = new Servidor(frases, 5557, "224.0.18.180");
        servidor.runServer();
        System.out.println("Parat!");
    }

}