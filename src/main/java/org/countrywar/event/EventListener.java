package org.countrywar.event;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.countrywar.CountryWar;
import org.countrywar.country.Country;
import org.countrywar.country.CountryManager;
import org.countrywar.country.CountrySpawnInventory;
import org.redkiller.entity.livingentity.player.NewPlayer;
import org.redkiller.event.AreaEvent;
import org.redkiller.system.world.AreaAct;
import org.redkiller.system.world.InterfaceArea;
import org.redkiller.util.Scheduler;

import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        switch (block.getType()) {
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE, EMERALD_ORE, DEEPSLATE_EMERALD_ORE, GOLD_ORE, DEEPSLATE_GOLD_ORE ->
                    CountryWar.sendLog(player.getName() + " break ore" + block.getType().name());
        }
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity atkEntity = event.getDamager();
        Entity defEntity = event.getEntity();

        if (atkEntity instanceof Player atkPlayer && defEntity instanceof Player defPlayer) {
            NewPlayer atkNewPlayer = NewPlayer.getNewPlayer(atkPlayer);
            NewPlayer defNewPlayer = NewPlayer.getNewPlayer(defPlayer);

            if (CountryManager.getPlayerTeam(atkNewPlayer).equals(CountryManager.getPlayerTeam(defNewPlayer))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        int percent = new Random().nextInt(100);
        List<ItemStack> drop = event.getDrops();
        EntityCategory entityCategory = livingEntity.getCategory();

        if (livingEntity instanceof Player player) {
            Location location = player.getLocation();
            Block block = location.getBlock();
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getInventory();
            inventory.addItem(drop.toArray(new ItemStack[0]));
            drop.clear();
        } else if (entityCategory == EntityCategory.UNDEAD && percent < 5) {
            drop.add(new ItemStack(Material.GHAST_TEAR));
        }
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.BEACON)
            event.setCancelled(true);
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        NewPlayer player = NewPlayer.getNewPlayer(event.getEntity());
        player.setGameMode(GameMode.SPECTATOR);
        Location loc = player.getDataMap().getLocation("spawn", player.getWorld().getSpawnLocation());


        for (int i = 0; i < 5; i++) {
            final int j = i;
            Scheduler.delayScheduler(new Scheduler.RunnableEx() {
                @Override
                public void function() {
                    player.sendTitle("§fYou are §4Dead", "§f부활까지 : " + (5 - j));
                }
            }, i * 20);
        }

        Scheduler.delayScheduler(new Scheduler.RunnableEx() {
            @Override
            public void function() {
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(loc);
                player.sendTitle("§e§l[ §e부활! §e§l]", "§f부활하셨습니다!");
            }
        }, 100);
    }

    @EventHandler
    public void entitySpawnEvent(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        int percent = new Random().nextInt(100);

        if (entity.getType() == EntityType.SKELETON && percent < 10) {
            event.setCancelled(true);
            entity.getWorld().spawnEntity(entity.getLocation(), EntityType.WITHER_SKELETON);
        }
    }

    @EventHandler
    public void areaEvent(AreaEvent event) {
        InterfaceArea interfaceArea = event.getInterfaceArea();
        if (!(interfaceArea instanceof Country country))
            return;

        AreaAct areaAct = event.getAreaAct();
        Block block = event.getBlock();
        NewPlayer player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        String playerTeam = CountryManager.getPlayerTeam(player);
        World world = player.getWorld();
        Material material = block == null ? null : block.getType();
        boolean sameCountryTeam = CountryManager.checkCountryPlayerSameTeam(country, player);

        if (areaAct == AreaAct.MOVE) {
            if (sameCountryTeam)
                return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
            //CountryManager.sendActionBarTeam(country.getTeam(), "§f누군가  " + country.getName() + "성에서 움직이고 있습니다");
        } else if (areaAct == AreaAct.BREAK) {
            if (block == null)
                return;

            if (country.getBreakBoundingBox().contains(block.getLocation().toVector())) {
                event.setCancelled(true);
            }

            if (material != Material.BEACON)
                return;

            if (sameCountryTeam) {
                player.sendMessage("§f니 집은 못부숩니다");
                return;
            }

            if (itemStack.getType() != Material.AIR) {
                player.sendMessage("§f손에 아이템을 들고 있으면 성을 부술 수 없습니다");
                return;
            }

            country.addCoreHealth(-1);
            player.sendTitle("§a§l[ §a공격 성공! §a§l]", "§f남은 체력 §7: §4" + country.getCoreHealth());

            if (country.getCoreHealth() <= 0) {
                CountryManager.sendTitleTeam(country.getTeam(), "§4§l[ §4방어 실패! §4§l]", "§f" + playerTeam + "팀이 " + country.getName() + "성을 점령하였습니다!");
                country.setTeam(playerTeam);
                country.setCoreHealth(10);
                CountryManager.sendTitleTeam(playerTeam, "§2§l[ §2점령 성공! §2§l]", "§f" + player.getName() + "님이 " + country.getName() + "성을 점령하였습니다!");
            } else {
                CountryManager.sendTitleTeam(country.getTeam(), "§c§l[ §c경고! §c§l]", "§f" + country.getName() + "성이 공격 받고 있습니다! 남은 체력 §7: §4" + country.getCoreHealth());
            }
        } else if (areaAct == AreaAct.INTERACT_BLOCK) {
            if (block == null)
                return;

            if (material != Material.BEACON)
                return;

            if (!sameCountryTeam)
                return;

            if (itemStack.getType() == Material.NETHER_STAR) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                country.setCoreHealth(10);
                player.sendMessage("§f" + country.getName() + "성의 체력을 §4" + country.getCoreHealth() + "§f로 회복 하였습니다!");
            }

            if (!CountryManager.isReader(player))
                return;

            player.openInventory(new CountrySpawnInventory(playerTeam, world));
        } else if (areaAct == AreaAct.PLACE) {
            if (block == null)
                return;

            if (country.getBreakBoundingBox().contains(block.getLocation().toVector())) {
                event.setCancelled(true);
                return;
            }

            if (country.getPlaceBoundingBox().contains(block.getLocation().toVector()) && !material.name().contains("GLASS")) {
                player.sendMessage("§c이곳에는 유리블럭만 설치가 가능합니다");
                event.setCancelled(true);
                return;
            }

            switch (material) {
                case BEACON, OBSIDIAN, COBWEB, CRYING_OBSIDIAN -> event.setCancelled(true);
            }
        }
    }
}
