package org.countrywar.country;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redkiller.inventory.CustomInventory;
import org.redkiller.item.ItemBuilder;
import org.redkiller.system.world.InterfaceArea;
import org.redkiller.system.world.WorldDataMap;
import org.redkiller.util.Scheduler;

import java.util.Arrays;

public class CountryMoveInventory extends CustomInventory {
    public CountryMoveInventory(String team, World world) {
        super("§f국가 이동", 27);
        WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);
        int temp = 0;

        for (InterfaceArea interfaceArea : worldDataMap.getAllAreas()) {
            if (!(interfaceArea instanceof Country country))
                continue;

            Location center = country.getCenter();

            if (team.equals(country.getTeam())) {
                ItemStack itemStack = new ItemBuilder(Material.BEACON).setDisplayName("§f" + country.getName()).setLore(Arrays.asList(
                        "§f성 위치 : " + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ(),
                        "§f클릭시 해당위치의 성으로 이동합니다"
                )).setCustomModelData(6974).build();
                super.setItem(temp, itemStack);

                this.putButton(temp++, inventoryClickEvent -> {
                    inventoryClickEvent.setCancelled(true);
                    Player player = (Player) inventoryClickEvent.getWhoClicked();
                    player.closeInventory();

                    for (int i = 0; i < 5; i++) {
                        final int j = i;
                        Scheduler.delayScheduler(new Scheduler.RunnableEx() {
                            @Override
                            public void function() {
                                player.sendTitle("§f국가 이동중...", "§f이동까지 : " + (5 - j));
                            }
                        }, i * 20);
                    }

                    Scheduler.delayScheduler(new Scheduler.RunnableEx() {
                        @Override
                        public void function() {
                            player.teleport(center.clone().add(0, 1, 0));
                            player.sendTitle("§b§l[ §b이동 완료! §b§l]", "§f" + country.getName() + "성으로 이동했습니다.");
                        }
                    }, 100);
                });
            }
        }
    }
}
