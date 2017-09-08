package net.falcarmc.bungee.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import net.falcarmc.bungee.util.Config;
import net.falcarmc.bungee.util.Logger;
import net.falcarmc.bungee.util.Logger.LoggerType;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PunishmentManager
{

	public enum PunishType
	{
		KICK, MUTE, BAN;
	}

	static PunishmentManager m = new PunishmentManager();
	private String IDENTIFY = ChatColor.YELLOW + "", WEAK = ChatColor.RESET + "", ACTIVE = ChatColor.GREEN + "";

	public static PunishmentManager getInstance()
	{
		return m;
	}

	public void warn(String name, String staff, String reason, long time, boolean mes)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(name);

		ArrayList<String> warns = (ArrayList<String>) cfg.getConfig().getStringList("punishments.warns");
		warns.add(staff + ";" + reason + ";" + time);
		cfg.getConfig().set("punishments.warns", warns);
		cfg.reloadConfig();

		if (BungeeCord.getInstance().getPlayer(name) != null)
		{
			sendMessage(BungeeCord.getInstance().getPlayer(name),
					"You have been warned by " + IDENTIFY + staff + WEAK + " for: " + IDENTIFY + reason);
		}

		if (mes)
			sendStaffMessage(IDENTIFY + staff + WEAK + " has warned " + IDENTIFY + name + WEAK + " for: " + IDENTIFY + reason);
	}

	@SuppressWarnings("deprecation")
	public void kick(ProxiedPlayer pp, String name, String reason)
	{
		if (name.equalsIgnoreCase("all"))
		{
			if (!pp.hasPermission("falcar.admin"))
			{
				sendMessage(pp, MessageManager.NO_PERMISSION);
				return;
			}

			for (ProxiedPlayer all : pp.getServer().getInfo().getPlayers())
			{
				if (all.getName().equalsIgnoreCase(pp.getName()))
					continue;

				all.disconnect(ChatColor.RED + "You have been kicked by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);
			}

			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has kicked everyone on " + IDENTIFY + pp.getServer().getInfo().getName() + WEAK
					+ " for: " + IDENTIFY + reason);
		} else
		{
			if (ConfigManager.getManager().getUserConfig(name) == null)
			{
				sendMessage(pp, "Player not found!");
				return;
			}

			if (!BungeeCord.getInstance().getPlayer(name).isConnected())
			{
				sendMessage(pp, "Player is not online!");
				return;
			}

			ProxiedPlayer t = BungeeCord.getInstance().getPlayer(name);

			Config cfg = ConfigManager.getManager().getUserConfig(name);

			ArrayList<String> warns = (ArrayList<String>) cfg.getConfig().getStringList("punishments.kicks");
			warns.add(pp.getName() + ";" + t.getServer().getInfo().getName() + ";" + reason + ";" + System.currentTimeMillis());
			cfg.getConfig().set("punishments.kicks", warns);
			cfg.reloadConfig();

			t.disconnect(ChatColor.RED + "You have been kicked by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);

			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has kicked " + name + " on " + IDENTIFY + t.getServer().getInfo().getName() + WEAK
					+ " for: " + IDENTIFY + reason);
		}
	}

	public void mute(ProxiedPlayer pp, String user, String reason, long timeTill)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		ArrayList<String> mutes = (ArrayList<String>) cfg.getConfig().getStringList("punishments.mutes");
		if (isMuted(cfg))
		{
			String mute = getMute(cfg);
			mutes.remove(mute);

			String[] parts = mute.split(";");
			mute = pp.getName() + ";" + reason + ";" + parts[2] + ";" + timeTill;
			mutes.add(mute);
			cfg.getConfig().set("punishments.mutes", mutes);
			cfg.reloadConfig();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timeTill);
			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " @ "
					+ cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
			String expire = (timeTill == -1 ? "never expires" : "expires on the " + IDENTIFY + date);

			if (BungeeCord.getInstance().getPlayer(name) != null)
			{
				sendMessage(BungeeCord.getInstance().getPlayer(name),
						IDENTIFY + pp.getName() + WEAK + " has updated your mute which " + expire + WEAK + " for: " + IDENTIFY + reason);
			}

			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has updated " + IDENTIFY + name + WEAK + "'s mute which " + expire + WEAK + " for: "
					+ IDENTIFY + reason);
			return;
		} else
		{
			mutes.add(pp.getName() + ";" + reason + ";" + System.currentTimeMillis() + ";" + timeTill);
			cfg.getConfig().set("punishments.mutes", mutes);
			cfg.reloadConfig();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timeTill);
			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " @ "
					+ cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
			String expire = (timeTill == -1 ? "never expires" : "expires on the " + IDENTIFY + date);

			if (BungeeCord.getInstance().getPlayer(name) != null)
			{
				sendMessage(BungeeCord.getInstance().getPlayer(name),
						IDENTIFY + pp.getName() + WEAK + " has muted you which " + expire + WEAK + " for: " + IDENTIFY + reason);
			}

			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has muted " + IDENTIFY + name + WEAK + " which " + expire + WEAK + " for: " + IDENTIFY
					+ reason);
			return;
		}
	}

	// Staff;reason;server;time;yill
	@SuppressWarnings("deprecation")
	public void ban(ProxiedPlayer pp, String user, String server, String reason, long till)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		ArrayList<String> bans = (ArrayList<String>) cfg.getConfig().getStringList("punishments.bans");

		if (isBanned(cfg, server))
		{
			String ban = getBan(cfg, server);
			bans.remove(ban);

			ban = pp.getName() + ";" + reason + ";" + server + ";" + System.currentTimeMillis() + ";" + till;
			bans.add(ban);
			cfg.getConfig().set("punishments.bans", bans);
			cfg.reloadConfig();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(till);
			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " @ "
					+ cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
			String expire = (till == -1 ? "never expires" : "expires on the " + IDENTIFY + date);
			String serverToUse = (server.equalsIgnoreCase("all") ? "all servers" : server);

			if(BungeeCord.getInstance().getPlayer(name) != null)
			{
				if (BungeeCord.getInstance().getPlayer(name).isConnected() && server.equalsIgnoreCase("all"))
				{
					BungeeCord.getInstance().getPlayer(name).disconnect(
							ChatColor.RED + "You have been globally banned by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);
				} else if (BungeeCord.getInstance().getPlayer(name).isConnected() && !server.equalsIgnoreCase("all"))
				{
					sendMessage(BungeeCord.getInstance().getPlayer(name), IDENTIFY + pp.getName() + WEAK + " has banned you on " + IDENTIFY + serverToUse
							+ WEAK + " which " + expire + WEAK + " for: " + IDENTIFY + reason);
				} else if (BungeeCord.getInstance().getPlayer(name).isConnected()
						&& BungeeCord.getInstance().getPlayer(name).getServer().getInfo().getName().equalsIgnoreCase(server))
				{
					BungeeCord.getInstance().getPlayer(name).disconnect(
							ChatColor.RED + "You have been banned by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);
				}
			}
			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has updated a ban on " + IDENTIFY + name + WEAK + " on " + IDENTIFY + serverToUse
					+ WEAK + " which " + expire + WEAK + " for: " + IDENTIFY + reason);
		} else
		{
			String ban = pp.getName() + ";" + reason + ";" + server + ";" + System.currentTimeMillis() + ";" + till;
			bans.add(ban);
			cfg.getConfig().set("punishments.bans", bans);
			cfg.reloadConfig();

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(till);
			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR) + " @ "
					+ cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
			String expire = (till == -1 ? "never expires" : "expires on the " + IDENTIFY + date);
			String serverToUse = (server.equalsIgnoreCase("all") ? "all servers" : server);
			
			if(BungeeCord.getInstance().getPlayer(name) != null)
			{
				if (BungeeCord.getInstance().getPlayer(name).isConnected() && server.equalsIgnoreCase("all"))
				{
					BungeeCord.getInstance().getPlayer(name).disconnect(
							ChatColor.RED + "You have been globally banned by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);
				} else if (BungeeCord.getInstance().getPlayer(name).isConnected() && !server.equalsIgnoreCase("all"))
				{
					sendMessage(BungeeCord.getInstance().getPlayer(name), IDENTIFY + pp.getName() + WEAK + " has banned you on " + IDENTIFY + serverToUse
							+ WEAK + " which " + expire + WEAK + " for: " + IDENTIFY + reason);
				} else if (BungeeCord.getInstance().getPlayer(name).isConnected()
						&& BungeeCord.getInstance().getPlayer(name).getServer().getInfo().getName().equalsIgnoreCase(server))
				{
					BungeeCord.getInstance().getPlayer(name).disconnect(
							ChatColor.RED + "You have been banned by " + IDENTIFY + pp.getName() + ChatColor.RED + " for: " + IDENTIFY + reason);
				}
			}
			sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has banned " + IDENTIFY + name + WEAK + " on " + IDENTIFY + serverToUse + WEAK
					+ " which " + expire + WEAK + " for: " + IDENTIFY + reason);
		}

	}

	public void removeWarn(ProxiedPlayer pp, String name, int index)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(name);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		ArrayList<String> warns = (ArrayList<String>) cfg.getConfig().getStringList("punishments.warns");

		if (warns == null || warns.size() == 0)
		{
			sendMessage(pp, "No warnings for this player!");
			return;
		}

		if (index > warns.size())
		{
			sendMessage(pp, "There is not that many warnings!");
			return;
		}

		if (index != 0)
			index--;

		warns.remove(index);
		cfg.getConfig().set("punishments.warns", warns);
		cfg.reloadConfig();

		sendMessage(pp, "Warning removed!");
		return;
	}

	public void unmute(ProxiedPlayer pp, String name)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(name);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (getMute(cfg) == null)
		{
			sendMessage(pp, "This player has no active Mutes!");
			return;
		}

		ArrayList<String> mutes = (ArrayList<String>) cfg.getConfig().getStringList("punishments.mutes");
		mutes.remove(getMute(cfg));
		cfg.getConfig().set("punishments.mutes", mutes);
		cfg.reloadConfig();

		if (BungeeCord.getInstance().getPlayer(name).isConnected())
			sendMessage(BungeeCord.getInstance().getPlayer(name), "You have been unmuted!");

		sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has unmuted " + IDENTIFY + cfg.getConfig().getString("properties.name"));
		return;
	}

	public void unban(ProxiedPlayer pp, String name, String server)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(name);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (!isBanned(cfg, server))
		{
			sendMessage(pp, "Player is not found on: " + IDENTIFY + (server.equalsIgnoreCase("all") ? "All servers" : server));
			return;
		}

		ArrayList<String> bans = (ArrayList<String>) cfg.getConfig().getStringList("punishments.bans");
		bans.remove(getBan(cfg, server));
		cfg.getConfig().set("punishments.bans", bans);
		cfg.reloadConfig();

		if (!server.equalsIgnoreCase("all") && BungeeCord.getInstance().getPlayer(name).isConnected())
		{
			sendMessage(BungeeCord.getInstance().getPlayer(name), "You have been unbanned on " + IDENTIFY + server);
		}

		sendStaffMessage(IDENTIFY + pp.getName() + WEAK + " has unbanned " + IDENTIFY + cfg.getConfig().getString("properties.name") + WEAK + " on "
				+ IDENTIFY + (server.equalsIgnoreCase("all") ? "all servers" : server));
	}

	@SuppressWarnings("deprecation")
	public void check(ProxiedPlayer pp, String user)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		pp.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "---------------");
		pp.sendMessage(IDENTIFY + "Name: " + WEAK + name);
		pp.sendMessage(getView(name, "Warns"));
		pp.sendMessage(getView(name, "Kicks"));
		pp.sendMessage(getView(name, "Mutes"));
		pp.sendMessage(getView(name, "Bans"));
		pp.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "---------------");
	}

	@SuppressWarnings("deprecation")
	public void viewWarns(ProxiedPlayer pp, String user)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (cfg.getConfig().getStringList("punishments.warns") == null || cfg.getConfig().getStringList("punishments.warns").size() == 0)
		{
			sendMessage(pp, "No warnings for this player!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		pp.sendMessage("Warnings for " + IDENTIFY + name);

		int index = 1;
		for (String warns : cfg.getConfig().getStringList("punishments.warns"))
		{
			String[] warn = warns.split(";");

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(warn[2]));

			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);

			pp.sendMessage(IDENTIFY + index + ") " + WEAK + "Warned by " + IDENTIFY + warn[0] + WEAK + " for: " + IDENTIFY + warn[1] + WEAK
					+ " on the " + IDENTIFY + date);
			index++;
		}
	}

	@SuppressWarnings("deprecation")
	public void viewKicks(ProxiedPlayer pp, String user)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (cfg.getConfig().getStringList("punishments.kicks") == null || cfg.getConfig().getStringList("punishments.kicks").size() == 0)
		{
			sendMessage(pp, "No kicks for this player!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		pp.sendMessage("Kicks for " + IDENTIFY + name);

		int index = 1;
		for (String kicks : cfg.getConfig().getStringList("punishments.kicks"))
		{
			String[] kick = kicks.split(";");

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(kick[3]));

			String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

			pp.sendMessage(IDENTIFY + index + ") " + WEAK + "Kicked by " + IDENTIFY + kick[0] + WEAK + " on " + IDENTIFY + kick[1] + WEAK + " for: "
					+ IDENTIFY + kick[2] + WEAK + " on the " + IDENTIFY + date);
			index++;
		}
	}

	@SuppressWarnings("deprecation")
	public void viewMutes(ProxiedPlayer pp, String user)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (cfg.getConfig().getStringList("punishments.mutes") == null || cfg.getConfig().getStringList("punishments.mutes").size() == 0)
		{
			sendMessage(pp, "No mutes for this player!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		pp.sendMessage("Mutes for " + IDENTIFY + name);

		int index = 1;
		Calendar cal = Calendar.getInstance();

		for (String mutes : cfg.getConfig().getStringList("punishments.mutes"))
		{
			String[] mute = mutes.split(";");
			cal.setTimeInMillis(Long.parseLong(mute[3]));

			String time = ChatColor.YELLOW + "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
					+ ChatColor.RESET + " @ " + ChatColor.YELLOW + +cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m "
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

			String expire = (Long.parseLong(mute[3]) == -1 ? "never ends" : "ends on the " + time);

			pp.sendMessage((isMuteString(cfg, mutes) ? ACTIVE : IDENTIFY) + index + ") " + (isMuteString(cfg, mutes) ? "" : WEAK) + "Muted by "
					+ IDENTIFY + mute[0] + (isMuteString(cfg, mutes) ? ACTIVE : WEAK) + " for: " + IDENTIFY + mute[1]
					+ (isMuteString(cfg, mutes) ? ACTIVE : WEAK) + " which " + expire);
			index++;
		}
	}

	// String ban = pp.getName() + ";" + reason + ";" + server + ";" +
	// System.currentTimeMillis() + ";" + till;

	@SuppressWarnings("deprecation")
	public void viewBans(ProxiedPlayer pp, String user)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(user);

		if (cfg == null)
		{
			sendMessage(pp, "Player not found!");
			return;
		}

		if (cfg.getConfig().getStringList("punishments.bans") == null || cfg.getConfig().getStringList("punishments.bans").size() == 0)
		{
			sendMessage(pp, "No bans for this player!");
			return;
		}

		String name = cfg.getConfig().getString("properties.name");

		pp.sendMessage("Bans for " + IDENTIFY + name);
		Calendar cal = Calendar.getInstance();

		int index = 1;
		for (String bans : cfg.getConfig().getStringList("punishments.bans"))
		{
			String[] ban = bans.split(";");
			cal.setTimeInMillis(Long.parseLong(ban[4]));

			String time = ChatColor.YELLOW + "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
					+ ChatColor.RESET + " @ " + ChatColor.YELLOW + +cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m "
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

			String expire = (Long.parseLong(ban[4]) == -1 ? "never ends" : "ends on the " + time);

			pp.sendMessage((isActiveBanString(cfg, bans) ? ACTIVE : IDENTIFY) + index + ") " + (isActiveBanString(cfg, bans) ? "" : WEAK)
					+ "Banned by " + IDENTIFY + ban[0] + (isActiveBanString(cfg, bans) ? ACTIVE : IDENTIFY) + " on " + IDENTIFY + ban[2]
					+ (isActiveBanString(cfg, bans) ? ACTIVE : WEAK) + " for: " + IDENTIFY + ban[1] + (isActiveBanString(cfg, bans) ? ACTIVE : WEAK)
					+ " which " + expire);
			index++;
		}
	}

	public boolean isActiveBanString(Config cfg, String s)
	{
		for (String bans : getActiveBans(cfg))
		{
			if (bans.equalsIgnoreCase(s))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isMuteString(Config cfg, String s)
	{
		if (getMute(cfg) == null)
			return false;
		if (getMute(cfg).equalsIgnoreCase(s))
			return true;
		else
			return false;
	}

	public TextComponent getView(String name, String arg)
	{
		TextComponent tc = new TextComponent("View " + arg);
		tc.setColor(ChatColor.YELLOW);
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/check " + name + " " + arg));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to view player " + arg).create()));
		return tc;
	}

	public boolean isMuted(Config cfg)
	{
		for (String mutes : cfg.getConfig().getStringList("punishments.mutes"))
		{
			long time = Long.parseLong(mutes.split(";")[3]);

			if (time == -1)
				return true;

			if (System.currentTimeMillis() <= time)
				return true;
		}
		return false;
	}

	public boolean isBanned(Config cfg, String server)
	{
		for (String bans : cfg.getConfig().getStringList("punishments.bans"))
		{
			String[] parts = bans.split(";");
			if (parts[2].equalsIgnoreCase(server) && Long.parseLong(parts[4]) <= System.currentTimeMillis())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isGlobalBanned(Config cfg)
	{
		for (String bans : cfg.getConfig().getStringList("punishments.bans"))
		{
			String[] parts = bans.split(";");
			if (parts[2].equalsIgnoreCase("all") && Long.parseLong(parts[4]) <= System.currentTimeMillis())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isBannedOnAllServers(Config cfg)
	{
		boolean result = false;
		for (String server : BungeeCord.getInstance().getServers().keySet())
		{
			for (String bans : cfg.getConfig().getStringList("punishments.bans"))
			{
				String[] parts = bans.split(";");
				if (parts[2].equalsIgnoreCase(server) && Long.parseLong(parts[4]) <= System.currentTimeMillis())
				{
					result = true;
				}
				result = false;
			}
		}
		return result;
	}

	private ArrayList<String> getActiveBans(Config cfg)
	{
		ArrayList<String> active = new ArrayList<>();

		for (String bans : cfg.getConfig().getStringList("punishments.bans"))
		{
			String[] parts = bans.split(";");
			if (Long.parseLong(parts[4]) <= System.currentTimeMillis())
			{
				active.add(bans);
			}
		}

		return active;
	}

	public String getBan(Config cfg, String server)
	{
		for (String bans : cfg.getConfig().getStringList("punishments.bans"))
		{
			String[] parts = bans.split(";");
			if (parts[2].equalsIgnoreCase(server) && Long.parseLong(parts[4]) <= System.currentTimeMillis())
			{
				return bans;
			}
		}
		return null;
	}

	public String getMute(Config cfg)
	{
		for (String mutes : cfg.getConfig().getStringList("punishments.mutes"))
		{
			long time = Long.parseLong(mutes.split(";")[3]);
			if (System.currentTimeMillis() <= time || time == -1)
			{
				return mutes;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void sendStaffMessage(String mes)
	{
		for (ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
		{
			if (pp.hasPermission("falcar.staff"))
			{
				pp.sendMessage(MessageManager.LOGO + mes);
			}
		}
		Logger.log(LoggerType.STAFF_INFO, ChatColor.stripColor(mes));
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(ProxiedPlayer pp, String mes)
	{
		pp.sendMessage(MessageManager.LOGO + mes);
	}
}
