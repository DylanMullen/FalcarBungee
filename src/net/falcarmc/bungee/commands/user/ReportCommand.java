package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.MessageManager;
import net.md_5.bungee.api.CommandSender;

public class ReportCommand extends FalcarCommand
{

	public ReportCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if (!isPlayer())
			return;

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

		if (bb.toString().length() < 5)
		{
			sendMessage("To report something, you must have more than 5 characters!");
			return;
		}

		sendStaffMessage(MessageManager.LOGO + String.format(MessageManager.STAFF_REPORT_FORMAT, //
				getPlayer().getName(), getPlayer().getServer().getInfo().getName(), //
				bb.toString()));
	}

}
