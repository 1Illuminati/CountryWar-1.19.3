package org.countrywar.country;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redkiller.entity.livingentity.player.NewPlayer;
import org.redkiller.inventory.CustomInventory;
import org.redkiller.item.ItemBuilder;
import org.redkiller.system.world.InterfaceArea;
import org.redkiller.system.world.WorldDataMap;

import java.util.Arrays;

public class CountrySpawnInventory extends CustomInventory {
    public CountrySpawnInventory(String team, World world) {
        super("§f리스폰 설정", 27);
        WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);
        int temp = 0;

        for (InterfaceArea interfaceArea : worldDataMap.getAllAreas()) {
            if (!(interfaceArea instanceof Country country))
                continue;

            Location center = country.getCenter();

            if (team.equals(country.getTeam())) {
                ItemStack itemStack = new ItemBuilder(Material.BEACON).setDisplayName("§f" + country.getName()).setLore(Arrays.asList(
                        "§f성 위치 : " + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ(),
                        "§f클릭시 해당위치를 죽을시 부활하는 리스폰 장소로 설정합니다."
                )).setCustomModelData(5882).build();
                super.setItem(temp, itemStack);

                this.putButton(temp++, inventoryClickEvent -> {
                    inventoryClickEvent.setCancelled(true);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        NewPlayer newPlayer = NewPlayer.getNewPlayer(player);

                        if (!team.equals(CountryManager.getPlayerTeam(newPlayer)))
                            continue;

                        newPlayer.getDataMap().put("spawn", center.clone().add(0, 1, 0));
                        inventoryClickEvent.getInventory().close();
                        player.sendMessage("§f" + country.getName() + "성의 위치로 리스폰 장소가 설정되었습니다.");
                    }
                });
            }
        }
    }
}
