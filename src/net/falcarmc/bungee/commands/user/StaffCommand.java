package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.manager.UserManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffCommand extends FalcarCommand
{

	public StaffCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if (!isPlayer())
			return;

		StringBuilder bb = new StringBuilder();

		sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
		sendUnformattedMessage("");

		int i = 1;
		for (ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
		{
			if (pp.hasPermission("falcar.staff"))
			{
				if(UserManager.getManager().getUser(pp.getUniqueId()).isHidden())
					continue;
				
				bb.append(pp.getName() + (Main.getInstance().getStaffOnlineCount() == i ? "" : WEAK + ", " + ChatColor.RESET));
				i++;
			}
		}

		sendUnformattedMessage((bb.toString().isEmpty() ? "There is no Staff online!" : WEAK + BOLD + "Staff Online: " + ChatColor.RESET + bb.toString()));
		sendUnformattedMessage("");
		sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
	}

}
