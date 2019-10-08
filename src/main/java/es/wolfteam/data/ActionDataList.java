package es.wolfteam.data;

import es.wolfteam.data.types.ActionType;

import java.util.HashSet;
import java.util.Set;

public class ActionDataList
{
    private Set<ActionType> commands;

    public ActionDataList()
    {
        this.commands = new HashSet<>();
    }

    public ActionDataList(Set<ActionType> commands)
    {
        this.commands = commands;
    }

    public Set<ActionType> getCommands()
    {
        return commands;
    }

    public void setCommands(Set<ActionType> commands)
    {
        this.commands = commands;
    }

    public void addCommand(ActionType command)
    {
        this.commands.add(command);
    }
}
