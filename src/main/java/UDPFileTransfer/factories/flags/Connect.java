package UDPFileTransfer.factories.flags;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.packet.Packet;
import UDPFileTransfer.transfer.TransferHandler;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import static UDPFileTransfer.helper.Modes.DEBUG;

/**
 * Describes the Connect flag
 * @author Mark Banierink
 */
public class Connect implements Flag {

    @Override
    public void execute(Main main, TransferHandler transferHandler, Packet packet) {
//        String sourceAddress = packet.getDatagramPacket().getAddress().toString().substring(1);
//        String inetAddress = null;
//        try {
//            inetAddress = Inet4Address.getLocalHost().getHostAddress();
//        }
//        catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        if (!sourceAddress.equals(inetAddress)) {
            transferHandler.setInetAddress(packet.getDatagramPacket().getAddress());
            main.handleUserOutput(DEBUG, "Connected: " + packet.getDatagramPacket().getAddress() + ":" + packet.getDatagramPacket().getPort());
//        }
    }
}