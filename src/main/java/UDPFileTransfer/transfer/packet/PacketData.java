package UDPFileTransfer.transfer.packet;

import java.util.Arrays;
import java.util.List;

import static UDPFileTransfer.helper.ByteToolbox.*;

/**
 * Describes the data part of a packet
 * @author Mark Banierink
 */
public class PacketData {

    private byte[] data;

    public PacketData() {
        this(new byte[0]);
    }

    public PacketData(byte[] data) {
        this.data = data;
    }

    public PacketData(String string) {
        data = stringToByteArray(string);
    }

    public PacketData(String[] stringArray) {
        data = stringArrayToByteArray(stringArray);
    }

    public PacketData(List<String> list) {
        data = stringListToByteArray(list);
    }

    public List<String> getList() {
        return byteArrayToStringList(data);
    }

    public String[] getStringArray() {
        return byteArrayToStringArray(data);
    }

    public String getString() {
        return byteArrayToString(data);
    }

    public byte[] toByteArray() {
        return data;
    }
}
