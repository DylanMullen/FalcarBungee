package net.falcarmc.bungee.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import net.falcarmc.bungee.core.Main;
import net.falcarmc.bungee.util.Config;

public class ConfigManager
{
	public ArrayList<Config> usersConfigs = new ArrayList<>();
	public ArrayList<Config> configs = new ArrayList<>();

	static ConfigManager m = new ConfigManager();

	public static ConfigManager getManager()
	{
		return m;
	}

	public Config createUserConfig(UUID uuid)
	{
		if (getUserConfig(uuid) != null)
		{
			return getUserConfig(uuid);
		}

		Config cfg = new Config(Main.getInstance().getDataFolder() + "/users/" + uuid.toString() + ".yml");

		usersConfigs.add(cfg);
		return cfg;
	}

	public void load()
	{
		loadUserConfigs();
	}

	public void loadUserConfigs()
	{
		File dir = new File(Main.getInstance().getDataFolder() + "/users");
		if (!dir.exists())
			dir.mkdirs();

		ArrayList<File> files = new ArrayList<File>(Arrays.asList(dir.listFiles()));

		for (int i = 0; i < files.size(); i++)
		{
			File f = files.get(i);
			if (f.getName().endsWith(".yml"))
			{
				Config cfg = new Config(f.getPath());
				usersConfigs.add(cfg);
			}
		}

	}

	// GETTING
	public Config getUserConfig(UUID uuid)
	{
		for (Config cfg : usersConfigs)
		{
			if (cfg.getConfig().getString("properties.uuid").equalsIgnoreCase(uuid.toString()))
			{
				return cfg;
			}
		}
		return null;
	}

	public Config getUserConfig(String name)
	{
		for (Config cfg : usersConfigs)
		{
			if (cfg.getConfig().getString("properties.name").equalsIgnoreCase(name))
			{
				return cfg;
			}
		}
		return null;
	}

}