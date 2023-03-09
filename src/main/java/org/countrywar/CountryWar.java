package org.countrywar;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.countrywar.command.CountryCommand;
import org.countrywar.command.CountryMoveCommand;
import org.countrywar.command.SetCountryTeamCommand;
import org.countrywar.country.CountryManager;
import org.countrywar.event.EventListener;
import org.countrywar.item.Recipe;
import org.redkiller.command.AbstractCommand;

public final class CountryWar extends JavaPlugin {
    public static final String PLUGIN_NAME = "CountryWar";
    private static CountryWar plugin;
    private static boolean debug = false;

    public static void sendLog(Object message) {
        Bukkit.getConsoleSender().sendMessage("§6§l[ §6" + PLUGIN_NAME + "§6§l ]: §f" + message);
    }

    public static void sendDebugLog(Object message) {
        if (debug)
            Bukkit.getConsoleSender().sendMessage("§6§l[ §6" + PLUGIN_NAME + " Debug" + "§6§l ]: §f" + message);
    }

    public static void setDebug(boolean debug) {
        CountryWar.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static CountryWar getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        CountryWar.plugin = this;
        this.setCommand();
        this.setEvent();
        Recipe.register();
        CountryManager.loadCountry();
    }

    @Override
    public void onDisable() {
        CountryManager.saveCountry();
    }

    private void registerEvent(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void setEvent() {
        this.registerEvent(new EventListener());
    }

    private void registerCommand(AbstractCommand command) {
        PluginCommand co = this.getCommand(command.getName());

        if (co == null) {
            throw new NullPointerException("Command " + command.getName() + " not found");
        }

        co.setExecutor(command);
        co.setTabCompleter(command);
    }

    private void setCommand() {
        this.registerCommand(new CountryCommand());
        this.registerCommand(new SetCountryTeamCommand());
        this.registerCommand(new CountryMoveCommand());
    }
}
