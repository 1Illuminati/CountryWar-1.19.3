package org.countrywar.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.countrywar.country.Country;
import org.countrywar.country.CountryManager;
import org.redkiller.command.AbstractPlayerCommand;
import org.redkiller.entity.livingentity.player.NewPlayer;
import org.redkiller.system.world.WorldDataMap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CountryCommand extends AbstractPlayerCommand {

    @Override
    public String getName() {
        return "country";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String s, String[] strings) {
        if (strings.length < 2) {
            commandSender.sendMessage("§c/성 [명령어] [성 이름]");
            return false;
        }
        NewPlayer player = NewPlayer.getNewPlayer((Player) commandSender);
        String name = strings[1];
        World world = player.getWorld();
        WorldDataMap worldDataMap = WorldDataMap.getWorldDataMap(world);

        switch (strings[0]) {
            case "생성" -> {
                Location center = player.getLocation();
                Country country = Country.newCountry(center.getBlock().getLocation());

                worldDataMap.registerArea(country);
                center.getBlock().setType(Material.BEACON);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++){
                        Location loc = center.clone().add(1D - i, -1, 1D - j);
                        loc.getBlock().setType(Material.EMERALD_BLOCK);
                    }
                }

                country.setName(name);

                return true;
            }
            case "팀" -> {
                Country country = CountryManager.getCountry(name, world);

                if (country == null) {
                    commandSender.sendMessage("§c해당 성이 존재하지 않습니다.");
                    return false;
                }

                if (strings.length < 3) {
                    commandSender.sendMessage("§c/성 팀 [성 이름] [팀 이름]");
                    return false;
                }

                String teamName = strings[2];
                country.setTeam(teamName);
                commandSender.sendMessage("§a성의 팀을 " + teamName + "으로 설정하였습니다.");
            }
            case "이동" -> {
                Country country = CountryManager.getCountry(name, world);

                if (country == null) {
                    commandSender.sendMessage("§c해당 성이 존재하지 않습니다.");
                    return false;
                }

                player.teleport(country.getCenter());
            }
            case "삭제" -> {
                Country country = CountryManager.getCountry(name, world);

                if (country == null) {
                    commandSender.sendMessage("§c해당 성이 존재하지 않습니다.");
                    return false;
                }

                worldDataMap.removeArea(country);
                commandSender.sendMessage("§a성을 삭제하였습니다.");
            }
            case "목록" -> {
                StringBuilder builder = new StringBuilder("성 목록 : ");

                worldDataMap.getAllAreas().forEach(area -> {
                    if (area instanceof Country country) {
                        builder.append(country.getName()).append(", ");
                    }
                });

                player.sendMessage(builder.toString());
            }
            case "갯수" -> {
                AtomicInteger amount = new AtomicInteger();
                worldDataMap.getAllAreas().forEach(area -> {
                    if (area instanceof Country country && country.getTeam().equals(name)) {
                        amount.getAndIncrement();
                    }
                });

                player.sendMessage(name + "팀의 현재 성 갯수 : " + amount.get());
            }
            default -> {commandSender.sendMessage("§c/성 생성/팀/삭제/이동/목록/갯수 [성 이름]");return false;}
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String s, String[] strings) {
        return strings.length == 1 ? Arrays.asList("생성", "팀", "이동", "삭제", "목록", "갯수") : List.of("[성 이름]");
    }
}
