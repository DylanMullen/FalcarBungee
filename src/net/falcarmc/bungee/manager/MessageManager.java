package net.falcarmc.bungee.manager;

import net.md_5.bungee.api.ChatColor;

public class MessageManager
{

	public static String LOGO = ChatColor.YELLOW + ChatColor.BOLD.toString() + "Falcar" + ChatColor.AQUA
			+ ChatColor.BOLD.toString() + "MC" + ChatColor.DARK_GRAY + " >> " + ChatColor.RESET;

	public static String FRIENDS_LOGO = ChatColor.GREEN + ChatColor.BOLD.toString() + "Friends " + ChatColor.DARK_GRAY + ">> " + ChatColor.RESET;
	public static String SWEAR_LOGO = ChatColor.RED + ChatColor.BOLD.toString() + "Anti-Swear " + ChatColor.DARK_GRAY + ">> " + ChatColor.RESET;
	public static String PARTY_LOGO = ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Party " + ChatColor.DARK_GRAY + ">> " + ChatColor.RESET;
	
	public static String STAFF_CHAT = ChatColor.GRAY + "(" + ChatColor.DARK_GREEN + "Staff" + ChatColor.GRAY + ") " + ChatColor.DARK_GREEN + "%s " + ChatColor.GRAY + "> %s";
	
	public static String NO_PERMISSION = "You are missing a permission";

	public static String STAFF_REPORT_FORMAT = ChatColor.translateAlternateColorCodes('&', "&e%s&r is requesting help on &e%s&r:&e %s");
	public static String STAFF_SWEAR_FORMAT = ChatColor.translateAlternateColorCodes('&', SWEAR_LOGO + "&e%s&f swore on &e%s&f: %s");
}
