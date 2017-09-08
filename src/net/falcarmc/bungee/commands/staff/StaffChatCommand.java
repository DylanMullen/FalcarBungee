package net.falcarmc.bungee.commands.staff;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.md_5.bungee.api.CommandSender;

public class StaffChatCommand extends FalcarCommand
{

	public StaffChatCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if (args.length == 0)
		{
			sendUsage();
			return;
		}

		StringBuilder bb = new StringBuilder();

		for (int i = 0; i < args.length; i++)
		{
			bb.append(args[i] + (args.length - i == 0 ? "" : " "));
		}

		sendStaffChat(bb.toString());
	}

}
