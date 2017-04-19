package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

import static UDPFileTransfer.helper.Modes.DEFAULT;
import static UDPFileTransfer.helper.Resources.COMMANDS;
import static UDPFileTransfer.helper.Resources.DOUBLE_BAR;
import static UDPFileTransfer.helper.Resources.SINGLE_BAR;

/**
 * Describes the Help command
 * @author Mark Banierink
 */
public class Help implements Command {

    @Override
    public void execute(Main main, TransferHandler transferHandler, String line) {
        transferHandler.handleUserOutput(DEFAULT, DOUBLE_BAR);
        transferHandler.handleUserOutput(DEFAULT, COMMANDS);
        transferHandler.handleUserOutput(DEFAULT, SINGLE_BAR);
    }
}
