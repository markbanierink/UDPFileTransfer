package UDPFileTransfer.transfer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static UDPFileTransfer.helper.ByteToolbox.*;
import static UDPFileTransfer.helper.Modes.*;
import static UDPFileTransfer.helper.Resources.*;
import static java.lang.Thread.interrupted;

/**
 * Receives packets
 * @author Mark Banierink
 */
public class Receiver implements Runnable {

    private TransferHandler transferHandler;
    private DatagramSocket datagramSocket;
    private boolean running = true;
    private byte[] buffer;

    private static final int BUFFER_SIZE = 1400;

    public Receiver(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;
        try {
            datagramSocket = new DatagramSocket(transferHandler.getPort());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running && !interrupted()) {
            try {
                buffer = new byte[BUFFER_SIZE];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                handleReceivedPacket(datagramPacket, System.currentTimeMillis());
            }
            catch (IOException e) {
                running = false;
            }
        }
        transferHandler.handleUserOutput(DEBUG, transferHandler.getReceiverName() + " is shut down");
    }

    private void handleReceivedPacket(DatagramPacket datagramPacket, long timestamp) {
        transferHandler.handleReceivedPacket(datagramPacket, timestamp);
    }

    public void shutdown() {
        datagramSocket.close();
    }
}
