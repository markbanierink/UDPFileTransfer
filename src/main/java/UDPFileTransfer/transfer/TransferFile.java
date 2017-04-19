package UDPFileTransfer.transfer;

import UDPFileTransfer.transfer.packet.PacketData;
import UDPFileTransfer.transfer.packet.TransferHeader;
import UDPFileTransfer.transfer.packet.Packet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static UDPFileTransfer.factories.flags.Flags.*;
import static UDPFileTransfer.helper.ByteToolbox.*;
import static UDPFileTransfer.helper.Modes.DEBUG;

/**
 * Describes the file that is send
 * @author Mark Banierink
 */
public class TransferFile {

    private File file;
    private Map<Integer, Packet> packets = new HashMap<>();
    private int numPackets = 0;
    private long size = 0;
    private TransferHandler transferHandler;
    private String hash;
    private boolean isMade = false;
    private long startingTime = 0;
    private long finishTime = 0;
    private long receivedBytes = 0;
    private long totalRTT = 0;
    private int retransmissions = 0;

    public TransferFile(TransferHandler transferHandler, File file) {
        this.transferHandler = transferHandler;
        this.file = file;
    }

    public TransferFile(TransferHandler transferHandler, String fileName, long size, String hash) {
        this.transferHandler = transferHandler;
        this.file = new File(fileName);
        this.size = size;
        this.hash = hash;
    }

    public void initFile() {
        Path path = Paths.get(file.getAbsolutePath());
        byte[] fileInBytes = fileToBytes(path);
        size = fileInBytes.length;
        hash = bytesToHex(calculateHash(fileInBytes));
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addPacket(Packet packet) {
        packets.put(packet.getTransferHeader().getSequenceNumber(), packet);
        updateStatistics(packet);
        if (packet.isFin()) {
            transferHandler.handleUserOutput(DEBUG, "Number of packets: " + packet.getTransferHeader().getSequenceNumber());
            numPackets = packet.getTransferHeader().getSequenceNumber();
        }
        if (isComplete() && !isMade) {          // Avoid double creation due to unacked packet
            finishTime = System.currentTimeMillis();
            createFile();
        }
    }

    private void updateStatistics(Packet packet) {
        if (packets.size() == 0) {
            startingTime = System.currentTimeMillis();
        }
        if (packets.containsKey(packet.getTransferHeader().getSequenceNumber())) {
            retransmissions++;
        }
        else {
            receivedBytes += packet.getPacketData().toByteArray().length;
        }
    }

    public int getReceivedPackets() {
        return packets.size();
    }

    public long getReceivedBytes() {
        return receivedBytes;
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

    private boolean isComplete() {
        return (numPackets > 0 && packets.size() == numPackets + 1);
    }

    private void createFile() {
        transferHandler.handleUserOutput(DEBUG, "Preparing file \"" + file + "\"");
        isMade = true;
        byte[] fileBytes = new byte[0];
        for (int i = 0; i < packets.size(); i++) {
            byte[] packetArray = packets.get(i).getPacketData().toByteArray();
            if (i + 1 == packets.size()) {
                packetArray = removeTrailingNulls(packetArray);
            }
            fileBytes = mergeByteArrays(fileBytes, packetArray);
            transferHandler.handleUserOutput(DEBUG, "Merging packets " + i + "/" + packets.size() + " of file \"" + file + "\"");
        }
        if (isInteger(fileBytes)) {
            writeFile(fileBytes, file.getPath());
            transferHandler.sendCommand(CLOSE,"Correct transfer of file \"" + file.getName() + "\"");
        }
        else {
            transferHandler.sendCommand(CLOSE,"Transfer of file \"" + file.getName() + "\" failed");
        }
    }

    public String getHash() {
        return hash;
    }

    private void writeFile(byte[] fileBytes, String fileName) {
        try {
            transferHandler.handleUserOutput(DEBUG, "Writing file \"" + file + "\"");
            String fullPath = transferHandler.getMain().getPath() + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
            fileOutputStream.write(fileBytes);
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isInteger(byte[] fileBytes) {
        String calculatedHash = bytesToHex(calculateHash(fileBytes));
        return calculatedHash.equals(hash);
    }

    public List<Packet> toPacketList(int packetSize) {
        int dataSize = packetSize - TransferHeader.TransferHeaderSize.getHeaderSize();
        Path path = Paths.get(file.getAbsolutePath());
        byte[] fileBytes = fileToBytes(path);
        int numPackets = (int)Math.ceil(((double)fileBytes.length) / dataSize);
        List<Packet> packetList = new ArrayList<>();
        for (int i = 0; i < numPackets; i++) {
            int rangeStart = i * dataSize;
            int rangeStop = rangeStart + dataSize;
            if (i + 1 == numPackets) {
                rangeStop = fileBytes.length;
            }
            PacketData packetData = new PacketData(Arrays.copyOfRange(fileBytes, rangeStart, rangeStop));
            Packet packet = new Packet(transferHandler.getInetAddress(), transferHandler.getPort(), i, DATA.getInt(), packetData);
            if (i + 1 == numPackets) {
                packet.getTransferHeader().setFlag(FIN, true);
            }
            packetList.add(packet);
        }
        return packetList;
    }

    public long getSize() {
        return size;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return file.getPath();
    }

    public String getName() {
        return file.getName();
    }
}