package UDPFileTransfer.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.copyOfRange;

/**
 * Toolbox to support byte operations
 * @author Mark Banierink
 */
public class ByteToolbox {

    public static final String CHARSET = "UTF-8";

    private static byte[] intToByteArray(int integer) {
        byte[] result = new byte[4];
        result[0] = (byte)((integer & 0xFF000000) >> 24);
        result[1] = (byte)((integer & 0x00FF0000) >> 16);
        result[2] = (byte)((integer & 0x0000FF00) >> 8);
        result[3] = (byte)(integer & 0x000000FF);
        return result;
    }

    public static byte[] intToByteArray(int integer, int size) {
        byte[] fullLength = intToByteArray(integer);
        return copyOfRange(fullLength, fullLength.length - size, fullLength.length);
    }

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes.length == 1) {
            return bytes[0] & 0xff;
        }
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] stringToByteArray(String string) {
        try {
            return string.getBytes(CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes);
    }

//    public static byte[] mergeByteArrays(byte[][] byteArrays) {
//        byte[] result = new byte[0];
//        for (byte[] byteArray : byteArrays) {
//            mergeByteArrays(result, byteArray);
//        }
//        return result;
//    }

    public static byte[] mergeByteArrays(byte[] array1, byte[] array2) {
        byte[] byteArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, byteArray, 0, array1.length);
        System.arraycopy(array2, 0, byteArray, array1.length, array2.length);
        return byteArray;
    }

    public static boolean isEqualByteArray(byte[] byteArray1, byte[] byteArray2) {
        for (int i = 0; i < byteArray2.length; i++) {
            if (byteArray2[i] != byteArray1[i]) {
                return false;
            }
        }
        return true;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] stringListToByteArray(List<String> list){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(list);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String[] byteArrayToStringArray(byte[] byteArray) {
        return null;
    }

    public static byte[] stringArrayToByteArray(String[] stringArray) {
        return null;
    }

    public static List<String> byteArrayToStringList(byte[] byteArray) {
        List<String> list = new ArrayList<>();
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            list = (ArrayList<String>) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static byte[] removeTrailingNulls(byte[] byteArray) {
        int stop = byteArray.length;
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] == 0x00) {
                stop = i;
                break;
            }
        }
        return Arrays.copyOfRange(byteArray, 0, stop);
    }

    public static byte[] calculateHash(byte[] byteArray) {
        byte[] hash = new byte[16];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            hash = messageDigest.digest(byteArray);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static byte[] fileToBytes(Path filePath) {
        try {
            return Files.readAllBytes(filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
