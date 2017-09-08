package net.falcarmc.bungee.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import net.falcarmc.bungee.commands.punishment.BanCommand;
import net.falcarmc.bungee.commands.punishment.CheckCommand;
import net.falcarmc.bungee.commands.punishment.KickCommand;
import net.falcarmc.bungee.commands.punishment.MuteCommand;
import net.falcarmc.bungee.commands.punishment.UnbanCommand;
import net.falcarmc.bungee.commands.punishment.UnmuteCommand;
import net.falcarmc.bungee.commands.punishment.WarnCommand;
import net.falcarmc.bungee.commands.social.FriendCommand;
import net.falcarmc.bungee.commands.social.MessageCommand;
import net.falcarmc.bungee.commands.social.PartyCommand;
import net.falcarmc.bungee.commands.social.ReplyCommand;
import net.falcarmc.bungee.commands.staff.GVanishCommand;
import net.falcarmc.bungee.commands.staff.ShoutToggleCommand;
import net.falcarmc.bungee.commands.staff.SocialSpyCommand;
import net.falcarmc.bungee.commands.staff.StaffChatCommand;
import net.falcarmc.bungee.commands.user.GlistCommand;
import net.falcarmc.bungee.commands.user.ReportCommand;
import net.falcarmc.bungee.commands.user.ServerCommand;
import net.falcarmc.bungee.commands.user.ShoutCommand;
import net.falcarmc.bungee.commands.user.ShoutIgnoreCommand;
import net.falcarmc.bungee.commands.user.StaffCommand;
import net.falcarmc.bungee.commands.user.VoteCommand;
import net.falcarmc.bungee.listeners.PlayerListener;
import net.falcarmc.bungee.manager.ConfigManager;
import net.falcarmc.bungee.manager.MessageManager;
import net.falcarmc.bungee.manager.PunishmentManager;
import net.falcarmc.bungee.manager.SwearManager;
import net.falcarmc.bungee.manager.UserManager;
import net.falcarmc.bungee.runnables.AnnounceRunnable;
import net.falcarmc.bungee.runnables.PartyRunnable;
import net.falcarmc.bungee.util.Logger;
import net.falcarmc.bungee.util.Logger.LoggerType;
import net.falcarmc.bungee.util.User;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.command.ConsoleCommandSender;

public class Main extends Plugin
{

	static Main instance;
	
	private PluginManager pm = BungeeCord.getInstance().getPluginManager();
	private boolean shout = true;
	
	public void onEnable()
	{
		if (instance == null)
			instance = this;

		SwearManager.getInstance().load();
		ConfigManager.getManager().loadUserConfigs();
		
		
		loadListener();
		loadCommands();
		loadRunnables();
	}

	private void loadListener()
	{
		BungeeCord.getInstance().getPluginManager().registerListener(this, new PlayerListener());
	}

	private void loadCommands()
	{
		for(String s : BungeeCord.getInstance().getServers().keySet())
		{
			pm.registerCommand(this, new ServerCommand(s, "", ""));
		}
		
		// User Commands
		pm.registerCommand(this, new GlistCommand("glist", "falcar.user", ""));
		pm.registerCommand(this, new ReportCommand("report", "falcar.user", "/report <reason>"));
		pm.registerCommand(this, new ShoutCommand("shout", "falcar.user", "/shout <message>"));
		pm.registerCommand(this, new StaffCommand("staff", "falcar.user", "", "liststaff"));
		pm.registerCommand(this, new VoteCommand("vote","falcar.user",""));
		pm.registerCommand(this, new ShoutIgnoreCommand("shoutignore", "falcar.user", "/shoutignore <name>"));
		
		//Social
		pm.registerCommand(this, new FriendCommand("friend", "falcar.user", "/friend <add|list|remove> [player]"));
		pm.registerCommand(this, new PartyCommand("party", "falcar.user", "/party <create|join|leave|invite>"));
		pm.registerCommand(this, new MessageCommand("msg", "falcar.user", "/msg <name> <message>", "m", "t", "tell", "message", "whisper", "w", "minecraft:whisper"));
		pm.registerCommand(this, new ReplyCommand("reply", "falcar.user", "/reply <message>", "r", "essentials:reply", "er"));

		// Staff Commands
		pm.registerCommand(this, new GVanishCommand("gv", "falcar.staff", "/gvanish", "vanish", "v"));
		pm.registerCommand(this, new StaffChatCommand("sc", "falcar.staff", "/sc <message>", "staffchat", "schat"));
		pm.registerCommand(this, new SocialSpyCommand("socialspy", "falcar.admin", "/socialspy"));
		pm.registerCommand(this, new ShoutToggleCommand("shouttoggle", "falcar.admin", ""));
		
		// Punishment
		pm.registerCommand(this, new WarnCommand("warn", "falcar.staff", "/warn <player> <reason>"));
		pm.registerCommand(this, new KickCommand("kick", "falcar.staff", "/kick <player|all> <reason>"));
		pm.registerCommand(this, new MuteCommand("mute", "falcar.staff", "/mute <player> [time] <reason>"));
		pm.registerCommand(this, new UnmuteCommand("unmute", "falcar.staff", "/unmute <player>"));
		pm.registerCommand(this, new BanCommand("ban", "falcar.mod", "/ban <player> [time] [server] <reason>"));
		pm.registerCommand(this, new UnbanCommand("unban", "falcar.mod", "/unban <player> [server]"));
		pm.registerCommand(this, new CheckCommand("check", "falcar.staff", "/check [player] [kick|mute|ban|warn]"));
	}
	
