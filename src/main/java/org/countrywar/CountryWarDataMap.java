package org.countrywar;

import org.redkiller.util.file.nbt.NBTHelper;
import org.redkiller.util.map.dataMap.DataMap;

public class CountryWarDataMap extends DataMap {
    private static CountryWarDataMap instance;
    public static CountryWarDataMap getInstance() {
        if (instance == null)
            instance = new CountryWarDataMap();

        return instance;
    }

    private CountryWarDataMap() {
        this.load();
    }

    @Override
    public void load() {
        this.copy(NBTHelper.loadDataMap("plugins/redKillerLibrary/nbt/countryWar/countryWar.dat"));
    }

    @Override
    public void save() {
        NBTHelper.saveDataMap("plugins/redKillerLibrary/nbt/countryWar/countryWar.dat", this);
    }
}
