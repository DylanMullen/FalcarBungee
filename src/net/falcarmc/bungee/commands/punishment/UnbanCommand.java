package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

public class UnbanCommand extends FalcarCommand
{

	public UnbanCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length < 1)
		{
			sendUsage();
			return;
		}
		
		String name = args[0];
		String server = "all";
		
		if(args.length > 1)
			if(BungeeCord.getInstance().getServerInfo(args[1]) != null)
				server = args[1];
		
		PunishmentManager.getInstance().unban(getPlayer(), name, server);
	}

}
