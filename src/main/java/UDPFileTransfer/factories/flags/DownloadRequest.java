package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static UDPFileTransfer.factories.flags.Flags.UPLOAD;
import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.FILE_NOT_EXISTS;
import static UDPFileTransfer.helper.Resources.SPACE;
import static UDPFileTransfer.helper.ByteToolbox.*;

/**
 * Describes the DownloadRequest flag
 * @author Mark Banierink
 */
public class DownloadRequest implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            String[] splitString = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");
            String fileName = splitString[0];
            String newFileName = splitString[1];
            File file = new File(main.getPath() + fileName);
            if (file.exists() && !main.isTransfer(fileName)) {
                TransferHandler newTransferHandler = main.createTransferHandler(file.getName(), main.increaseAndGetPort());
                newTransferHandler.getTransferFile().initFile();
                String hash = newTransferHandler.getTransferFile().getHash();
                long size = newTransferHandler.getTransferFile().getSize();
                newTransferHandler.sendCommand(packet.getDatagramPacket().getPort(), UPLOAD, newFileName + SPACE + size + SPACE + hash);
            }
            else {
                transferHandler.handleUserOutput(DEFAULT, FILE_NOT_EXISTS);
            }
        }
    }
}
