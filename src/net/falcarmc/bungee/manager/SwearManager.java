package net.falcarmc.bungee.manager;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class SwearManager
{

	static SwearManager m = new SwearManager();
	
	private HashMap<String, String> swears = new HashMap<>();

	public static SwearManager getInstance()
	{
		return m;
	}
	
	public void load()
	{
		swears.put("fu(ck|k)(|(a|er))", "fudge");
		swears.put("sh(i|1)t", "crap");
		swears.put("b(1|i)tch", "dog");
		swears.put("\b(a|4)(s|$|5)(s|$|5)\b", "donkey");
		
		swears.put("v(a|4)g(i|1)n(a|4)", "vegtable");
		swears.put("cunt", "vegtable");			
		swears.put("c(l|1)(i|1)t", "vegtable");	
		swears.put("\bd(i|1)(ck|k)\b", "sausage");
		swears.put("t(i|1)t(ty|s|ties)", "nip naps");
		swears.put("b(o|0)n(e|3)r", "wood");
		swears.put("cum", "milk");
		swears.put("b(o|0)ll(o|0)(ck|k)s", "nuts");
		swears.put("b(a|4)lls(a|4)(ck|k)", "nuts");
		swears.put("\bs(e|3)x\b", "pillow fight");
		swears.put("w(a|4)nk", "tug");
		swears.put("f(i|1)ng(e|3)r(i|1)ng", "tickling");
		swears.put("p(e|3)n(1|i)(s|5|$)", "sausage");
		
		swears.put("g(a|4)(y|yl(o|0)rd)", "happy");
		swears.put("f(a|4)gg(o|0)t", "happy");
		swears.put("s(l|1)ut", "lady");
		swears.put("tw(a|4)t", "lady");

		swears.put("n(i|1)g(ger|a|g(a|4)|(e|3)r)", "african");
		swears.put("n(e|3)gr(o|0)", "african");
		swears.put("c(o|0)(o|0)n", "african");
		swears.put("ch(i|1)(nk|nky)", "chinese");
		swears.put("p(a|4)k(i|1)", "pakistani");

	}

	public boolean hasSwear(String mes)
	{
		boolean found = false;

		for (String regex : swears.keySet())
		{
			for(String word : mes.split(" "))
			{
				Pattern pat = Pattern.compile("(?i)" + regex);
				Matcher m = pat.matcher(word);
				
				if (m.find())
				{
					found = true;
				}
			}
		}

		return found;
	}

	public String getStaffNotifcation(String mes)
	{
		String temp = mes;
		for (String regex : swears.keySet())
		{
			Pattern pat = Pattern.compile("(?i)" + regex);
			Matcher m = pat.matcher(mes);

			if (m.find())
			{
				String word = m.group();
				temp = temp.replaceAll("(?i)" + regex, ChatColor.GOLD + word + ChatColor.RESET);
			}
		}
		return temp;
	}

	public String getFilterMessages(String mes)
	{
		String temp = mes;
		for (String regex : swears.keySet())
		{
			Pattern pat = Pattern.compile("(?i)" + regex);
			Matcher m = pat.matcher(mes);

			if (m.find())
			{
				temp = temp.replaceAll("(?i)" + regex, swears.get(regex));
			}
		}
		return temp;
	}
	
}
