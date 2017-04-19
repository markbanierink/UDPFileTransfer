package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import java.io.File;

import static UDPFileTransfer.factories.flags.Flags.UPLOAD_REQUEST;
import static UDPFileTransfer.helper.Modes.*;
import static UDPFileTransfer.helper.Resources.*;

/**
 * Describes the UploadRequest command
 * @author Mark Banierink
 */
public class Upload implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 3) {
            String fileName = splitLine[1].toLowerCase();
            String newFileName = splitLine[2].toLowerCase();
            File file = transferHandler.getFile(fileName, main.getPath());
            if (file != null) {
                long fileSize = file.length();
                transferHandler.sendCommand(UPLOAD_REQUEST, fileName + SPACE + newFileName + SPACE + fileSize);
                transferHandler.handleUserOutput(DEFAULT, "Uploading file \"" + fileName + "\"");
            }
            else {
                transferHandler.handleUserOutput(DEFAULT, FILE_NOT_EXISTS);
            }
        }
        else {
            main.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
        }
    }
}
