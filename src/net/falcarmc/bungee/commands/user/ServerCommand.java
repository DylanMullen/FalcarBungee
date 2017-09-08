package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

public class ServerCommand extends FalcarCommand
{

	public ServerCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		if(getPlayer().getServer().getInfo().getName().equalsIgnoreCase(getName()))
		{
			sendMessage("You are already on this server!");
			return;
		}
		
		getPlayer().connect(BungeeCord.getInstance().getServerInfo(getName()));
	}

}
