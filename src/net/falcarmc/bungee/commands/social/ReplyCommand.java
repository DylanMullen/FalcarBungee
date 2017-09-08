package net.falcarmc.bungee.commands.social;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.manager.UserManager;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ReplyCommand extends FalcarCommand
{

	public ReplyCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(args.length == 0)
		{
			sendUsage();
			return;
		}
		
		if(getUser().getLastMessage() == null || !BungeeCord.getInstance().getPlayer(getUser().getLastMessage()).isConnected())
		{
			sendMessage("Player not found!");
			return;
		}
		
		StringBuilder bb = new StringBuilder();
		
		for(int i = 0; i < args.length; i++)
		{
			bb.append(args[i] + (args.length - i == 0 ? "" : " "));
		}
		
		String mes = bb.toString();
		mes = ChatColor.translateAlternateColorCodes('&', mes);
		
		User us = UserManager.getManager().getUser(getUser().getLastMessage());
		
		us.getPlayer().sendMessage(ChatColor.GREEN + getPlayer().getName() + ChatColor.GRAY + " -> " + ChatColor.GREEN + "Me " + ChatColor.GRAY + "> " + ChatColor.RESET + mes);
		sendUnformattedMessage(ChatColor.GREEN + "Me" + ChatColor.GRAY + " -> " + ChatColor.GREEN + us.getPlayer().getName() + ChatColor.GRAY + " > " + ChatColor.RESET + mes);	
		
		us.setLastMessage(getPlayer().getUniqueId());
		getUser().setLastMessage(us.getPlayer().getUniqueId());
		
		Main.getInstance().sendSocialSpyMessage(getPlayer().getName() + " -> " + us.getPlayer().getName() + "> "  + mes);
	}

}
