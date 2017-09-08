package net.falcarmc.bungee.listeners;

import java.util.Calendar;
import java.util.TimeZone;

import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.manager.ConfigManager;
import net.falcarmc.bungee.manager.MessageManager;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.falcarmc.bungee.manager.SwearManager;
import net.falcarmc.bungee.manager.UserManager;
import net.falcarmc.bungee.util.Config;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener
{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPostLogin(PostLoginEvent e)
	{
		if (UserManager.getManager().getUser(e.getPlayer().getUniqueId()) == null)
		{
			UserManager.getManager().createUser(e.getPlayer().getUniqueId());
		}

		if (e.getPlayer().hasPermission("falcar.staff"))
		{
			for (ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
			{
				if (pp.hasPermission("falcar.staff"))
				{
					pp.sendMessage(ChatColor.DARK_GRAY + "      " + ChatColor.STRIKETHROUGH + "------" + ChatColor.GREEN + " "
							+ e.getPlayer().getName() + " joined the game " + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "------");
				}
			}
		}

		Config cfg = ConfigManager.getManager().getUserConfig(e.getPlayer().getUniqueId());
		if(cfg != null)
		{
			if(!cfg.getConfig().getString("properties.name").equalsIgnoreCase(e.getPlayer().getName()))
			{
				cfg.getConfig().set("properties.name", e.getPlayer().getName());
				cfg.reloadConfig();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(LoginEvent e)
	{
		Config cfg = ConfigManager.getManager().getUserConfig(e.getConnection().getUniqueId());

		if (PunishmentManager.getInstance().isGlobalBanned(cfg))
		{
			String ban = PunishmentManager.getInstance().getBan(cfg, "all");
			String[] parts = ban.split(";");
			long till = Long.parseLong(parts[4]);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(till);

			String date = ChatColor.YELLOW + "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
					+ " @ " + cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m "
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
			String expire = (till == -1 ? "never expires" : "expires on the " + ChatColor.YELLOW + date);

			e.setCancelReason(ChatColor.RED + "You have been globally banned by " + ChatColor.YELLOW + parts[0] + ChatColor.RED + " for: "
					+ ChatColor.YELLOW + parts[1] + ChatColor.RED + " which " + expire);
			e.setCancelled(true);
		} else if (PunishmentManager.getInstance().isBannedOnAllServers(cfg))
		{
			e.setCancelReason(ChatColor.RED + "You have been banned on all Servers. To find out when it ends ask a Staff Member @ " + ChatColor.YELLOW
					+ "www.falcarmc.net");
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogout(PlayerDisconnectEvent e)
	{
		if (UserManager.getManager().getUser(e.getPlayer().getUniqueId()) != null)
		{
			UserManager.getManager().removeUser(UserManager.getManager().getUser(e.getPlayer().getUniqueId()));
		}

		if (e.getPlayer().hasPermission("falcar.staff"))
		{
			for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
			{
				if(pp.hasPermission("falcar.staff"))
				{
					pp.sendMessages(ChatColor.DARK_GRAY + "      " + ChatColor.STRIKETHROUGH + "------" + ChatColor.RED + "   " + e.getPlayer().getName()
					+ " left the game   " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------");
				}
			}
		
		}
		
	}

	@EventHandler
	public void onSwitchServer(ServerSwitchEvent e)
	{
		if (e.getPlayer().hasPermission("falcar.staff"))
		{
			if (UserManager.getManager().getUser(e.getPlayer().getUniqueId()).isHidden())
			{
				e.getPlayer().chat("/essentials:vanish on");
			} else
			{
				e.getPlayer().chat("/essentials:vanish off");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(ChatEvent e)
	{
		ProxiedPlayer pp = (ProxiedPlayer) e.getSender();

		if (!e.isCommand())
		{
			if (e.getMessage().startsWith("@"))
			{
				e.setCancelled(true);
				if (UserManager.getManager().getUser(pp.getUniqueId()).getParty() != null)
				{
					String mes = e.getMessage();
					mes = mes.replaceFirst("@", "");
					UserManager.getManager().getUser(pp.getUniqueId()).getParty()
					.sendMessage(ChatColor.LIGHT_PURPLE + pp.getName() + ChatColor.GRAY + " > " + ChatColor.RESET + mes);
					return;
				} else
				{
					pp.sendMessage(MessageManager.PARTY_LOGO + "You are not in a party!");
					return;
				}
			}
			
			if (checkMuted(pp))
			{
				e.setCancelled(true);
				return;
			}

			if (e.getMessage().startsWith("!"))
			{
				e.setCancelled(true);
				String mes = e.getMessage();
				mes = mes.replaceFirst("!", "");
				Main.getInstance().sendShoutMessage(pp, mes);
				return;
			}

			if (SwearManager.getInstance().hasSwear(e.getMessage()))
			{
				Main.getInstance().sendStaffMessage(String.format(MessageManager.STAFF_SWEAR_FORMAT, pp.getName(), pp.getServer().getInfo().getName(),
						SwearManager.getInstance().getStaffNotifcation(e.getMessage())));

				e.setMessage(SwearManager.getInstance().getFilterMessages(e.getMessage()));
			}

		} else
		{
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPingList(ProxyPingEvent e)
	{
		ServerPing ping = e.getResponse();
		ping.setDescription(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Falcar" + ChatColor.AQUA
				+ ChatColor.BOLD.toString() + "MC" + ChatColor.GOLD + ChatColor.BOLD + " 2.0! " + ChatColor.DARK_GRAY + ">> " + ChatColor.GREEN + ChatColor.BOLD + "Skyblock and Creative open!");
		e.setResponse(ping);
	}

	@SuppressWarnings("deprecation")
	private boolean checkMuted(ProxiedPlayer pp)
	{
		if (PunishmentManager.getInstance().isMuted(ConfigManager.getManager().getUserConfig(pp.getUniqueId())))
		{
			String[] mute = PunishmentManager.getInstance().getMute(ConfigManager.getManager().getUserConfig(pp.getUniqueId())).split(";");
			long t = Long.parseLong(mute[3]);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(t);

			String time = ChatColor.YELLOW + "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
					+ ChatColor.RESET + " @ " + ChatColor.YELLOW + +cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m "
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

			String expire = (Long.parseLong(mute[3]) == -1 ? "never ends" : "ends on the " + time);

			pp.sendMessage(MessageManager.LOGO + "You are still muted which " + expire);
			return true;
		}
		return false;
	}
}
