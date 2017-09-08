package net.falcarmc.bungee.commands.social;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.FriendManager;
import net.md_5.bungee.api.CommandSender;

public class FriendCommand extends FalcarCommand
{


	public FriendCommand(String name, String permission, String usage, String... aliases)
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
			sendUsage();
			return;
		}
		
		if(args[0].equalsIgnoreCase("add"))
		{
			if(args.length == 1)
			{
				sendMessage("You must include a name!");
				return;
			}
			FriendManager.getInstance().addFriend(getPlayer(), args[1]);
			return;
		}
		else if(args[0].equalsIgnoreCase("list"))
		{
			FriendManager.getInstance().sendFriendList(getPlayer());	
			return;
		}
		else if(args[0].equalsIgnoreCase("remove"))
		{
			if(args.length == 1)
			{
				sendMessage("You must include a name!");
				return;
			}
			FriendManager.getInstance().removeFriend(getPlayer(), args[1]);
			return;
		}
		else
		{
			sendUsage();
			return;
		}
	}


}
