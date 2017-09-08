package net.falcarmc.bungee.util;

public class Logger
{

	public enum LoggerType
	{
		STAFFCHAT("Staff Chat"),
		SHOUT("Shout"),
		SWEAR("Swear"),
		STAFF_INFO("Staff Info"),
		SOCIAL_SPY("Social Spy");
		
		private String prefix;
		
		LoggerType(String prefix)
		{
			this.prefix = prefix;
		}
		
		public String getPrefix()
		{
			return "[" + prefix + "] ";
		}
	}

	private Logger() {}
	
	public static void log(LoggerType type, String mes)
	{
		System.out.println(type.getPrefix() + mes);
	}
	
	public static void error(LoggerType type, String mes)
	{
		System.err.println(type.getPrefix() + mes);
	}
	
}
