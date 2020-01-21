package es.wolfteam.data.types;

import es.wolfteam.services.actions.ActionService;
import es.wolfteam.services.actions.impl.*;

/**
 * The enum Action type.
 * <p/>
 * Declare the new command for action here and you should to specify:
 * - An String command
 * - The maxParam allowed
 * - {@link ActionService} Class target
 * - Minimum Level for Rol Permission to execute this command
 */
public enum ActionType {
    HELP("help", 1, HelpActionService.class, PermissionType.WOLFTEAM),
    START("start", 2, StartActionService.class, PermissionType.OFFICER),
    STOP("stop", 1, StopActionService.class, PermissionType.OFFICER),
    RESTART("restart", 1, DefaultActionService.class, PermissionType.OFFICER),
    STATUS("status", 1, DefaultActionService.class, PermissionType.WOLFTEAM),
    UPDATE("update", 2, UpdateActionService.class, PermissionType.OFFICER),
    NUCLEAR("nuclear", 1, DefaultActionService.class, PermissionType.ADMIN),
    SLOT("slot", 3, SlotActionService.class, PermissionType.OFFICER),
    MODS("mods", 3, ModsActionService.class, PermissionType.OFFICER);

    private String name;
    private int maxParams;
    private Class<?> actionService;
    private PermissionType requiredLevel;

    ActionType(final String name, int maxParams, final Class<?> actionService, final PermissionType requiredLevel) {
        this.name = name;
        this.maxParams = maxParams;
        this.actionService = actionService;
        this.requiredLevel = requiredLevel;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets max params.
     *
     * @return the max params
     */
    public int getMaxParams()
    {
        return maxParams;
    }

    /**
     * Gets action service.
     *
     * @return the action service
     */
    public Class<?> getActionService()
    {
        return actionService;
    }

    public PermissionType getRequiredLevel()
    {
        return requiredLevel;
    }
}
