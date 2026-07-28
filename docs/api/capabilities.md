# Gas Capability Integration

Register an addon-owned block entity with the stable capability:

```java
private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.ADDON_MACHINE.get(),
            (blockEntity, side) -> blockEntity.getGasHandler(side));
}
```

The handler must return `GasStack.EMPTY`, `0`, or `false` for invalid tank indexes and must never
return `null` for an empty result. For every transfer, simulate first and execute only the amount
the destination accepted:

```java
GasStack offered = source.extractGas(sourceTank, limit, GasAction.SIMULATE);
GasStack remainder = destination.insertGas(destinationTank, offered, GasAction.SIMULATE);
long accepted = offered.amount() - remainder.amount();
if (accepted > 0) {
    GasStack extracted = source.extractGas(sourceTank, accepted, GasAction.EXECUTE);
    destination.insertGas(destinationTank, extracted, GasAction.EXECUTE);
}
```

The provider is responsible for exposing only permitted sides and invalidating NeoForge capability
caches when a multiblock structure, port binding, or side policy changes. Consumers must re-query
`GasCapabilities.BLOCK` before an operation and must not retain a handler after such a change.

`ExternalGasHandlers` is not public addon API. It exists solely for Toilet Technology's optional
Mekanism compatibility adapter.
