package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;

public class UnmuteCommand extends FalcarCommand
{

	public UnmuteCommand(String name, String permission, String usage, String... aliases)
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
		
		PunishmentManager.getInstance().unmute(getPlayer(), name);
	}

}
