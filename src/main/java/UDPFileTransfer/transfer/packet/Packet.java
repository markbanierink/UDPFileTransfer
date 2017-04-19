package UDPFileTransfer.transfer.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;

import static UDPFileTransfer.factories.flags.Flags.*;
import static UDPFileTransfer.helper.ByteToolbox.*;
import static java.util.Arrays.*;

/**
 * Describes the Packet
 * @author Mark Banierink
 */
public class Packet {

    private static final int TRANSFER_HEADER_SIZE = 7;

    private TransferHeader transferHeader;
    private PacketData packetData;
    private InetAddress inetAddress;
    private DatagramPacket datagramPacket;
    private long timestamp;

    public Packet() {

    }

    public Packet(InetAddress inetAddress, int port, int sequenceNumber, int flags, PacketData packetData) {
        transferHeader = new TransferHeader(sequenceNumber, flags);
        this.packetData = packetData;
        setHash();
        this.inetAddress = inetAddress;
        datagramPacket = createDatagramPacket(port);
        timestamp = 0;
    }

    public Packet(DatagramPacket datagramPacket, long timestamp) {
        this.datagramPacket = datagramPacket;
        datagramPacket.setPort(datagramPacket.getPort() - 1);
        this.timestamp = timestamp;
        this.inetAddress = datagramPacket.getAddress();
        transferHeader = new TransferHeader(copyOfRange(datagramPacket.getData(), 0, TRANSFER_HEADER_SIZE));
        packetData = new PacketData(copyOfRange(datagramPacket.getData(), TRANSFER_HEADER_SIZE, datagramPacket.getData().length));
    }

    private void setHash() {
        transferHeader.setHash(new byte[TransferHeader.TransferHeaderSize.HASH.getSize()]); // Set blank hash for length and avoiding circular calculation
        transferHeader.setHash(copyOfRange(calculateHash(getDatagramDataByteArray()), 0, TransferHeader.TransferHeaderSize.HASH.getSize()));
    }

    private DatagramPacket createDatagramPacket(int port) {
        return new DatagramPacket(getDatagramDataByteArray(), getDatagramDataByteArray().length, inetAddress, port);
    }

    public boolean isIntact() {
        byte[] hashCopy = copyOf(transferHeader.getHash(), transferHeader.getHash().length);
        byte[] hash = calculateHash(getDatagramDataByteArray());
        return isEqualByteArray(hash, hashCopy);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }

    public DatagramPacket getDatagramPacket() {
        return createDatagramPacket(datagramPacket.getPort());
    }

    public TransferHeader getTransferHeader() {
        return transferHeader;
    }

    public PacketData getPacketData() {
        return packetData;
    }

    public void setPacketData(PacketData packetData) {
        this.packetData = packetData;
    }

    public boolean isAck() {
        return transferHeader.isAck();
    }

    public boolean isFin() {
        return transferHeader.isFin();
    }

    public Packet copy() {
        PacketData packetData = new PacketData();
        if (!transferHeader.hasFlag(DATA)) {
            packetData = new PacketData(getPacketData().toByteArray());
        }
        return new Packet(inetAddress, getDatagramPacket().getPort(), getTransferHeader().getSequenceNumber(), getTransferHeader().getFlagMessage().getInt(), packetData);
    }

    public byte[] getDatagramDataByteArray() {
        return mergeByteArrays(transferHeader.toByteArray(), packetData.toByteArray());
    }

}
