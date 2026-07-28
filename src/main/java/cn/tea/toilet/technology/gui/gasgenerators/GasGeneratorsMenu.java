package cn.tea.toilet.technology.gui.gasgenerators;

import cn.tea.toilet.technology.api.gas.Gas;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.gasgenerators.GasGeneratorOperation;
import cn.tea.toilet.technology.block.gasgenerators.GasGeneratorsBlockEntity;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class GasGeneratorsMenu extends AbstractContainerMenu {
    private static final int DATA_GAS_AMOUNT = 0;
    private static final int DATA_GAS_TYPE = 1;
    private static final int DATA_ENERGY = 2;
    private static final int DATA_GENERATING = 3;
    private static final int DATA_COUNT = 4;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public GasGeneratorsMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, null, ContainerLevelAccess.NULL);
    }

    public GasGeneratorsMenu(int containerId, Inventory inventory, GasGeneratorsBlockEntity blockEntity, ContainerLevelAccess access) {
        super(ModMenuTypes.GAS_GENERATORS_MENU.get(), containerId);
        this.access = access;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
        data = blockEntity == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override
            public int get(int index) {
                GasStack stack = blockEntity.getGasStack();
                return switch (index) {
                    case DATA_GAS_AMOUNT -> (int) stack.getAmount();
                    case DATA_GAS_TYPE -> stack.isEmpty() ? 0 : GasRegistry.id(stack.getGas()) + 1;
                    case DATA_ENERGY -> blockEntity.getEnergyStored();
                    case DATA_GENERATING -> blockEntity.isGenerating() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override public void set(int index, int value) { }
            @Override public int getCount() { return DATA_COUNT; }
        };
        addDataSlots(data);
    }

    public GasStack gasStack() {
        Gas gas = GasRegistry.byId(data.get(DATA_GAS_TYPE) - 1);
        int amount = data.get(DATA_GAS_AMOUNT);
        return gas == null || amount <= 0 ? GasStack.EMPTY : new GasStack(gas, amount);
    }

    public int energyStored() {
        return data.get(DATA_ENERGY);
    }

    public boolean isGenerating() {
        return data.get(DATA_GENERATING) != 0;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, ModBlocks.GAS_GENERATORS.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }
}
