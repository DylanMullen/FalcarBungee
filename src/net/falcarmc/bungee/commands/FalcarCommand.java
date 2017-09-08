package net.falcarmc.bungee.commands;

import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.manager.MessageManager;
import net.falcarmc.bungee.manager.UserManager;
import net.falcarmc.bungee.util.Logger;
import net.falcarmc.bungee.util.Logger.LoggerType;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public abstract class FalcarCommand extends Command
{

	private CommandSender cs;
	private String usage;
	
	protected String WEAK = ChatColor.YELLOW.toString(), BOLD = ChatColor.BOLD + "";
	
	public FalcarCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, aliases);
		this.usage = usage;
	}
	
	public abstract void run(CommandSender cs, String[] args);
	
	@Override
	public void execute(CommandSender cs, String[] args)
	{
		this.cs = cs;
		run(cs, args);
	}

	public void sendUsage()
	{
		sendMessage("Usage: " + usage);
	}
	
	@SuppressWarnings("deprecation")
	public void sendMessage(String mes)
	{
		cs.sendMessage(MessageManager.LOGO + mes);
	}
	
	@SuppressWarnings("deprecation")
	public void sendUnformattedMessage(String mes)
	{
		cs.sendMessage(mes);
	}
	
	public void sendTextComponent(TextComponent tc)
	{
		cs.sendMessage(tc);
	}
	
	@SuppressWarnings("deprecation")
	public void sendBroadcast(ProxiedPlayer pp, String mes)
	{
		pp.sendMessage(mes);
	}
	
	public void sendStaffChat(String mes)
	{
		boolean console = (cs instanceof ConsoleCommandSender);
		
		for(User u : Main.getInstance().getStaffOnline(true))
		{
			sendBroadcast(u.getPlayer(), String.format(MessageManager.STAFF_CHAT, (console ? "Console" : getPlayer().getName()), mes));
		}
		Logger.log(LoggerType.STAFFCHAT, (console ? "Console" : getPlayer().getName()) + " > " + mes);
	}
	
	public void sendStaffMessage(String mes)
	{
		for(User u : Main.getInstance().getStaffOnline(true))
		{
			if(u.getPlayer().isConnected())
			{
				sendBroadcast(u.getPlayer(), mes);
			}
		}
	}
	
	public boolean isPlayer()
	{
		return (cs instanceof ProxiedPlayer);
	}
	
	public ProxiedPlayer getPlayer()
	{
		return (ProxiedPlayer)cs;
	}
	
	public User getUser()
	{
		return UserManager.getManager().getUser(getPlayer().getUniqueId());
	}
	
}
