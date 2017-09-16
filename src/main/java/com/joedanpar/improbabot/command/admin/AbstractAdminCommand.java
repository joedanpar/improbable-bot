package com.joedanpar.improbabot.command.admin;

import com.joedanpar.improbabot.command.AbstractCommand;

import static com.joedanpar.improbabot.util.RolePermission.BOT_ADMIN;

public abstract class AbstractAdminCommand extends AbstractCommand {
    public AbstractAdminCommand() {
        permissionLevel = BOT_ADMIN;
    }
}
