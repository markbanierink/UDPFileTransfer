package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.helper.Modes.DEFAULT;

/**
 * Describes the Status command
 * @author Mark Banierink
 */
public class Status implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        main.handleUserOutput(DEFAULT, main.getStatusViews());
    }
}
