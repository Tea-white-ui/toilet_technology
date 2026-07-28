# Toilet Technology 附加模组 API

## 兼容性

受支持的 API 范围为 `cn.tea.toilet.technology.api.**`。该包以外的类，包括机器方块实体、`gas.GasRegistry`、管道调度以及所有 `compat` 包，均属于内部实现细节，可能在未通知的情况下变更。

API 1.0 随 `cn.tea.toilet.technology:toilet_technology:<mod version>` 构件一同发布。附加模组必须使用匹配的 Minecraft 1.21.1 与 NeoForge 版本。在同一 API 主版本内，公共类型与现有方法行为不会被移除或作不兼容变更；在可行时，新接口行为会通过默认方法添加。

```groovy
repositories {
    maven { url = uri("../toilet_technology/repo") }
}

dependencies {
    compileOnly fg.deobf("cn.tea.toilet.technology:toilet_technology:0.5.2-NeoForge")
    runtimeOnly fg.deobf("cn.tea.toilet.technology:toilet_technology:0.5.2-NeoForge")
}
```

在使用本地 `repo/` 发布物前，请先在 Toilet Technology 中运行 `./gradlew publish`。如有可用的已发布 Maven 坐标，请改用该坐标。

## 气体契约

- `Gas.id()` 是稳定标识。不得使用引用相等性或 GUI 同步索引。
- `GasStack.EMPTY` 是唯一的空表示。API 对空气体栈绝不会返回 `null`。
- `GasAction.SIMULATE` 不得修改存储、物品 NBT、方块实体、世界或缓存。`GasAction.EXECUTE` 才会提交操作。
- 无效储罐索引应返回空气体栈、`0` 或 `false`；处理器不得为此抛出异常。
- `BasicGasTank` 是可复用的单储罐实现，持久化格式为 `{Gas: "namespace:id", Amount: long}`，并接受 Mekanism 10.7 的迁移格式。
- `GasTransfer.transfer` 会在执行任一端前模拟两个端点。在这些阶段之间发生变更的处理器会导致传输失败，而不会悄然丢失气体。

## 内置气体

在 API 1.0 中，`ToiletGasRegistry` 为只读：

```java
Optional<Gas> biogas = ToiletGasRegistry.get(
        ResourceLocation.fromNamespaceAndPath("toilet_technology", "biogas"));
GasStack stack = biogas.map(gas -> new GasStack(gas, 1_000)).orElse(GasStack.EMPTY);
```

内置气体为 `ToiletGasRegistry.BIOGAS` 和 `ToiletGasRegistry.METHANE`。目前刻意不提供第三方气体注册，因为菜单网络 ID、客户端同步、纹理加载、存档迁移以及可选 Mekanism 映射都需要专门的注册表设计。

## 能力

使用 `GasCapabilities.BLOCK` 和 `GasCapabilities.ITEM` 与气体存储交互。每次操作前都要立即查询方块能力；使用方不得缓存处理器。当结构失效或暴露面发生变化时，提供方必须使 NeoForge 能力缓存失效。

```java
IGasHandler handler = level.getCapability(GasCapabilities.BLOCK, targetPos, direction);
if (handler != null) {
    GasStack remainder = handler.insertGas(0, offered, GasAction.SIMULATE);
    long accepted = offered.amount() - remainder.amount();
    if (accepted > 0) {
        handler.insertGas(0, offered.copyWithAmount(accepted), GasAction.EXECUTE);
    }
}
```

暴露气体处理器的附加模组应通过 NeoForge 的 `RegisterCapabilitiesEvent` 注册 `GasCapabilities.BLOCK`。请遵守请求的方向，并在该面不被允许时返回 `null`。不要调用 `ExternalGasHandlers.register`；它是内部的可选兼容桥接。

## 可选集成

公共 API 不依赖 Mekanism。附加模组不得通过本 API 导入 `mekanism.api.*`，也不得依赖 `cn.tea.toilet.technology.compat.mekanism`。Mekanism 支持仍是由 Toilet Technology 维护的可选桥接。
