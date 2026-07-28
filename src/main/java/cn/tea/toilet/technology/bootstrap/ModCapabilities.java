package cn.tea.toilet.technology.bootstrap;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.GasCapabilities;
import cn.tea.toilet.technology.item.GasTankItem;
import cn.tea.toilet.technology.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Registers the capabilities exposed by this mod's block entities and items.
 */
public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModCapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerToiletFluidHandlers(event);
        registerDryingHandlers(event);
        registerSepticTankHandlers(event);
        registerBiogasPondHandlers(event);
        registerAbsorptionTowerHandlers(event);
        registerMachineHandlers(event);
        registerItemHandlers(event);
    }

    private static void registerToiletFluidHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.PREMIUM_TOILET.get(),
                (blockEntity, side) -> blockEntity.fluidTank);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.NETHERITE_TOILET.get(),
                (blockEntity, side) -> blockEntity.fluidTank);
    }

    private static void registerDryingHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.DRYING_RACK.get(),
                (blockEntity, side) -> blockEntity.itemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.DRYING_BOX.get(),
                (blockEntity, side) -> blockEntity.getHopperHandler());
    }

    private static void registerSepticTankHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(),
                (blockEntity, side) -> blockEntity.isStructureValid() ? blockEntity.getAutomationItems() : null);
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.SEPTIC_TANK_PORT.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SEPTIC_TANK_PORT.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
    }

    private static void registerBiogasPondHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (blockEntity, side) -> blockEntity.getItemHandler(side));
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
    }

    private static void registerAbsorptionTowerHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.ABSORPTION_TOWER_BOTTOM.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.ABSORPTION_TOWER_BOTTOM.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.ABSORPTION_TOWER_BODY.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.ABSORPTION_TOWER_BODY.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
    }

    private static void registerMachineHandlers(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.BIOGAS_GENERATOR.get(),
                (blockEntity, side) -> blockEntity.energyStorage);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.GAS_MELTING_FURNACE.get(),
                (blockEntity, side) -> blockEntity.getItemHandler(side));
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.GAS_MELTING_FURNACE.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.GAS_PIPE.get(),
                (blockEntity, side) -> blockEntity.getGasHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SEWAGE_PURIFIER.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler(side));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.SEWAGE_PURIFIER.get(),
                (blockEntity, side) -> blockEntity.getEnergyStorage(side));
    }

    private static void registerItemHandlers(RegisterCapabilitiesEvent event) {
        event.registerItem(GasCapabilities.ITEM,
                (stack, ignored) -> ((GasTankItem) stack.getItem()).createGasHandler(stack),
                ModItems.GAS_TANK.get());
    }
}
