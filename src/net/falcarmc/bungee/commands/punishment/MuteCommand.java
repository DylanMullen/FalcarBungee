package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.falcarmc.bungee.util.TimeUtil;
import net.md_5.bungee.api.CommandSender;

public class MuteCommand extends FalcarCommand
{

	public MuteCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length < 2)
		{
			sendUsage();
			return;
		}
		
		String player = args[0];
		long till = -1;
		
		if(TimeUtil.parseDateOffset(args[1]) != -1)
		{
			till = System.currentTimeMillis() + TimeUtil.parseDateOffset(args[1]);
			if(args.length == 2)
			{
				sendMessage("You must include a reason!");
				return;
			}
		}
		
		StringBuilder bb = new StringBuilder();

		for(int i = (till == -1 ? 1 : 2); i < args.length; i++)
		{
			bb.append(args[i] + (args.length - 1 == (till == -1 ? 1 : 2) ? "" : " "));
		}
		
		PunishmentManager.getInstance().mute(getPlayer(), player, bb.toString(), till);
	}

}
