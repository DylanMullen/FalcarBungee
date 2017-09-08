package net.falcarmc.bungee.runnables;

import java.util.ArrayList;
import java.util.Random;

import net.falcarmc.bungee.manager.MessageManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AnnounceRunnable implements Runnable
{

	private ArrayList<TextComponent> globalMessages = new ArrayList<>();
	private ArrayList<TextComponent> creativeMessages = new ArrayList<>();
	private ArrayList<TextComponent> skyblockMessages = new ArrayList<>();
	
	private Random r = new Random();
	private int x = 0;
	
	public AnnounceRunnable()
	{
		globalMessages.add(getMessage("Click me to check out our other Servers!", "Click to view Glist!", "/glist", false));
		globalMessages.add(getMessage("Want to Vote for us? Click me!", "Click to view our Vote links!", "/vote", false));
		globalMessages.add(getMessage("Need Staff help? Use '/modreq' to get Staff help!"));
		globalMessages.add(getMessage("Visit the Forums! Click me!", "Click to go to the Forums!", "www.falcarmc.net", true));
		globalMessages.add(getMessage("Visit our Twitter! Click me!", "Click to go to our Twitter!", "www.twitter/falcarmc", true));
		globalMessages.add(getMessage("Donate to us! Click to view our Perks!", "Click to go to our Donation page!", "www.falcarmc.buycraft.net", true));
		
		creativeMessages.add(getMessage("To get a new plot for $1000, do '/plot auto'!"));
		creativeMessages.add(getMessage("Enter our 'Builder of the Week' competition! Click me!", "Click to submit your work!", "www.falcarmc.net/builder_of_week", true));
		creativeMessages.add(getMessage("Vote for us for W/E! Click me!", "Click to view our Vote links!", "/vote", false));
		
		skyblockMessages.add(getMessage("Vote for us for $10k! Click me", "Click to view our Vote links!", "/vote", false));
		
	}
	
	@Override
	public void run()
	{
		for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
		{
			if(!pp.isConnected())
				continue;
			
			if(x == 0)
			{
				pp.sendMessage(getRandomGlobal());
			}
			else
			{
				pp.sendMessage(getRandomText(pp.getServer().getInfo()));
			}
		}
		
		if(x == 3)
			x = 0;
		else
			x++;
	}
	
	public TextComponent getMessage(String text)
	{
		return new TextComponent(MessageManager.LOGO + text);
	}
	
	public TextComponent getMessage(String text, String hover, String click, boolean link)
	{
		TextComponent tc = new TextComponent(MessageManager.LOGO + text);
		tc.setClickEvent(new ClickEvent((link ? ClickEvent.Action.OPEN_URL : ClickEvent.Action.RUN_COMMAND), click));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		return tc;
	}
	
	public TextComponent getRandomText(ServerInfo info)
	{
		int i = 0;
		switch(info.getName().toLowerCase())
		{
			case "creative":
				i = r.nextInt(creativeMessages.size())-1;
				return creativeMessages.get(i);
			case "skyblock":
				return skyblockMessages.get(0);
		}
		return null;
	}
	
	public TextComponent getRandomGlobal()
	{
		return globalMessages.get((r.nextInt(globalMessages.size()-1)));
	}
}
