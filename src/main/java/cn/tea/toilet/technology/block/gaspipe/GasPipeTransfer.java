package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.GasTransfer;
import cn.tea.toilet.technology.api.gas.IGasHandler;

final class GasPipeTransfer {
    private GasPipeTransfer() {
    }

    static GasStack pull(IGasHandler source, IGasHandler pipe, long limit) {
        return GasTransfer.transfer(source, 0, pipe, 0, limit);
    }

    static GasStack push(IGasHandler pipe, IGasHandler destination, long limit) {
        return GasTransfer.transfer(pipe, 0, destination, 0, limit);
    }
}
