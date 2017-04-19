package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;

/**
 * The Interface for all flags
 * @author Mark Banierink
 */
public interface Flag {

    void execute(Main main, TransferHandler transferHandler, Packet packet);

}
