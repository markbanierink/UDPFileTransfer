package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.helper.Modes.DEFAULT;

/**
 * Describes the Resume flag
 * @author Mark Banierink
 */
public class Resume implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        String[] splitLine = packet.getPacketData().getString().replaceAll("\u0000.*", "").split(" ");  // name, lastPacket
        String fileName = splitLine[0];
        int lastPacket = Integer.parseInt(splitLine[1]);
        if (!packet.isAck()) {
            if (main.isTransfer(fileName)) {
                int port = packet.getDatagramPacket().getPort() - 1;
                TransferHandler newTransferHandler = main.createTransferHandler(fileName, port);
                newTransferHandler.sendFile(lastPacket);
                transferHandler.handleUserOutput("Transfer of file \"" + fileName + "\" is resumed");
            }
        }
        else {
            transferHandler.handleUserOutput(DEFAULT, "Resuming transfer of file \"" + fileName + "\"");
        }
    }
}
