package org.countrywar.command;

import org.bukkit.command.CommandSender;
import org.countrywar.country.CountryManager;
import org.redkiller.command.AbstractCommand;
import org.redkiller.entity.livingentity.player.NewPlayer;

import java.util.Collections;
import java.util.List;

public class SetCountryTeamCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "setCountryTeam";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String s, String[] strings) {

        if (strings.length < 3) {
            commandSender.sendMessage("§c/국가팀설정 [플레이어 이름] [팀 이름] [리더 여부]");
            return false;
        }

        NewPlayer player = NewPlayer.getNewPlayer(strings[0]);

        if (player == null) {
            commandSender.sendMessage("§c존재하지 않는 플레이어");
            return false;
        }

        String reader = strings[2];
        player.getDataMap().set("reader", reader.equals("true"));

        CountryManager.setPlayerTeam(player, strings[1]);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String s, String[] strings) {
        return strings.length == 1 ? null : strings.length == 2 ? Collections.singletonList("[팀 이름]") : Collections.singletonList("[리더 여부]");
    }
}
