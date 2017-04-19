package UDPFileTransfer.transfer;

import UDPFileTransfer.transfer.packet.Packet;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static UDPFileTransfer.helper.Modes.*;
import static UDPFileTransfer.helper.Resources.*;
import static java.lang.Thread.interrupted;

/**
 * Sends packets
 * @author Mark Banierink
 */
public class Sender implements Runnable {

    private TransferHandler transferHandler;
    private DatagramSocket datagramSocket;
    private LinkedBlockingQueue<Packet> ackQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> pendingQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Packet> packetQueue = new LinkedBlockingQueue<>();
    private HashMap<Integer, Packet> pendingPackets = new HashMap<>();

    private int timeout = 2000;
    private int windowSize = Integer.MAX_VALUE;
    private boolean running = true;
    private boolean finalPacket = false;
    private boolean finished = false;
    private int packetSize;
    private long startingTime = 0;
    private long finishTime = 0;
    private int sentPackets = 0;
    private long sentBytes = 0;
    private long totalRTT = 0;
    private int retransmissions = 0;

    private static final int THREAD_SLEEP = 10;     // Milliseconds
    private static final int DEFAULT_PACKET_SIZE = 1400;

    public Sender(TransferHandler transferHandler) {
        this.transferHandler = transferHandler;
        packetSize = DEFAULT_PACKET_SIZE;
        try {
            datagramSocket = new DatagramSocket(transferHandler.getPort() + 1);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startingTime = System.currentTimeMillis();
        while (running && !interrupted()) {
            Packet packet;
            packet = ackQueue.poll();
            if (packet != null) {
                sendAck(packet);
            }
            packet = pendingQueue.poll();
            if (packet != null && isTimedOut(packet)) {
                retransmissions++;
                sendDataPacket(packet);
            }
            packet = packetQueue.poll();
            if (packet != null && isWindowSpace()) {
                if (packet.isFin()) {
                    finalPacket = true;
                }
                sendDataPacket(packet);
            }
            if (isFinish() && !finished) {
                finished = true;
                finishTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(THREAD_SLEEP);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        datagramSocket.close();
        transferHandler.handleUserOutput(DEBUG, transferHandler.getSenderName() + " is shut down");
    }

    private boolean isFinish() {
        return finalPacket && pendingQueue.size() == 0;
    }

    public int getSentPackets() {
        return sentPackets;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    public long getTotalRTT() {
        return totalRTT;
    }

    public int getRetransmissions() {
        return retransmissions;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    private synchronized Packet sendPacket(Packet packet) {
        try {
            datagramSocket.send(packet.getDatagramPacket());
            sentPackets++;
            String isAck = "";
            if (packet.isAck()) {
                isAck = "ACK";
            }
            transferHandler.handleUserOutput(DEBUG,
                    SENT.toString() + SPACE + packet.getTransferHeader().getSequenceNumber() + SPACE + packet.getDatagramPacket().getAddress() + ":"
                            + packet.getDatagramPacket().getPort() + SPACE + packet.getTransferHeader().getFlagMessage() + SPACE + isAck);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    private boolean isTimedOut(Packet packet) {
        return packet.getTimestamp() + timeout < System.currentTimeMillis();
    }

    private boolean isWindowSpace() {
        return pendingQueue.size() < windowSize;
    }

    private void sendAck(Packet packet) {
        sendPacket(packet);
    }

    private void sendDataPacket(Packet packet) {
        packet.setTimestamp(System.currentTimeMillis());
        pendingQueue.add(packet);
        pendingPackets.put(packet.getTransferHeader().getSequenceNumber(), packet);
        sendPacket(packet);
    }

    public void removeAckedPacket(Packet ackPacket) {
        int sequenceNumber = ackPacket.getTransferHeader().getSequenceNumber();
        Packet packet = pendingPackets.get(sequenceNumber);
        if (packet != null) {
            updatePacketStatistics(packet, ackPacket);
            pendingQueue.remove(packet);
            pendingPackets.remove(sequenceNumber);
        }
    }

    private void updatePacketStatistics(Packet packet, Packet ackPacket) {
        totalRTT += (ackPacket.getTimestamp() - packet.getTimestamp());
        sentBytes += packet.getPacketData().toByteArray().length;
    }

    public void addTransferFileToPacketQueue(TransferFile transferFile) {
        for (Packet packet : transferFile.toPacketList(getPacketSize())) {
            addToPacketQueue(packet);
        }
    }

    public void addTransferFileToPacketQueue(TransferFile transferFile, int startingPacket) {
        int i = 0;
        for (Packet packet : transferFile.toPacketList(getPacketSize())) {
            if (i >= startingPacket) {
                addToPacketQueue(packet);
            }
            i++;
        }
    }

    public void addToAckQueue(Packet packet) {
        ackQueue.add(packet);
    }

    public void addToPacketQueue(Packet packet) {
        packetQueue.add(packet);
    }

    private int getPacketSize() {
        return packetSize;
    }

    public void shutdown() {
        running = false;
    }
}