	private void loadRunnables()
	{
		BungeeCord.getInstance().getScheduler().schedule(this, new AnnounceRunnable(), 90, TimeUnit.SECONDS);
		BungeeCord.getInstance().getScheduler().schedule(this, new PartyRunnable(), 1, TimeUnit.SECONDS);
	}
	
	public void onDisable()
	{
		BungeeCord.getInstance().getScheduler().cancel(this);
	}
	
	public int getStaffOnlineCount()
	{
		int i = 0;

		for (User u : UserManager.getManager().getUsers())
		{
			if (u.getPlayer().isConnected() && !u.isHidden())
			{
				i++;
			}
		}
		return i;
	}

	@SuppressWarnings("deprecation")
	public void sendShoutMessage(CommandSender cs, String mes)
	{
		boolean console = (cs instanceof ConsoleCommandSender);
		ProxiedPlayer p = null;
		if(isShoutToggled())
		{
			if (!console)
			{
				p = (ProxiedPlayer) cs;
				if (PunishmentManager.getInstance().isMuted(ConfigManager.getManager().getUserConfig(p.getUniqueId())))
				{
					String[] mute = PunishmentManager.getInstance().getMute(ConfigManager.getManager().getUserConfig(p.getUniqueId())).split(";");
					long t = Long.parseLong(mute[3]);
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(t);
					
					String time = ChatColor.YELLOW + "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
					+ ChatColor.RESET + " @ " + ChatColor.YELLOW + +cal.get(Calendar.HOUR_OF_DAY) + "h" + cal.get(Calendar.MINUTE) + "m "
					+ cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
					
					String expire = (Long.parseLong(mute[3]) == -1 ? "never ends" : "ends on the " + time);
					
					p.sendMessage(MessageManager.LOGO + "You are still muted which " + expire);
				}
			}
			
			String message = mes;
			
			if (SwearManager.getInstance().hasSwear(mes) && !console)
			{
				message = SwearManager.getInstance().getFilterMessages(mes);
				sendStaffMessage(String.format(MessageManager.STAFF_SWEAR_FORMAT, //
						p.getName(), //
						"shout", //
						SwearManager.getInstance().getStaffNotifcation(mes)));
			}
			
			for (ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
			{
				if(!console)
				{
					if(UserManager.getManager().getUser(pp.getUniqueId()).isIgnored(p.getUUID().toString()) && !p.hasPermission("falcar.staff"))
					{
						continue;
					}
				}
				pp.sendMessage(ChatColor.GRAY + "(" + ChatColor.GREEN + "G" + ChatColor.GRAY + "/" + ChatColor.AQUA
						+ (console ? "???" : p.getServer().getInfo().getName()) + ChatColor.GRAY + ") "
						+ (console ? ChatColor.AQUA + "" : UserManager.getManager().getUser(p.getUniqueId()).getPrefix())
						+ (console ? "Console" : p.getName()) + ChatColor.GRAY + " > " + ChatColor.RESET + message);
			}
			Logger.log(LoggerType.SHOUT, (console ? "Console" : p.getName()) + " > " + message);
		}
		else
		{
			cs.sendMessage(MessageManager.LOGO + "Shout has been toggled off!");
			return;
		}
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(ProxiedPlayer pp, String mes)
	{
		pp.sendMessage(MessageManager.LOGO + mes);
	}

	@SuppressWarnings("deprecation")
	public void sendUnformatedMessage(ProxiedPlayer pp, String mes)
	{
		pp.sendMessage(mes);
	}

	public ArrayList<User> getStaffOnline(boolean hidden)
	{
		ArrayList<User> staff = new ArrayList<>();

		for (User u : UserManager.getManager().getUsers())
		{
			if (u.getPlayer().isConnected() && u.isStaff())
			{
				if (u.isHidden() && hidden == false)
					continue;
				staff.add(u);
			}
		}
		return staff;
	}

	@SuppressWarnings("deprecation")
	public void sendSocialSpyMessage(String mes)
	{
		for(User u : UserManager.getManager().getUsers())
		{
			if(u.isSpying() && u.getPlayer().isConnected())
			{
				u.getPlayer().sendMessage(ChatColor.GRAY + "(Spy) " + mes);
			}
		}
		Logger.log(LoggerType.SOCIAL_SPY, mes);
	}
	
	public static Main getInstance()
	{
		return instance;
	}

	@SuppressWarnings("deprecation")
	public void sendStaffMessage(String string)
	{
		for (User u : getStaffOnline(true))
		{
			if (u.getPlayer().isConnected())
			{
				u.getPlayer().sendMessage(string);
			}
		}
	}
	
	public boolean isShoutToggled()
	{
		return shout;
	}
	
	public void toggleShout(boolean value)
	{
		shout = value;
	}
	
}
