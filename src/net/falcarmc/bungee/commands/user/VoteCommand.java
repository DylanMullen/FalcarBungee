package net.falcarmc.bungee.commands.user;

import net.falcarmc.bungee.commands.FalcarCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteCommand extends FalcarCommand
{

	public VoteCommand(String name, String permission, String usage, String... aliases)
	{
		super(name, permission, usage, aliases);
	}
	
	@Override
	public void run(CommandSender cs, String[] args)
	{
		if(!isPlayer())
			return;
		
		sendUnformattedMessage("Our Vote Links:");
		sendTextComponent(getVoteLink("MCServers.online", "http://bit.ly/2fzORY5"));
		sendTextComponent(getVoteLink("Minecraft-MP", "http://bit.ly/2wCWyRv"));
		sendTextComponent(getVoteLink("MinecraftServers", "http://bit.ly/2uPuxns"));
		sendTextComponent(getVoteLink("Minecraft-Server-List", "http://bit.ly/2fA6neP"));
	}
	
	public TextComponent getVoteLink(String name, String URL)
	{
		TextComponent tc = new TextComponent(name);
		tc.setColor(ChatColor.YELLOW);
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, URL));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to go to the Link!").create()));
		return tc;
	}

}
