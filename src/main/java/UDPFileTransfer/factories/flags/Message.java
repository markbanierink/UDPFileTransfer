package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;

/**
 * Describes the Message flag behaviour
 * @author Mark Banierink
 */
public class Message implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        transferHandler.handleUserOutput(packet.getPacketData().getString());
        transferHandler.close();
    }
}
