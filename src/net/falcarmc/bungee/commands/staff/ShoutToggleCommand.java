package net.falcarmc.bungee.commands.staff;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.util.Logger;
import net.falcarmc.bungee.util.Logger.LoggerType;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public class ShoutToggleCommand extends FalcarCommand
{

	public ShoutToggleCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender cs, String[] args)
	{

		boolean console = (cs instanceof ConsoleCommandSender);
		Main.getInstance().toggleShout(!Main.getInstance().isShoutToggled());
		
		for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers())
		{
			if(all.isConnected())
			{
				all.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + (console ? "Console" : getPlayer().getName()) + " has turned off Shout!");
			}
		}
		Logger.log(LoggerType.STAFF_INFO, "Shout has been toggled " + (Main.getInstance().isShoutToggled() ? "on" : "false"));
	}

}
