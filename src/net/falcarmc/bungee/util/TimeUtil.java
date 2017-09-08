package net.falcarmc.bungee.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil
{
	private static Pattern timePattern = Pattern.compile(
			"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
			2);

	public static long parseDateOffset(String time)
	{
		Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
				+ "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
				+ "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find())
		{
			if (m.group() == null || m.group().isEmpty())
			{
				continue;
			}
			for (int i = 0; i < m.groupCount(); i++)
			{
				if (m.group(i) != null && !m.group(i).isEmpty())
				{
					found = true;
					break;
				}
			}
			if (found)
			{
				if (m.group(1) != null && !m.group(1).isEmpty())
				{
					years = Integer.parseInt(m.group(1));
				}
				if (m.group(2) != null && !m.group(2).isEmpty())
				{
					months = Integer.parseInt(m.group(2));
				}
				if (m.group(3) != null && !m.group(3).isEmpty())
				{
					weeks = Integer.parseInt(m.group(3));
				}
				if (m.group(4) != null && !m.group(4).isEmpty())
				{
					days = Integer.parseInt(m.group(4));
				}
				if (m.group(5) != null && !m.group(5).isEmpty())
				{
					hours = Integer.parseInt(m.group(5));
				}
				if (m.group(6) != null && !m.group(6).isEmpty())
				{
					minutes = Integer.parseInt(m.group(6));
				}
				if (m.group(7) != null && !m.group(7).isEmpty())
				{
					seconds = Integer.parseInt(m.group(7));
				}
				break;
			}
		}
		if (!found)
		{
			return -1;
		}

		long timeDifference = 0;

		if (years > 0)
		{
			timeDifference += years * (365l * 24 * 60 * 60 * 1000);
		}
		if (months > 0)
		{
			timeDifference += months * (30l * 24 * 60 * 60 * 1000);
		}
		if (weeks > 0)
		{
			timeDifference += weeks * (7l * 24 * 60 * 60 * 1000);
		}
		if (days > 0)
		{
			timeDifference += days * (24l * 60 * 60 * 1000);
		}
		if (hours > 0)
		{
			timeDifference += hours * (60l * 60 * 1000);
		}
		if (minutes > 0)
		{
			timeDifference += minutes * (60l * 1000);
		}
		if (seconds > 0)
		{
			timeDifference += seconds * 1000l;
		}

		return timeDifference;
	}

	// TODO: Convert other classes using your version of parseDateDiff:
	public static long parseDateDiff(String time, boolean future) throws Exception
	{
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find())
		{
			if ((m.group() != null) && (!m.group().isEmpty()))
			{
				for (int i = 0; i < m.groupCount(); i++)
				{
					if ((m.group(i) != null) && (!m.group(i).isEmpty()))
					{
						found = true;
						break;
					}
				}
				if (found)
				{
					if ((m.group(1) != null) && (!m.group(1).isEmpty()))
					{
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty()))
					{
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty()))
					{
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty()))
					{
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty()))
					{
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty()))
					{
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) != null) && (!m.group(7).isEmpty()))
					{
						seconds = Integer.parseInt(m.group(7));
					}
				}
			}
		}
		if (!found)
		{
			return -666L;
		}
		Calendar c = new GregorianCalendar();
		if (years > 0)
		{
			c.add(1, years * (future ? 1 : -1));
		}
		if (months > 0)
		{
			c.add(2, months * (future ? 1 : -1));
		}
		if (weeks > 0)
		{
			c.add(3, weeks * (future ? 1 : -1));
		}
		if (days > 0)
		{
			c.add(5, days * (future ? 1 : -1));
		}
		if (hours > 0)
		{
			c.add(11, hours * (future ? 1 : -1));
		}
		if (minutes > 0)
		{
			c.add(12, minutes * (future ? 1 : -1));
		}
		if (seconds > 0)
		{
			c.add(13, seconds * (future ? 1 : -1));
		}
		Calendar max = new GregorianCalendar();
		max.add(1, 10);
		if (c.after(max))
		{
			return max.getTimeInMillis();
		}
		return c.getTimeInMillis();
	}

	public static String timeLeft(int timeoutSeconds)
	{
		int days = (int) (timeoutSeconds / 86400);
		int hours = (int) (timeoutSeconds / 3600) % 24;
		int minutes = (int) (timeoutSeconds / 60) % 60;
		int seconds = timeoutSeconds % 60;
		return ((days > 0 ? " " + days + " day" + (days != 1 ? "s" : "") : "") + (hours > 0 ? " " + hours + " hour" + (hours != 1 ? "s" : "") : "")
				+ (minutes > 0 ? " " + minutes + " minute" + (minutes != 1 ? "s" : "") : "")
				+ (seconds > 0 ? " " + seconds + " second" + (seconds != 1 ? "s" : "") : "")).trim();
	}
}