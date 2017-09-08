package net.falcarmc.bungee.commands.social;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.manager.UserManager;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageCommand extends FalcarCommand
{

	public MessageCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@SuppressWarnings("deprecation")
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

		ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(args[0]);
		
		if (pp == null)
		{
			sendMessage("Player not found!");
			return;
		}
		
		User u = UserManager.getManager().getUser(pp.getUniqueId());
		
		StringBuilder bb = new StringBuilder();
		
		for(int i = 1; i < args.length; i++)
		{
			bb.append(args[i] + (args.length - i == 1 ? "" : " "));
		}
		
		String mes = bb.toString();
		mes = ChatColor.translateAlternateColorCodes('&', mes);

		pp.sendMessage(ChatColor.GREEN + getPlayer().getName() + ChatColor.GRAY + " -> " + ChatColor.GREEN + "Me " + ChatColor.GRAY + "> " + ChatColor.RESET + mes);
		sendUnformattedMessage(ChatColor.GREEN + "Me" + ChatColor.GRAY + " -> " + ChatColor.GREEN + pp.getName() + ChatColor.GRAY + " > " + ChatColor.RESET + mes);
		
		u.setLastMessage(getPlayer().getUniqueId());
		getUser().setLastMessage(pp.getUniqueId());
		
		Main.getInstance().sendSocialSpyMessage(getPlayer().getName() + " -> " + pp.getName() + "> "  + mes);
	}
	

}
