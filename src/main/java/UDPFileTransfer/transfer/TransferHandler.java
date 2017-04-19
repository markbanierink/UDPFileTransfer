package UDPFileTransfer.transfer;

import UDPFileTransfer.factories.flags.Flags;
import UDPFileTransfer.helper.Modes;
import UDPFileTransfer.helper.Resources;
import UDPFileTransfer.transfer.packet.PacketData;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.main.Main;
import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

import static UDPFileTransfer.factories.flags.Flags.ACK;
import static UDPFileTransfer.helper.ByteToolbox.fileToBytes;
import static UDPFileTransfer.helper.Modes.DEBUG;

/**
 * Handles the transfer of packets
 * @author Mark Banierink
 */
public class TransferHandler {

    private Main main;
    private int port;
    private Receiver receiver;
    private Thread receiverThread;
    private Sender sender;
    private Thread senderThread;
    private InetAddress inetAddress;
    private TransferFile transferFile;
    private int sequenceNumber = 0;
    private int packetHandlerNumber = 0;

    public TransferHandler(Main main, int port, String ipAddress) {
        this.main = main;
        this.port = port;
        setInetAddress(ipAddress);
        initReceiver();
        initSender();
    }

    public TransferHandler(Main main, int port, InetAddress inetAddress) {
        this.main = main;
        this.port = port;
        this.inetAddress = inetAddress;
        initReceiver();
        initSender();
    }

