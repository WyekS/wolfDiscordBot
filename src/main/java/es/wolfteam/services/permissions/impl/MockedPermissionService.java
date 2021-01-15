package es.wolfteam.services.permissions.impl;

import es.wolfteam.data.types.ActionType;
import es.wolfteam.services.permissions.PermissionService;
import net.dv8tion.jda.api.entities.Member;

public class MockedPermissionService implements PermissionService
{
    @Override
    public boolean hasPermissionForAction(final Member member, final ActionType action)
    {
        return member.getRoles().stream()
                .anyMatch(role -> role.getIdLong() == action.getRequiredLevel().getIdMocked());
    }
}
