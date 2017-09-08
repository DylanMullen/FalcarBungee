package net.falcarmc.bungee.commands.staff;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GVanishCommand extends FalcarCommand
{

	public GVanishCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		User u = getUser();
		
		u.setHidden(!u.isHidden());
		getPlayer().chat("/essentials:vanish " + (u.isHidden() ? "on" : "off"));
		
		sendMessage("You have globally " + (u.isHidden() ? "vanished" : "unvanished"));
		
		for(ProxiedPlayer pp : getPlayer().getServer().getInfo().getPlayers())
		{
			pp.sendMessage(ChatColor.YELLOW + getPlayer().getName() + " " + (u.isHidden() ? "left" : "joined") + " the Server!");
		}
	}


}
