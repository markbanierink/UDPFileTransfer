package UDPFileTransfer.factories.flags;

/**
 * Describes the Flags
 * @author Mark Banierink
 */
public enum Flags {

    ACK("00000001", null),
    FIN("00000010", null),
    CONNECT("00010000", Connect.class),
    FOLDER_REQUEST("00110000", FolderRequest.class),
    FOLDER("01000000", Folder.class),
    DOWNLOAD_REQUEST("01010000", DownloadRequest.class),
    UPLOAD_REQUEST("10110000", UploadRequest.class),
    UPLOAD("11100000", Upload.class),
    DATA("00100000", Data.class),
    PAUSE("01100000", Pause.class),
    RESUME("01110000", Resume.class),
    ABORT("10000000", Abort.class),
    REMOVE("10100000", Remove.class),
    MESSAGE("11010000", Message.class),
    CLOSE("11110000", Close.class);

    private String intString;
    private Class flagClass;

    Flags(String intString, Class flagClass) {
        this.intString = intString;
        this.flagClass = flagClass;
    }

    public Flag getInstance() {
        try {
            return (Flag)flagClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt() {
        return Integer.parseInt(intString, 2);
    }

    public static Flags getFlag(int integer) {
        for (Flags flag : Flags.values()) {
            if (flag.getInt() == integer){
                return flag;
            }
        }
        return null;
    }

}
