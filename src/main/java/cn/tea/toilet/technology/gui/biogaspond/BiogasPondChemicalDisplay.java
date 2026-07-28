package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.gui.TankMenuData;

final class BiogasPondChemicalDisplay {
    private BiogasPondChemicalDisplay() {
    }

    static int encodeRegistryId(int registryId) {
        return TankMenuData.encodeRegistryId(registryId);
    }

    static int decodeRegistryId(int wireValue) {
        return TankMenuData.decodeRegistryId(wireValue);
    }

    static int fillHeight(long amount, long capacity, int height) {
        return TankMenuData.fillHeight(amount, capacity, height);
    }
}
