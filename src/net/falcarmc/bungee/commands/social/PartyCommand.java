package net.falcarmc.bungee.commands.social;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.falcarmc.bungee.manager.PartyManager;
import net.md_5.bungee.api.CommandSender;

public class PartyCommand extends FalcarCommand
{

	public PartyCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}

	// /party <create|invite|leave>
	
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
		
		switch(args[0].toLowerCase())
		{
			case "create":
				PartyManager.getInstance().createNewParty(getPlayer());
				break;
			case "leave":
				PartyManager.getInstance().leaveParty(getPlayer());
				break;
			case "invite":
				PartyManager.getInstance().invitePlayer(getPlayer(), args);
				break;
			case "join":
				PartyManager.getInstance().joinParty(getPlayer(), args);
				break;
			default:
				sendUsage();
				break;
		}
	}

}
