/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.Mythicdrops;

import com.conventnunnery.plugins.Mythicdrops.builders.SocketGemBuilder;
import com.conventnunnery.plugins.Mythicdrops.command.MythicDropsCommand;
import com.conventnunnery.plugins.Mythicdrops.listeners.EntityListener;
import com.conventnunnery.plugins.Mythicdrops.listeners.ItemListener;
import com.conventnunnery.plugins.Mythicdrops.managers.ConfigurationManager;
import com.conventnunnery.plugins.Mythicdrops.managers.DropManager;
import com.conventnunnery.plugins.Mythicdrops.managers.EntityManager;
import com.conventnunnery.plugins.Mythicdrops.managers.ItemManager;
import com.conventnunnery.plugins.Mythicdrops.managers.LanguageManager;
import com.conventnunnery.plugins.Mythicdrops.managers.NameManager;
import com.conventnunnery.plugins.Mythicdrops.managers.SocketGemManager;
import com.conventnunnery.plugins.Mythicdrops.managers.TierManager;
import com.modcrafting.diablodrops.builders.CustomBuilder;
import com.modcrafting.diablodrops.builders.TierBuilder;
import net.h31ix.updater.Updater;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.Random;

public class MythicDrops extends JavaPlugin implements Listener {

	private static MythicDrops instance;
	private ConfigurationManager configurationManager;
	private PluginSettings pluginSettings;
	private TierManager tierManager;
	private NameManager nameManager;
	private ItemManager itemManager;
	private DropManager dropManager;
	private EntityManager entityManager;
	private SocketGemManager socketGemManager;
	private TierBuilder tierBuilder;
	private CustomBuilder customBuilder;
	private SocketGemBuilder socketGemBuilder;
	private Debugger debug;
	private Random random = new Random();
	private LanguageManager languageManager;

	public static MythicDrops getInstance() {
		return instance;
	}

	public com.conventnunnery.plugins.Mythicdrops.managers.LanguageManager getLanguageManager() {
		return languageManager;
	}

	public com.conventnunnery.plugins.Mythicdrops.builders.SocketGemBuilder getSocketGemBuilder() {
		return socketGemBuilder;
	}

	public com.conventnunnery.plugins.Mythicdrops.managers.SocketGemManager getSocketGemManager() {
		return socketGemManager;
	}

	public TierBuilder getTierBuilder() {
		return tierBuilder;
	}

	public CustomBuilder getCustomBuilder() {
		return customBuilder;
	}

	public Random getRandom() {
		return random;
	}

	public com.conventnunnery.plugins.Mythicdrops.managers.ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/**
	 * @return the debug
	 */
	public Debugger getDebug() {
		return debug;
	}

	/**
	 * @return the dropManager
	 */
	public com.conventnunnery.plugins.Mythicdrops.managers.DropManager getDropManager() {
		return dropManager;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @return the itemManager
	 */
	public com.conventnunnery.plugins.Mythicdrops.managers.ItemManager getItemManager() {
		return itemManager;
	}

	/**
	 * @return the nameManager
	 */
	public com.conventnunnery.plugins.Mythicdrops.managers.NameManager getNameManager() {
		return nameManager;
	}

	public com.conventnunnery.plugins.Mythicdrops.PluginSettings getPluginSettings() {
		return pluginSettings;
	}

	/**
	 * @return the tierManager
	 */
	public com.conventnunnery.plugins.Mythicdrops.managers.TierManager getTierManager() {
		return tierManager;
	}

	@Override
	public void onEnable() {
		instance = this;
		debug = new Debugger(this);
		configurationManager = new ConfigurationManager(this);
		pluginSettings = new com.conventnunnery.plugins.Mythicdrops.PluginSettings(this);
		pluginSettings.loadPluginSettings();
		languageManager = new LanguageManager(this);
		tierManager = new TierManager(this);
		nameManager = new NameManager(this);
		itemManager = new ItemManager(this);
		dropManager = new DropManager(this);
		entityManager = new EntityManager(this);
		socketGemManager = new SocketGemManager(this);
		tierBuilder = new TierBuilder(this);
		customBuilder = new CustomBuilder(this);
		socketGemBuilder = new SocketGemBuilder(this);
		tierBuilder.build();
		customBuilder.build();
		socketGemBuilder.build();
		if (pluginSettings.isDebugOnStartup()) {
			pluginSettings.debugSettings();
			getNameManager().debugPrefixesAndSuffixes();
			getTierManager().debugTiers();
			getDropManager().debugCustomItems();
			getSocketGemManager().debugSocketGems();
		}
		getCommand("Mythicdrops").setExecutor(new MythicDropsCommand(this));
		getServer().getPluginManager().registerEvents(new EntityListener(this),
				this);
		getServer().getPluginManager().registerEvents(new ItemListener(this), this);
		if (getPluginSettings().isAutomaticUpdate()) {
			new Updater(this, "mythic", this.getFile(),
					Updater.UpdateType.DEFAULT, false);
		}
		startStatistics();
	}

	private void startStatistics() {
		try {
			Metrics metrics = new Metrics(this);
			Metrics.Graph graph = metrics.createGraph("Tiers Used");
			graph.addPlotter(new Metrics.Plotter() {
				@Override
				public int getValue() {
					return getTierManager().getTiers().size();
				}
			});
			Metrics.Graph graph2 = metrics.createGraph("Custom Items Created");
			graph2.addPlotter(new Metrics.Plotter() {
				@Override
				public int getValue() {
					return getDropManager().getCustomItems().size();
				}
			});
			metrics.start();
		} catch (IOException e) {
			getLogger().info("Could not begin Metrics.");
		}
	}

}
