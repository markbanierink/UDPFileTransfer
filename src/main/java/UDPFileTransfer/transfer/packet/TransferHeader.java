package UDPFileTransfer.transfer.packet;

import UDPFileTransfer.factories.flags.Flags;
import java.util.Arrays;

import static UDPFileTransfer.helper.ByteToolbox.*;
import static UDPFileTransfer.transfer.packet.TransferHeader.TransferHeaderSize.*;
import static UDPFileTransfer.factories.flags.Flags.*;

/**
 * Represents the custom File Transfer Header
 * @author Mark Banierink
 */
public class TransferHeader {

    public enum TransferHeaderSize {

        SEQUENCE_NUMBER(4),
        FLAGS(1),
        HASH(2);

        private int size;

        TransferHeaderSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public int getStart() {
            int start = 0;
            for (TransferHeaderSize transferHeaderSize : TransferHeaderSize.values()) {
                if (transferHeaderSize == this) {
                    break;
                }
                start = start + transferHeaderSize.size;
            }
            return start;
        }

        public static int getHeaderSize() {
            int totalSize = 0;
            for (TransferHeaderSize size : TransferHeaderSize.values()) {
                totalSize += size.size;
            }
            return totalSize;
        }
    }

    private int sequenceNumber;
    private int flags;
    private byte[] hash;

    public TransferHeader(int sequenceNumber, int flags) {
        this.sequenceNumber = sequenceNumber;
        this.flags = flags;
        hash = new byte[HASH.getSize()];
    }

    public TransferHeader(byte[] data) {
        sequenceNumber = byteArrayToInt(Arrays.copyOfRange(data, SEQUENCE_NUMBER.getStart(), SEQUENCE_NUMBER.getStart() + SEQUENCE_NUMBER.size));
        flags = byteArrayToInt(Arrays.copyOfRange(data, FLAGS.getStart(), FLAGS.getStart() + FLAGS.size));
        hash = Arrays.copyOfRange(data, HASH.getStart(), HASH.getStart() + HASH.size);
    }

    public int getHeaderSize() {
        return TransferHeaderSize.getHeaderSize();
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public void setFlag(Flags flag, boolean bool) {
        if (flag.getInt() > 0x0f) {
            flags = (flags & 0x0f);         // mask for last 4 bits, to avoid message flag influence
            if (bool) {
                flags += flag.getInt();
            }
        }
        else if (bool && !hasFlag(flag)) {
            flags += flag.getInt();
        }
        else if (!bool && hasFlag(flag)) {
            flags -= flag.getInt();
        }
    }

    public int getFlags() {
        return flags;
    }

    public Flags getFlagMessage() {
        return Flags.getFlag(flags & 0xf0);
    }

    public boolean isAck() {
        int flagInt = flags & 0x0f;
        return (flagInt & ACK.getInt()) > 0;
    }

    public boolean isFin() {
        return (flags & FIN.getInt()) > 0;
    }

    public boolean hasFlag(Flags flag) {
        return (flags & 0xf0) == flag.getInt();
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public byte[] toByteArray() {   // TODO - more elegant
        return mergeByteArrays(mergeByteArrays(intToByteArray(sequenceNumber, SEQUENCE_NUMBER.getSize()), intToByteArray(flags, FLAGS.getSize())), hash);
    }
}
