package net.falcarmc.bungee.commands.punishment;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.MessageManager;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;

public class CheckCommand extends FalcarCommand
{

	public CheckCommand(String name, String permission, String usage, String...aliases)
	{
		super(name, permission, usage, aliases);
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length == 0)
		{
			PunishmentManager.getInstance().check(getPlayer(), getPlayer().getName());
			return;
		}
		
		if(args[0].equalsIgnoreCase(getPlayer().getName()))
		{
			if(args.length == 1)
			{
				PunishmentManager.getInstance().check(getPlayer(), getPlayer().getName());
				return;
			}
			
			if(args[1].equalsIgnoreCase("kicks"))
			{
				PunishmentManager.getInstance().viewKicks(getPlayer(), getPlayer().getName());
				return;
			}
			else if(args[1].equalsIgnoreCase("mutes"))
			{
				PunishmentManager.getInstance().viewMutes(getPlayer(), getPlayer().getName());
				return;
			}
			else if(args[1].equalsIgnoreCase("bans"))
			{
				PunishmentManager.getInstance().viewBans(getPlayer(), getPlayer().getName());
				return;
			}
			else if(args[1].equalsIgnoreCase("warns"))
			{
				PunishmentManager.getInstance().viewWarns(getPlayer(), getPlayer().getName());
				return;
			}
			else
			{
				PunishmentManager.getInstance().check(getPlayer(), getPlayer().getName());
				return;
			}
		}
		else
		{
			if(!getPlayer().hasPermission("falcar.staff"))
			{
				sendMessage(MessageManager.NO_PERMISSION);
				return;
			}
			
			if(args.length == 1)
			{
				PunishmentManager.getInstance().check(getPlayer(), args[0]);
				return;
			}
			
			if(args[1].equalsIgnoreCase("kicks"))
			{
				PunishmentManager.getInstance().viewKicks(getPlayer(), args[0]);
				return;
			}
			else if(args[1].equalsIgnoreCase("mutes"))
			{
				PunishmentManager.getInstance().viewMutes(getPlayer(), args[0]);
				return;
			}
			else if(args[1].equalsIgnoreCase("bans"))
			{
				PunishmentManager.getInstance().viewBans(getPlayer(), getPlayer().getName());
				return;
			}
			else if(args[1].equalsIgnoreCase("warns"))
			{
				PunishmentManager.getInstance().viewWarns(getPlayer(), args[0]);
				return;
			}
			else
			{
				PunishmentManager.getInstance().check(getPlayer(), args[0]);
				return;
			}
		}
	}

}
