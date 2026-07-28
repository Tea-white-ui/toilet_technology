package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.gui.TankMenuData;

final class BiogasPondFluidDisplay {
    private BiogasPondFluidDisplay() {
    }

    static int encodeRegistryId(int registryId) {
        return TankMenuData.encodeRegistryId(registryId);
    }

    static int decodeRegistryId(int wireValue) {
        return TankMenuData.decodeRegistryId(wireValue);
    }

    static int fillHeight(int amount, int capacity, int height) {
        return TankMenuData.fillHeight(amount, capacity, height);
    }
}
