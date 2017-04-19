package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferFile;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static UDPFileTransfer.factories.flags.Flags.UPLOAD;
import static UDPFileTransfer.helper.Modes.USE;
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
        String[] splitString = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");
        if (!packet.isAck()) {
            File file = new File(main.getPath() + "/" + splitString[0]);
            if (file.exists()) {
                Path path = Paths.get(file.getAbsolutePath());
                byte[] fileInBytes = fileToBytes(path);
                String hash = bytesToHex(calculateHash(fileInBytes));
                TransferHandler newTransferHandler = main.createTransferHandler(file.getName(), main.increaseAndGetPort());
                newTransferHandler.sendCommand(packet.getDatagramPacket().getPort(), UPLOAD, splitString[1] + SPACE + file.length() + SPACE + hash);
                newTransferHandler.getTransferFile().setSize(fileInBytes.length);
            }
            else {
                transferHandler.handleUserOutput(USE, FILE_NOT_EXISTS);
            }
        }
    }
}
