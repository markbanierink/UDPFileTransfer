package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;

/**
 * Describes the PacketData flag
 * @author Mark Banierink
 */
public class Data implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            transferHandler.getTransferFile().addPacket(packet);
        }
    }
}