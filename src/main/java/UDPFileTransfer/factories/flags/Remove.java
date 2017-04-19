package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;
import java.io.File;

import static UDPFileTransfer.factories.flags.Flags.FOLDER;

/**
 * Describes the Remove flag
 * @author Mark Banierink
 */
public class Remove implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            String fileName = packet.getPacketData().getString().replaceAll("\u0000.*", "");
            File file = transferHandler.getFile(fileName, main.getPath());
            if (file != null) {
                if (transferHandler.removeFile(file)) {
                    transferHandler.handleUserOutput("Removed " + fileName);
                    transferHandler.sendCommand(FOLDER, transferHandler.getFileNamesString(main.getPath()));
                }
                else {
                    transferHandler.handleUserOutput("File could not be removed");
                }
            }
        }
    }
}
