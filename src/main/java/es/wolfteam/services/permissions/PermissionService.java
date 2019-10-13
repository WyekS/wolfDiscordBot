package es.wolfteam.services.permissions;

import es.wolfteam.data.types.ActionType;
import net.dv8tion.jda.api.entities.Member;

public interface PermissionService
{
    boolean hasPermissionForAction(final Member member, final ActionType action);
}
