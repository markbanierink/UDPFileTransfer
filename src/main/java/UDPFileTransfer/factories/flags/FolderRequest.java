package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.PacketData;
import java.io.File;

import static UDPFileTransfer.factories.flags.Flags.FOLDER;

/**
 * Describes the FolderRequest flag
 * @author Mark Banierink
 */
public class FolderRequest implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            transferHandler.sendCommand(FOLDER, transferHandler.getFileNamesString(main.getPath()));
        }
    }
}
