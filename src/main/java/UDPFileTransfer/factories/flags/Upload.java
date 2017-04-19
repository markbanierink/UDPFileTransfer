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
            String[] line = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");
            if (line.length == 3) {
                String name = line[0];
                long size = Long.parseLong(line[1]);
                String hash = line[2];
                transferHandler.setTransferFile(new TransferFile(transferHandler, name, size, hash));
            }
        }
    }
}