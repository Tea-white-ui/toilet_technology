# 气体能力集成

使用稳定能力注册由附加模组拥有的方块实体：

```java
private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.ADDON_MACHINE.get(),
            (blockEntity, side) -> blockEntity.getGasHandler(side));
}
```

对于无效储罐索引，处理器必须返回 `GasStack.EMPTY`、`0` 或 `false`，且对于空结果绝不能返回 `null`。每次传输都应先模拟，并且只执行目标端已接受的数量：

```java
GasStack offered = source.extractGas(sourceTank, limit, GasAction.SIMULATE);
GasStack remainder = destination.insertGas(destinationTank, offered, GasAction.SIMULATE);
long accepted = offered.amount() - remainder.amount();
if (accepted > 0) {
    GasStack extracted = source.extractGas(sourceTank, accepted, GasAction.EXECUTE);
    destination.insertGas(destinationTank, extracted, GasAction.EXECUTE);
}
```

提供方负责只暴露允许的面，并在多方块结构、端口绑定或面策略发生变化时使 NeoForge 能力缓存失效。使用方必须在操作前重新查询 `GasCapabilities.BLOCK`，并且不得在此类变化后继续保留处理器。

`ExternalGasHandlers` 不是面向附加模组的公共 API。它仅用于 Toilet Technology 的可选 Mekanism 兼容适配器。
