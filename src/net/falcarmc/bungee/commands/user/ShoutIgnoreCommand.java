package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.ConfigManager;
import net.falcarmc.bungee.util.Config;
import net.md_5.bungee.api.CommandSender;

public class ShoutIgnoreCommand extends FalcarCommand
{

	public ShoutIgnoreCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(cs.hasPermission("falcar.staff"))
		{
			sendMessage("As a Staff Member, you are not allowed to shoutignore players!");
			return;
		}
		
		if(args.length == 0)
		{
			sendUsage();
			return;
		}
		
		Config cfg = ConfigManager.getManager().getUserConfig(args[0]);
		if(cfg == null)
		{
			sendMessage("Player not found!");
			return;
		}
		
		String uuid = cfg.getConfig().getString("properties.uuid");
		
		getUser().ignore(uuid);
	}
}
