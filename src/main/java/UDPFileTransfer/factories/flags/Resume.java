package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;

/**
 * Describes the Resume flag
 * @author Mark Banierink
 */
public class Resume implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        String fileName = packet.getPacketData().getString().replaceAll("\u0000.*", "");
        if (main.isTransfer(fileName)) {
            //main.resumeTransfer(fileName);
            transferHandler.handleUserOutput("Transfer of file \"" + fileName + "\" is resumed");
        }
    }
}
