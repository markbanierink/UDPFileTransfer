package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;

import static UDPFileTransfer.helper.Modes.DEFAULT;

/**
 * Describes the Close flag behaviour
 * @author Mark Banierink
 */
public class Close implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
        transferHandler.handleUserOutput(packet.getPacketData().getString());
        if (packet.isAck()) {
            transferHandler.handleUserOutput(DEFAULT, transferHandler.getReceiverStatisticsString());
        }
        else {
            transferHandler.handleUserOutput(DEFAULT, transferHandler.getSenderStatisticsString());
        }
        // Sleep to be sure all final messages are sent and received
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        transferHandler.close();
    }
}
