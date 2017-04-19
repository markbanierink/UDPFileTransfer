package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.factories.flags.Flags.RESUME;
import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.UNKNOWN_COMMAND;
import static UDPFileTransfer.main.Main.DEFAULT_PORT;

/**
 * Describes the Resume command
 * @author Mark Banierink
 */
public class Resume implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
            String fileName = splitLine[1].toLowerCase();
            if (main.isTransfer(fileName)) {
                TransferHandler resumingTransferHandler = main.getTransferHandler(fileName);
                resumingTransferHandler.sendCommand(DEFAULT_PORT, RESUME, fileName);

            }
        }
        else {
            main.handleUserOutput(DEFAULT, UNKNOWN_COMMAND);
        }

    }
}
