package UDPFileTransfer.factories.commands;

/**
 * All available Commands for the main
 * @author Mark Banierink
 */
public enum Commands {
    CONNECT(Connect.class),
    FOLDER(Folder.class),
    UPLOAD(Upload.class),
    DOWNLOAD(Download.class),
    STATUS(Status.class),
    PAUSE(Pause.class),
    RESUME(Resume.class),
    ABORT(Abort.class),
    REMOVE(Remove.class),
    HELP(Help.class),
    EXIT(Exit.class);

    private Class commandClass;

    Commands(Class commandClass) {
        this.commandClass = commandClass;
    }

    public Command getInstance() {
        try {
            return (Command)commandClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
