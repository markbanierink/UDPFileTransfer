package UDPFileTransfer.transfer;

import UDPFileTransfer.factories.flags.Flag;
import UDPFileTransfer.factories.flags.FlagFactory;
import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import java.net.DatagramPacket;

import static UDPFileTransfer.helper.Modes.*;
import static UDPFileTransfer.helper.Resources.*;

/**
 * Handles incoming packets in separate thread to avoid blocking the receiver due to long handling times
 * @author Mark Banierink
 */
public class PacketHandler implements Runnable {

    private Main main;
    private TransferHandler transferHandler;
    private DatagramPacket datagramPacket;
    private long timestamp;

    public PacketHandler(Main main, TransferHandler transferHandler, DatagramPacket datagramPacket, long timestamp) {
        this.main = main;
        this.transferHandler = transferHandler;
        this.datagramPacket = datagramPacket;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        Packet packet = new Packet(datagramPacket, timestamp);
        packet.getDatagramPacket().setPort(datagramPacket.getPort());
//        if (packet.isIntact()) {
            handlePacket(packet);
//        }
//        else {
//            transferHandler.handleUserOutput(DEBUG, "Packet damaged and dropped");
//        }
    }

    private void handlePacket(Packet packet) {
        Flag flag = FlagFactory.handleFlags(transferHandler, packet);
        if (flag == null) {
            transferHandler.handleUserOutput(USE, UNKNOWN_FLAGS);
        }
        else {
            flag.execute(main, transferHandler, packet);
        }
    }

}
