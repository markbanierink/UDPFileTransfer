package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;
import java.io.File;

import static UDPFileTransfer.factories.flags.Flags.DOWNLOAD_REQUEST;
import static UDPFileTransfer.helper.Modes.USE;
import static UDPFileTransfer.helper.Resources.FILE_ALREADY_EXISTS;
import static UDPFileTransfer.helper.Resources.SPACE;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;
import static UDPFileTransfer.main.Main.DEFAULT_PORT;

/**
 * Describes the Download command
 * @author Mark Banierink
 */
public class Download implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 3) {
            String fileName = splitLine[1].toLowerCase();
            String newFileName = splitLine[2].toLowerCase();
            File file = new File(main.getPath() + newFileName);
            if (!file.exists() && !main.isTransfer(newFileName)) {
                TransferHandler newTransferHandler = main.createTransferHandler(file.getName(), main.increaseAndGetPort());
                newTransferHandler.sendCommand(DEFAULT_PORT, DOWNLOAD_REQUEST, fileName + SPACE + newFileName);
            }
            else {
                transferHandler.handleUserOutput(USE, FILE_ALREADY_EXISTS);
            }
        }
        else {
            main.handleUserOutput(USE, UNKNOWN_COMMAND);
        }
    }
}
