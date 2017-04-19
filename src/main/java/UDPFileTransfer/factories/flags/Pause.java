package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;

/**
 * Describes the Pause pause
 * @author Mark Banierink
 */
public class Pause implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        String fileName = packet.getPacketData().getString().replaceAll("\u0000.*", "");
        if (main.isTransfer(fileName)) {
            //main.pauseTransfer(fileName);
            transferHandler.handleUserOutput("Transfer of file \"" + fileName + "\" is paused");
        }
    }
}
