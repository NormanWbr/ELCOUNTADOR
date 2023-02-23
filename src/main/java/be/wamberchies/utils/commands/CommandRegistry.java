package be.wamberchies.utils.commands;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CommandRegistry {

    private ArrayList<Command> commands;

    public CommandRegistry() {
        this.commands = new ArrayList<>();
    }

    public void addCommand(Command command){
        commands.add(command);
    }

    public void removeCommand(String id){
        commands.removeIf((command) -> command.getId().equalsIgnoreCase(id));
    }

    public Optional<Command> getByAlias(String alias){

        for (Command command : commands) {
            if (Arrays.asList(command.getAliases()).contains(alias)){
                return Optional.of(command);
            }
        }



        return Optional.empty();
    }

}
