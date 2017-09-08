package net.falcarmc.bungee.manager;

import java.util.ArrayList;
import java.util.UUID;

import net.falcarmc.bungee.util.Config;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendManager
{

	static FriendManager m = new FriendManager();
	
	public static FriendManager getInstance()
	{
		return m;
	}
	
	public void addFriend(ProxiedPlayer pp, String name)
	{
		Config temp = ConfigManager.getManager().getUserConfig(name);
		Config cfg = ConfigManager.getManager().getUserConfig(pp.getUniqueId());

		if (temp == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		UUID uuid = UUID.fromString(temp.getConfig().getString("properties.uuid"));

		if (isFriend(uuid, cfg))
		{
			sendMessage(pp, "You have already added them to your friends list!");
			return;
		}

		ArrayList<String> friends = (ArrayList<String>) cfg.getConfig().getStringList("social.friends");
		friends.add(uuid.toString());
		cfg.getConfig().set("social.friends", friends);
		cfg.reloadConfig();

		if (BungeeCord.getInstance().getPlayer(uuid).isConnected())
		{
			sendMessage(BungeeCord.getInstance().getPlayer(uuid),
					ChatColor.YELLOW + pp.getName() + ChatColor.RESET + " has added you to their friends list!");
		}

		sendMessage(pp, "You have added " + ChatColor.YELLOW + temp.getConfig().getStringList("properties.name") + ChatColor.RESET
				+ " to your friends list!");
		return;

	}

	@SuppressWarnings("deprecation")
	public void sendFriendList(ProxiedPlayer pp)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(pp.getUniqueId());

		StringBuilder bb = new StringBuilder();
		for (int i = 0; i < cfg.getConfig().getStringList("social.friends").size(); i++)
		{
			UUID uuid = UUID.fromString(cfg.getConfig().getStringList("social.friends").get(i));
			bb.append(getFriendName(uuid) + (cfg.getConfig().getStringList("social.friends").size() - i == 0 ? "" : ChatColor.RESET + ", "));
		}

		if (bb.toString().isEmpty())
		{
			sendMessage(pp, "You have no friends yet!");
			return;
		}
		pp.sendMessage("Friends for " + ChatColor.YELLOW + pp.getName() + ChatColor.RESET + ":");
		pp.sendMessage(bb.toString());
	}

	public void removeFriend(ProxiedPlayer pp, String name)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(pp.getUniqueId());
		Config temp = ConfigManager.getManager().getUserConfig(name);
		
		if (temp == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		UUID uuid = UUID.fromString(temp.getConfig().getString("properties.uuid"));
		if(!isFriend(uuid, cfg))
		{
			sendMessage(pp, "This user is not on your friends list!");
			return;
		}
		
		ArrayList<String> friends = (ArrayList<String>) cfg.getConfig().getStringList("social.friends");
		friends.remove(uuid.toString());
		cfg.getConfig().set("social.friends", friends);
		cfg.reloadConfig();

		sendMessage(pp, "You have removed " + ChatColor.YELLOW + temp.getConfig().getStringList("properties.name") + ChatColor.RESET
				+ " from your friends list!");
	}

	public String getFriendName(UUID uuid)
	{
		ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(uuid);
		Config cfg = ConfigManager.getManager().getUserConfig(uuid);

		return (pp.isConnected() ? ChatColor.GREEN : ChatColor.RED) + cfg.getConfig().getString("properties.name");
	}

	public boolean isFriend(UUID uuid, Config cfg)
	{
		for (String us : cfg.getConfig().getStringList("social.friends"))
		{
			if (us.equalsIgnoreCase(uuid.toString()))
			{
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(ProxiedPlayer pp, String mes)
	{
		pp.sendMessage(MessageManager.LOGO + mes);
	}
	
}
