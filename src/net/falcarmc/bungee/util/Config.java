package net.falcarmc.bungee.util;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config
{
	private String name;
	private String path;
	private File configFile;
	private Configuration config;
	private ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
	
	public Config(String name, String path)
	{
		this.name = name;
		this.path = path;
		load();
	}
	
	public Config(String path)
	{
		this.name = "";
		this.path = path;
		load();
	}
	
	private void load()
	{
		configFile = new File(path);
		if(!configFile.getParentFile().exists())
			configFile.getParentFile().mkdirs();
		
		if(!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			config = provider.load(configFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveConfig()
	{
		try
		{
			provider.save(config, configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void reloadConfig()
	{
		try
		{
			provider.save(config, configFile);
			config = provider.load(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return name;
	}

	public String getPath()
	{
		return path;
	}

	public File getConfigFile()
	{
		return configFile;
	}

	public Configuration getConfig()
	{
		return config;
	}

	public ConfigurationProvider getProvider()
	{
		return provider;
	}
	
}