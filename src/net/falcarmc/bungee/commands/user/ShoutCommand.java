package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.core.Main;
import net.md_5.bungee.api.CommandSender;

public class ShoutCommand extends FalcarCommand
{

	public ShoutCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(args.length == 0)
		{
			sendUsage();
			return;
		}
		
		StringBuilder bb = new StringBuilder();
		
		for(int i = 0; i < args.length; i++)
		{
			bb.append(args[i] + (args.length - i == 0 ? "" : " "));
		}
		
		Main.getInstance().sendShoutMessage(cs, bb.toString());
	}

}
