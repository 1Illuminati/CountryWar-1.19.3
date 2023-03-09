package org.countrywar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.countrywar.country.CountryManager;
import org.countrywar.country.CountryMoveInventory;
import org.redkiller.command.AbstractPlayerCommand;
import org.redkiller.entity.livingentity.player.NewPlayer;

import java.util.ArrayList;
import java.util.List;

public class CountryMoveCommand extends AbstractPlayerCommand {

    @Override
    public String getName() {
        return "countryMove";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String s, String[] strings) {
        NewPlayer player = NewPlayer.getNewPlayer((Player) commandSender);
        String team = CountryManager.getPlayerTeam(player);
        player.openInventory(new CountryMoveInventory(team, player.getWorld()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String s, String[] strings) {
        return new ArrayList<>();
    }
}
