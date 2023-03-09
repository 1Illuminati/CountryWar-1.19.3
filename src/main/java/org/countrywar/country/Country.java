package org.countrywar.country;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.redkiller.entity.livingentity.player.NewPlayer;
import org.redkiller.system.world.AreaAct;
import org.redkiller.system.world.InterfaceArea;
import org.redkiller.util.key.RedKillerKey;
import org.redkiller.util.map.dataMap.DataMap;
import org.redkiller.util.map.dataMap.ToDataMap;

import java.util.UUID;

public class Country implements InterfaceArea, ToDataMap {

    public static Country newCountry(Location center) {
        return new Country(center);
    }

    public static Country formDataMap(DataMap dataMap) {
        Country country = new Country(dataMap.getLocation("center"));
        country.team = dataMap.getString("team");
        country.setCoreHealth(dataMap.getInt("coreHealth"));
        country.setName(dataMap.getString("countryName"));
        country.key = dataMap.getRedKillerKey("key");

        return country;
    }
    private final BoundingBox boundingBox;
    private final BoundingBox breakBoundingBox;
    private final BoundingBox placeBoundingBox;
    private final Location center;
    private String team = "EXIST";
    private int coreHealth = 10;
    private String countryName = UUID.randomUUID().toString();

    private RedKillerKey key = new RedKillerKey("CountryWar", UUID.randomUUID());

    private Country(Location center) {
        this.center = center;
        double x = center.getBlockX();
        double y = center.getBlockY();
        double z = center.getBlockZ();
        this.boundingBox = BoundingBox.of(new Vector(x - 63, -64, z - 63), new Vector(x + 63, 320, z + 63));
        this.placeBoundingBox = BoundingBox.of(new Vector(x - 2.8, y - 1.5, z - 2.8), new Vector(x + 2.8, 320, z + 2.8));
        this.breakBoundingBox = BoundingBox.of(new Vector(x - 2.8, y - 1.5, z - 2.8), new Vector(x + 2.8, y + 2.5, z + 2.8));
    }

    public BoundingBox getBreakBoundingBox() {
        return breakBoundingBox;
    }

    public BoundingBox getPlaceBoundingBox() {
        return placeBoundingBox;
    }

    public String getName() {
        return this.countryName;
    }

    public void setName(String name) {
        this.countryName = name;
    }

    public Location getCenter() {
        return this.center;
    }
    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public boolean contain(Vector vector) {
        return boundingBox.contains(vector);
    }

    @Override
    public boolean contain(BoundingBox boundingBox) {
        return boundingBox.contains(boundingBox);
    }

    @Override
    public boolean contain(InterfaceArea interfaceArea) {
        return this.contain(interfaceArea.getBoundingBox());
    }

    @Override
    public boolean overlap(InterfaceArea interfaceArea) {
        return this.overlap(interfaceArea.getBoundingBox());
    }

    @Override
    public boolean overlap(BoundingBox boundingBox) {
        return boundingBox.overlaps(boundingBox);
    }

    @Override
    public boolean playerAct(NewPlayer newPlayer, AreaAct areaAct) {
        return false;
    }

    @Override
    public DataMap toDataMap() {
        DataMap dataMap = new DataMap();
        dataMap.set("team", team);
        dataMap.set("center", this.center);
        dataMap.set("coreHealth", coreHealth);
        dataMap.set("countryName", countryName);
        dataMap.set("key", key);
        return dataMap;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return this.team;
    }

    public void setCoreHealth(int coreHealth) {
        this.coreHealth = coreHealth;
    }

    public int getCoreHealth() {
        return this.coreHealth;
    }

    public void addCoreHealth(int coreHealth) {
        this.coreHealth += coreHealth;
    }

    @Override
    public RedKillerKey getKey() {
        return key;
    }
}
