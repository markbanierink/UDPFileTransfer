package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;

/**
 * Describes the Folder folder
 * @author Mark Banierink
 */
public class Folder implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            transferHandler.handleUserOutput("Files available:");
            transferHandler.handleUserOutput(packet.getPacketData().getString());
        }
    }
}
