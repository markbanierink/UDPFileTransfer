package UDPFileTransfer.helper;

/**
 * Contains all output strings
 * @author Mark Banierink
 */
public enum Resources {

    SPACE(" "),
    DOUBLE_BAR("=====================================================================================================\n"),
    SINGLE_BAR("-----------------------------------------------------------------------------------------------------\n"),
    PROGRAM_TITLE(">>>>> UDP FILE TRANSFER >>>>>"),
    COMMANDS("The following commands can be used (case-insensitive):\n\n"
            + "CONNECT <pi/local>\t\t\t\t\t\t\t\tConnect to the Pi (not necessary after startup)\n"
            + "FOLDER <pi/local>\t\t\t\t\t\t\t\tShow available files\n"
            + "UPLOAD <sourceFileName> <newFileName>\t\t\tUploadRequest a file\n"
            + "DOWNLOAD <sourceFileName> <newFileName>\t\t\tDownload a file\n"
            + "STATUS\t\t\t\t\t\t\t\t\t\t\tShow status of transmissions\n"
            + "REMOVE <fileName>\t\t\t\t\t\t\t\tRemove a file at the Pi\n"
            + "PAUSE <transmissionName>\t\t\t\t\t\tPause a transmission\n"
            + "RESUME <transmissionName>\t\t\t\t\t\tResumes a paused transmission\n"
            + "ABORT <transmissionID>\t\t\t\t\t\t\tAbort a transmission\n"
            + "EXIT\t\t\t\t\t\t\t\t\t\t\tExit from Pi\n"
            + "HELP\t\t\t\t\t\t\t\t\t\t\tShow this screen"),
    UNKNOWN_COMMAND("Unknown command"),
    UNKNOWN_FLAGS("Unknown message"),
    SENT("Sent: "),
    RECEIVED("Received: "),
    FILE_NOT_EXISTS("The requested file does not exist"),
    FILE_ALREADY_EXISTS("The requested file does already exist"),
    CLOSE_APPLICATION("Closing application");

    private String string;

    Resources(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

}
