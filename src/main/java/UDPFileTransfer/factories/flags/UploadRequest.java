package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static UDPFileTransfer.factories.flags.Flags.DOWNLOAD_REQUEST;
import static UDPFileTransfer.factories.flags.Flags.MESSAGE;
import static UDPFileTransfer.helper.Resources.SPACE;

/**
 * Describes the UploadRequest flag
 * @author Mark Banierink
 */
public class UploadRequest implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (!packet.isAck()) {
            String[] splitString = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");
            String fileName = splitString[0];
            String newFileName = splitString[1];
            long size = Long.parseLong(splitString[2]);
            if (size != -1) {
                File file = new File(main.getPath() + newFileName);
                if (!file.exists() && !main.isTransfer(newFileName)) {
                    File folder = new File(main.getPath());
                    if (folder.getUsableSpace() - main.pendingSpace() > size) {
                        TransferHandler newTransferHandler = main.createTransferHandler(main.increaseAndGetPort());
                        newTransferHandler.sendCommand(packet.getDatagramPacket().getPort(), DOWNLOAD_REQUEST, fileName + SPACE + newFileName);
                    }
                    else {
                        transferHandler.sendCommand(MESSAGE, "File is too large.");
                    }
                }
                else {
                    transferHandler.sendCommand(MESSAGE, "File with that name already exists.");
                }
            }
        }
    }
}
