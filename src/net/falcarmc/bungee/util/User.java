package net.falcarmc.bungee.util;

import java.util.ArrayList;
import java.util.UUID;

import net.falcarmc.bungee.manager.ConfigManager;
import net.falcarmc.bungee.manager.MessageManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class User
{

	private UUID uuid;
	private UUID lastMessage;
	private boolean hidden;
	private boolean spy;
	private Party party;

	public User(UUID uuid)
	{
		this.uuid = uuid;
		this.hidden = false;
		this.spy = false;

		loadConfig();
	}

	public void loadConfig()
	{
		Config cfg = ConfigManager.getManager().createUserConfig(uuid);

		boolean reload = false;

		if (cfg.getConfig().getString("properties.name").isEmpty())
		{
			cfg.getConfig().set("properties.name", getPlayer().getName());
			reload = true;
		}
		if (cfg.getConfig().getString("properties.uuid").isEmpty())
		{
			cfg.getConfig().set("properties.uuid", uuid.toString());
			reload = true;
		}
		if (cfg.getConfig().getString("properties.hidden").isEmpty())
		{
			cfg.getConfig().set("properties.hidden", false);
			reload = true;
		}
		if (cfg.getConfig().getString("properties.spying").isEmpty())
		{
			cfg.getConfig().set("properties.spying", false);
			reload = true;
		}
		if (cfg.getConfig().getString("properties.ignored").isEmpty())
		{
			cfg.getConfig().set("properties.ignored", new ArrayList<String>());
			reload = true;
		}
		if (cfg.getConfig().getStringList("punishments.warns").isEmpty())
		{
			cfg.getConfig().set("punishments.warns", new ArrayList<String>());
			reload = true;
		}
		if (cfg.getConfig().getStringList("punishments.kicks").isEmpty())
		{
			cfg.getConfig().set("punishments.kicks", new ArrayList<String>());
			reload = true;
		}

		if (reload)
			cfg.reloadConfig();
	}

	public String getPrefix()
	{
		if (getPlayer().hasPermission("falcar.owner"))
		{
			return ChatColor.GRAY + "(" + ChatColor.YELLOW + "Owner" + ChatColor.GRAY + ") " + ChatColor.YELLOW;
		} else if (getPlayer().hasPermission("falcar.admin"))
		{
			return ChatColor.GRAY + "(" + ChatColor.RED + "Admin" + ChatColor.GRAY + ") " + ChatColor.RED;
		} else if (getPlayer().hasPermission("falcar.dev"))
		{
			return ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "Developer" + ChatColor.GRAY + ") " + ChatColor.DARK_AQUA;
		} else if (getPlayer().hasPermission("falcar.mod"))
		{
			return ChatColor.GRAY + "(" + ChatColor.AQUA + "Mod" + ChatColor.GRAY + ") " + ChatColor.AQUA;
		} else if (getPlayer().hasPermission("falcar.helper"))
		{
			return ChatColor.GRAY + "(" + ChatColor.DARK_AQUA + "Helper" + ChatColor.GRAY + ") " + ChatColor.DARK_AQUA;
		} else
		{
			return ChatColor.AQUA.toString();
		}
	}

	public void setHidden(boolean value)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(uuid);
		cfg.getConfig().set("properties.hidden", value);
		cfg.reloadConfig();
		hidden = value;
	}

	public ProxiedPlayer getPlayer()
	{
		return BungeeCord.getInstance().getPlayer(uuid);
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public boolean isHidden()
	{
		return hidden;
	}

	public boolean isStaff()
	{
		return getPlayer().hasPermission("falcar.staff");
	}

	public void setLastMessage(UUID lastMessage)
	{
		this.lastMessage = lastMessage;
	}
	
	public UUID getLastMessage()
	{
		return lastMessage;
	}

	public Party getParty()
	{
		return party;
	}

	public void setParty(Party party)
	{
		this.party = party;
	}
	
	public void toggleSpy(boolean value)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(uuid);
		cfg.getConfig().set("properties.spying", value);
		cfg.reloadConfig();
		
		this.spy = value;
	}
	
	public boolean isSpying()
	{
		return spy;
	}

	@SuppressWarnings("deprecation")
	public void ignore(String uuid)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(this.uuid);
		Config temp = ConfigManager.getManager().getUserConfig(UUID.fromString(uuid));
		ArrayList<String> ignored = (ArrayList<String>) cfg.getConfig().getStringList("properties.ignored");
		
		if(isIgnored(uuid))
		{
			ignored.remove(uuid);
			cfg.getConfig().set("properties.ignored", ignored);
			cfg.reloadConfig();
			
			getPlayer().sendMessage(MessageManager.LOGO + "You have unignored " + ChatColor.YELLOW + temp.getConfig().getString("properties.name"));
		}
		else
		{
			ignored.add(uuid);
			cfg.getConfig().set("properties.ignored", ignored);
			cfg.reloadConfig();
			
			getPlayer().sendMessage(MessageManager.LOGO + "You have ignored " + ChatColor.YELLOW + temp.getConfig().getString("properties.name"));
		}
	}
	
	public boolean isIgnored(String uuid)
	{
		for(String s : ConfigManager.getManager().getUserConfig(this.uuid).getConfig().getStringList("properties.ignored"))
		{
			if(s.equalsIgnoreCase(uuid))
				return true;
		}
		return false;
	}
}