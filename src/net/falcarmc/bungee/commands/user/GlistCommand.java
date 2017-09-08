package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.UserManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GlistCommand extends FalcarCommand
{

	public GlistCommand(String name, String permission, String usage, String... aliases)
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
			sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
			
			for(String server : BungeeCord.getInstance().getServers().keySet())
			{
				sendUnformattedMessage(WEAK + BOLD + ChatColor.BOLD.toString() + server + ChatColor.GRAY + " >> " + WEAK + BungeeCord.getInstance().getServers().get(server).getPlayers().size() + " Online");
			}
			
			sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
			return;
		}
		
		if(BungeeCord.getInstance().getServerInfo(args[0]) != null)
		{
			sendUnformattedMessage(WEAK + args[0] + ":");
			
			StringBuilder bb = new StringBuilder();
			int i = 0;
			
			for(ProxiedPlayer pp : BungeeCord.getInstance().getServerInfo(args[0]).getPlayers())
			{
				if(UserManager.getManager().getUser(pp.getUniqueId()).isHidden())
					continue;
				
				
				bb.append(pp.getName() + ((BungeeCord.getInstance().getServerInfo(args[0]).getPlayers().size()-1) == i ? "" : WEAK + ", " + ChatColor.RESET));
			}
			
			sendUnformattedMessage((bb.toString().isEmpty() ? WEAK + "There are no players on this server!" : bb.toString()));
		}
		else
		{
			sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
			
			for(String server : BungeeCord.getInstance().getServers().keySet())
			{
				sendUnformattedMessage(WEAK + BOLD + ChatColor.BOLD.toString() + server + ChatColor.GRAY + " >> " + WEAK + BungeeCord.getInstance().getServers().get(server).getPlayers().size() + " Online");
			}
			
			sendUnformattedMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "---------------");
		}
	}

}