    private void setInetAddress(String ipString) {
        try {
            inetAddress = InetAddress.getByName(ipString);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setTransferFile(String fileName) {
        transferFile = new TransferFile(this, new File(main.getPath() + fileName));
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getFileNamesString(String path) {
        StringBuilder line = new StringBuilder();
        File[] files = getFiles(path);
        for (File file : files) {
            Path tempPath = Paths.get(file.getAbsolutePath());
            String size = NumberFormat.getNumberInstance(Locale.GERMAN).format(fileToBytes(tempPath).length);
            line.append("- ").append(file.getName()).append(" (").append(size).append(" bytes)\n");
        }
        return line.toString();
    }

    public File[] getFiles(String path) {
        return (new File(path)).listFiles();
    }

    public File getFile(String fileName, String path) {
        for (File file : getFiles(path)) {
            if (file.getName().toLowerCase().equals(fileName.toLowerCase())){
                return file;
            }
        }
        return null;
    }

    private void initReceiver() {
        receiver = new Receiver(this);
        receiverThread = new Thread(receiver, "Receiver (" + port + ")");
        receiverThread.start();
        handleUserOutput(DEBUG, "New Receiver (" + port + ")");
    }

    public void initSender() {
        sender = new Sender(this);
        senderThread = new Thread(sender, "Sender (" + (port + 1) + ")");
        senderThread.start();
        handleUserOutput(DEBUG, "New Sender (" + (port + 1) + ")");
    }

    public boolean removeFile(File file) {
        return file.delete();
    }

    public void handleAck(Packet packet) {
        sender.removeAckedPacket(packet);
    }

    public void sendAck(Packet packet) {
        Packet packetCopy = packet.copy();
        packetCopy.getTransferHeader().setFlag(ACK, true);
        packetCopy.setTimestamp(System.currentTimeMillis());
        sender.addToAckQueue(packetCopy);
    }

    private double getSenderTransferPercentage() {
        return Math.round((double)sender.getSentBytes() / (double)getFileSize() * 100.0);
    }

    private double getSenderDropPercentage() {
        return Math.round((double)sender.getRetransmissions() / (double)sender.getSentPackets() * 100.0);
    }

    private double getSenderAverageRTT() {
        return Math.round((double)sender.getTotalRTT() / (double)sender.getSentPackets());
    }

    private double getSenderDuration() {
        return ((double)sender.getFinishTime() - (double)sender.getStartingTime()) / 1000.0;
    }

    private double getSenderTransferSpeed() {
        return Math.round((double)sender.getSentBytes() / getSenderDuration());
    }

    private double getReceiverTransferPercentage() {
        return Math.round((double)transferFile.getReceivedBytes() / (double)getFileSize() * 100.0);
    }

    private double getReceiverDropPercentage() {
        return Math.round((double)transferFile.getRetransmissions() / (double)transferFile.getReceivedPackets() * 100.0);
    }

    private double getReceiverDuration() {
        return ((double)transferFile.getFinishTime() - (double)transferFile.getStartingTime()) / 1000.0;
    }

    private double getReceiverTransferSpeed() {
        return Math.round((double)transferFile.getReceivedBytes() / getSenderDuration());
    }

    private long getFileSize() {
        return transferFile.getSize();
    }

    public String getSenderStatisticsString() {
        String fileSize = NumberFormat.getNumberInstance(Locale.GERMAN).format(getFileSize());
        String transferSpeed = NumberFormat.getNumberInstance(Locale.GERMAN).format(getSenderTransferSpeed());
        String string = getFileName() + " (" + fileSize + " bytes):\n";
        string += "- Transfer percentage: " + getSenderTransferPercentage() + "%\n";
        string += "- Retransmission percentage " + getSenderDropPercentage() + "%\n";
        string += "- Average RTT: " + getSenderAverageRTT() + " milliseconds\n";
        string += "- Duration: " + getSenderDuration() + " seconds\n";
        string += "- Transfer speed: " + transferSpeed + " bytes/second\n";
        return string;
    }

    public String getReceiverStatisticsString() {
        String fileSize = NumberFormat.getNumberInstance(Locale.GERMAN).format(getFileSize());
        String transferSpeed = NumberFormat.getNumberInstance(Locale.GERMAN).format(getReceiverTransferSpeed());
        String string = getFileName() + " (" + fileSize + " bytes):\n";
        string += "- Transfer percentage: " + getReceiverTransferPercentage() + "%\n";
        string += "- Retransmission percentage " + getReceiverDropPercentage() + "%\n";
        string += "- Duration: " + getReceiverDuration() + " seconds\n";
        string += "- Transfer speed: " + transferSpeed + " bytes/second\n";
        return string;
    }

    public String getReceiverName() {
        return receiverThread.getName();
    }

    public String getSenderName() {
        return senderThread.getName();
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void handleUserOutput(Modes mode, Resources resource) {
        main.handleUserOutput(mode, resource);
    }

    public void handleUserOutput(Modes mode, String string) {
        main.handleUserOutput(mode, string);
    }

    public void handleUserOutput(String[] strings) {
        main.handleUserOutput(strings);
    }

    public void handleUserOutput(String string) {
        main.handleUserOutput(string);
    }

    public void handleReceivedPacket(DatagramPacket datagramPacket, long timestamp) {
        (new PacketHandler(main, this, datagramPacket, timestamp)).run();
//        PacketHandler packetHandler = new PacketHandler(main, this, datagramPacket, timestamp);
//        Thread dataHandlerThread = new Thread(packetHandler, "PacketHandler (" + packetHandlerNumber + ")");
//        dataHandlerThread.start();
        packetHandlerNumber++;
    }

    public TransferFile getTransferFile(){
        return transferFile;
    }

    public void setTransferFile(TransferFile transferFile) {
        this.transferFile = transferFile;
    }

    public void sendCommand(Flags flags, String arguments) {
        Packet packet = new Packet(inetAddress, port, sequenceNumber, flags.getInt(), new PacketData(arguments));
        sender.addToPacketQueue(packet);
        sequenceNumber++;
    }

    public void sendCommand(int port, Flags flags, String arguments) {
        Packet packet = new Packet(inetAddress, port, sequenceNumber, flags.getInt(), new PacketData(arguments));
        sender.addToPacketQueue(packet);
        sequenceNumber++;
    }

    public void sendFile() {
        sender.addTransferFileToPacketQueue(transferFile);
    }

    public void sendFile(int startingPacket) {
        sender.addTransferFileToPacketQueue(transferFile, startingPacket);
    }

    public String getFileName() {
        return transferFile.getName();
    }

    public Main getMain() {
        return main;
    }

    public void close() {
        main.closeTransferHandler(this);
    }

    public void shutdown() {
        receiverShutdown();
        senderShutdown();
    }

    public void receiverShutdown() {
        receiverThread.interrupt();
        receiver.shutdown();
    }

    public void senderShutdown() {
        senderThread.interrupt();
        sender.shutdown();
    }

}
