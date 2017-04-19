package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferFile;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;

/**
 * Describes the Upload flag
 * @author Mark Banierink
 */
public class Upload implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        if (packet.isAck()) {
            transferHandler.sendFile();
        }
        else {
            String[] splitString = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");
            if (splitString.length == 3) {
                String fileName = splitString[0];
                long size = Long.parseLong(splitString[1]);
                String hash = splitString[2];
                transferHandler.setTransferFile(new TransferFile(transferHandler, fileName, size, hash));
            }
        }
    }
}