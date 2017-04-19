package UDPFileTransfer.factories.commands;

import UDPFileTransfer.main.Main;
import UDPFileTransfer.transfer.TransferHandler;

/**
 * Interface describing all Main Commands
 * @author Mark Banierink
 */
public interface Command {

    void execute(Main main, TransferHandler transferHandler, String line);

}
