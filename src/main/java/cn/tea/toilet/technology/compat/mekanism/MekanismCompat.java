package cn.tea.toilet.technology.compat.mekanism;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlockEntities;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import cn.tea.toilet.technology.item.GasTankItem;
import cn.tea.toilet.technology.item.ModItems;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

/** Optional Mekanism bridge. This class is loaded only when Mekanism is present. */
public final class MekanismCompat {
    private static final ResourceLocation CHEMICAL_HANDLER_ID =
            ResourceLocation.fromNamespaceAndPath("mekanism", "chemical_handler");
    private static final BlockCapability<IChemicalHandler, Direction> CHEMICAL_BLOCK =
            BlockCapability.createSided(CHEMICAL_HANDLER_ID, IChemicalHandler.class);
    private static final ItemCapability<IChemicalHandler, Void> CHEMICAL_ITEM =
            ItemCapability.createVoid(CHEMICAL_HANDLER_ID, IChemicalHandler.class);
    private static final DeferredRegister<Chemical> CHEMICALS =
            DeferredRegister.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ModConstants.MOD_ID);
    private static final DeferredHolder<Chemical, Chemical> BIOGAS = CHEMICALS.register(
            "biogas", () -> new Chemical(ChemicalBuilder.builder(GasRegistry.BIOGAS.texture())
                    .tint(GasRegistry.BIOGAS.tint())));
    private static final DeferredHolder<Chemical, Chemical> METHANE = CHEMICALS.register(
            "methane", () -> new Chemical(ChemicalBuilder.builder(GasRegistry.METHANE.texture())
                    .tint(GasRegistry.METHANE.tint())));

    private MekanismCompat() { }

    public static void register(IEventBus bus) {
        CHEMICALS.register(bus);
        bus.addListener(MekanismCompat::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(CHEMICAL_BLOCK, ModBlockEntities.SEPTIC_TANK_PORT.get(),
                (entity, side) -> adapt(entity.getGasHandler(side)));
        event.registerBlockEntity(CHEMICAL_BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),
                (entity, side) -> entity.getPortType() == cn.tea.toilet.technology.block.biogaspond.BiogasPondPortType.GAS_OUTPUT
                        ? adapt(entity.getGasHandler(side)) : null);
        event.registerBlockEntity(CHEMICAL_BLOCK, ModBlockEntities.ABSORPTION_TOWER_BOTTOM.get(),
                (entity, side) -> adapt(entity.getGasHandler(side)));
        event.registerBlockEntity(CHEMICAL_BLOCK, ModBlockEntities.ABSORPTION_TOWER_BODY.get(),
                (entity, side) -> adapt(entity.getGasHandler(side)));
        event.registerItem(CHEMICAL_ITEM,
                (stack, ignored) -> adapt(((GasTankItem) stack.getItem()).createGasHandler(stack)),
                ModItems.GAS_TANK.get());
    }

    private static IChemicalHandler adapt(IGasHandler handler) {
        return handler == null ? null : new ChemicalHandlerAdapter(handler);
    }

    private static final class ChemicalHandlerAdapter implements IChemicalHandler {
        private final IGasHandler delegate;

        private ChemicalHandlerAdapter(IGasHandler delegate) { this.delegate = delegate; }

        @Override public int getChemicalTanks() { return delegate.getGasTanks(); }

        @Override public ChemicalStack getChemicalInTank(int tank) {
            return chemicalStackFor(delegate.getGasInTank(tank));
        }

        @Override public void setChemicalInTank(int tank, ChemicalStack stack) {
            Gas gas = gasFor(stack);
            if (gas != null) {
                delegate.setGasInTank(tank, new GasStack(gas, stack.getAmount()));
            }
        }

        @Override public long getChemicalTankCapacity(int tank) { return delegate.getGasTankCapacity(tank); }

        @Override public boolean isValid(int tank, ChemicalStack stack) {
            Gas gas = gasFor(stack);
            return gas != null && delegate.isValid(tank, new GasStack(gas, 1));
        }

        @Override public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
            Gas gas = gasFor(stack);
            if (gas == null) return stack;
            GasStack remainder = delegate.insertGas(tank,
                    new GasStack(gas, stack.getAmount()),
                    action == Action.EXECUTE ? GasAction.EXECUTE : GasAction.SIMULATE);
            return remainder.isEmpty() ? ChemicalStack.EMPTY
                    : new ChemicalStack(stack.getChemicalHolder(), remainder.amount());
        }

        @Override public ChemicalStack extractChemical(int tank, long amount, Action action) {
            GasStack extracted = delegate.extractGas(tank, amount,
                    action == Action.EXECUTE ? GasAction.EXECUTE : GasAction.SIMULATE);
            return chemicalStackFor(extracted);
        }

        private static @Nullable Gas gasFor(ChemicalStack stack) {
            if (stack.is(BIOGAS.get())) return GasRegistry.BIOGAS;
            if (stack.is(METHANE.get())) return GasRegistry.METHANE;
            return null;
        }

        private static ChemicalStack chemicalStackFor(GasStack stack) {
            if (stack.isEmpty()) return ChemicalStack.EMPTY;
            if (GasRegistry.BIOGAS.equals(stack.gas())) return new ChemicalStack(BIOGAS, stack.amount());
            if (GasRegistry.METHANE.equals(stack.gas())) return new ChemicalStack(METHANE, stack.amount());
            return ChemicalStack.EMPTY;
        }
    }
}