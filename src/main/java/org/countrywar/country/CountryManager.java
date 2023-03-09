package org.countrywar.country;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.countrywar.CountryWar;
import org.redkiller.entity.livingentity.player.NewPlayer;
import org.redkiller.system.world.InterfaceArea;
import org.redkiller.system.world.WorldDataMap;
import org.redkiller.util.map.dataMap.DataMap;

public class CountryManager {
    private CountryManager() {
        throw new IllegalArgumentException("Utility class");
    }

    public static String getPlayerTeam(NewPlayer player) {
        return player.getDataMap().getString("team");
    }

    public static void setPlayerTeam(NewPlayer player, String team) {
        player.getDataMap().set("team", team);
    }

    public static boolean checkCountryPlayerSameTeam(Country country, NewPlayer player) {
        return country.getTeam().equals(getPlayerTeam(player));
    }

    public static boolean isReader(NewPlayer player) {
        return player.getDataMap().getBoolean("reader");
    }

    public static void sendMessageTeam(String team, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            NewPlayer newPlayer = NewPlayer.getNewPlayer(player);

            String playerTeam = CountryManager.getPlayerTeam(newPlayer);

            if (playerTeam.equals(team))
                newPlayer.sendMessage(message);
        }
    }

    public static Country getCountry(String name, World world) {
        WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);
        for (InterfaceArea interfaceArea : worldDataMap.getAllAreas()) {
            if (!(interfaceArea instanceof Country country))
                continue;

            if (country.getName().equals(name))
                return country;
        }

        return null;
    }

    public static void sendActionBarTeam(String team, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            NewPlayer newPlayer = NewPlayer.getNewPlayer(player);

            String playerTeam = CountryManager.getPlayerTeam(newPlayer);

            if (playerTeam.equals(team))
                newPlayer.sendActionBar(message);
        }
    }

    public static void sendTitleTeam(String team, String title, String subtitle) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            NewPlayer newPlayer = NewPlayer.getNewPlayer(player);

            String playerTeam = CountryManager.getPlayerTeam(newPlayer);
            if (playerTeam.equals(team))
                newPlayer.sendTitle(title, subtitle);
        }
    }

    public static void saveCountry() {
        for (World world : Bukkit.getWorlds()) {
            WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);
            DataMap dataMap = new DataMap();

            for (InterfaceArea interfaceArea : worldDataMap.getAllAreas()) {
                if (!(interfaceArea instanceof Country country))
                    continue;

                CountryWar.sendLog("Save Country: " + country.getName());
                DataMap countryDataMap = country.toDataMap();
                dataMap.set(country.getName(), countryDataMap);
            }

            worldDataMap.put("countrys", dataMap);
        }
    }

    public static void loadCountry() {
        for (World world : Bukkit.getWorlds()) {
            WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);
            DataMap dataMap = worldDataMap.getDataMap("countrys");

            for (String countryName : dataMap.keySet()) {
                DataMap countryDataMap = dataMap.getDataMap(countryName);
                Country country = Country.formDataMap(countryDataMap);
                CountryWar.sendLog("load Country: " + country.getName());
                worldDataMap.registerArea(country);
            }
        }
    }
}
