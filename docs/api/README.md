# Toilet Technology Addon API

## Compatibility

The supported API surface is `cn.tea.toilet.technology.api.**`. Classes outside that package,
including machine block entities, `gas.GasRegistry`, pipeline scheduling, and all `compat` packages,
are internal implementation details and may change without notice.

API 1.0 is shipped in the `cn.tea.toilet.technology:toilet_technology:<mod version>` artifact.
Addons must use a matching Minecraft 1.21.1 and NeoForge version. Within one API major version,
public types and existing method behavior are not removed or changed incompatibly; new interface
behavior is added through default methods where possible.

```groovy
repositories {
    maven { url = uri("../toilet_technology/repo") }
}

dependencies {
    compileOnly fg.deobf("cn.tea.toilet.technology:toilet_technology:0.5.2-NeoForge")
    runtimeOnly fg.deobf("cn.tea.toilet.technology:toilet_technology:0.5.2-NeoForge")
}
```

Run `./gradlew publish` in Toilet Technology before consuming the local `repo/` publication.
Use the released Maven coordinate instead when one is available.

## Gas Contracts

- `Gas.id()` is the stable identity. Never use reference equality or GUI synchronization indexes.
- `GasStack.EMPTY` is the only empty representation. APIs never return `null` for an empty stack.
- `GasAction.SIMULATE` must not mutate storage, item NBT, block entities, worlds, or caches.
  `GasAction.EXECUTE` commits the operation.
- Invalid tank indexes return an empty stack, `0`, or `false`; handlers must not throw for them.
- `BasicGasTank` is a reusable single-tank implementation that persists as
  `{Gas: "namespace:id", Amount: long}` and accepts the Mekanism 10.7 migration format.
- `GasTransfer.transfer` simulates both endpoints before it executes either endpoint. Handlers
  that mutate between these phases cause it to fail rather than silently lose gas.

## Built-In Gases

`ToiletGasRegistry` is read-only in API 1.0:

```java
Optional<Gas> biogas = ToiletGasRegistry.get(
        ResourceLocation.fromNamespaceAndPath("toilet_technology", "biogas"));
GasStack stack = biogas.map(gas -> new GasStack(gas, 1_000)).orElse(GasStack.EMPTY);
```

The built-in gases are `ToiletGasRegistry.BIOGAS` and `ToiletGasRegistry.METHANE`. Third-party
gas registration is intentionally not available yet because menu wire IDs, client synchronization,
texture loading, save migration, and optional Mekanism mappings need a dedicated registry design.

## Capabilities

Use `GasCapabilities.BLOCK` and `GasCapabilities.ITEM` to interact with gas storage. Query a block
capability immediately before each operation; consumers must not cache handlers. Providers must
invalidate NeoForge capability caches whenever a structure becomes invalid or an exposed side changes.

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

An addon that exposes a gas handler registers `GasCapabilities.BLOCK` through NeoForge's
`RegisterCapabilitiesEvent`. Respect the requested side and return `null` when the side is not
allowed. Do not call `ExternalGasHandlers.register`; it is an internal optional-compatibility bridge.

## Optional Integrations

The public API has no Mekanism dependency. Addons must not import `mekanism.api.*` through this API
or depend on `cn.tea.toilet.technology.compat.mekanism`. Mekanism support remains an optional bridge
owned by Toilet Technology.
