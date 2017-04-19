package UDPFileTransfer.factories.flags;

import UDPFileTransfer.transfer.TransferHandler;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.packet.PacketData;

import static UDPFileTransfer.factories.flags.Flags.DOWNLOAD_REQUEST;
import static UDPFileTransfer.helper.Modes.DEBUG;
import static UDPFileTransfer.helper.Resources.RECEIVED;
import static UDPFileTransfer.helper.Resources.SPACE;

/**
 * Factory to create Flag object
 * @author Mark Banierink
 */
public class FlagFactory {

    public static Flag handleFlags(TransferHandler transferHandler, Packet packet) {
        String ack = "";
        if (packet.isAck()) {
            ack = "ACK";
        }
        transferHandler.handleUserOutput(DEBUG,
                RECEIVED.toString() + SPACE + packet.getTransferHeader().getSequenceNumber() + SPACE + packet.getDatagramPacket().getAddress() + ":"
                        + packet.getDatagramPacket().getPort() + SPACE + packet.getTransferHeader().getFlagMessage() + SPACE + ack);
        if (packet.isAck()) {
            transferHandler.handleAck(packet);
        }
        else {
            transferHandler.sendAck(packet);
        }
        return packet.getTransferHeader().getFlagMessage().getInstance();
    }

}
