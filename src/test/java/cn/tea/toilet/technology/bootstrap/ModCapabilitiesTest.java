package cn.tea.toilet.technology.bootstrap;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModCapabilitiesTest {
    @Test
    void registersItemCapabilitiesForBiogasPondPorts() throws IOException {
        String source = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/bootstrap/ModCapabilities.java"));

        assertTrue(source.contains("event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),"),
                "Biogas pond item input/output ports must expose an ItemHandler capability to item pipes");
    }

    @Test
    void registersGasMeltingFurnaceAutomationCapabilities() throws IOException {
        String capabilities = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/bootstrap/ModCapabilities.java"));
        String mekanismCompat = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/compat/mekanism/MekanismCompat.java"));

        assertTrue(capabilities.contains("event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.GAS_MELTING_FURNACE.get(),"),
                "Gas melting furnace must expose its item handler to pipes and hoppers");
        assertTrue(capabilities.contains("event.registerBlockEntity(GasCapabilities.BLOCK, ModBlockEntities.GAS_MELTING_FURNACE.get(),"),
                "Gas melting furnace must expose its native gas handler to gas pipes");
        assertTrue(mekanismCompat.contains("event.registerBlockEntity(CHEMICAL_BLOCK, ModBlockEntities.GAS_MELTING_FURNACE.get(),"),
                "Gas melting furnace must expose the optional Mekanism chemical bridge when Mekanism is installed");
    }

    @Test
    void gasPipesDiscoverMekanismChemicalHandlersThroughTheOptionalBridge() throws IOException {
        String scheduler = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/block/gaspipe/GasPipeNetworkScheduler.java"));
        String pipeBlock = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/block/gaspipe/GasPipeBlock.java"));
        String mekanismCompat = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/compat/mekanism/MekanismCompat.java"));

        assertTrue(scheduler.contains("ExternalGasHandlers.find(level, neighborPos, direction.getOpposite())"),
                "Pipe networks must use the optional external gas-handler lookup for Mekanism tanks");
        assertTrue(pipeBlock.contains("ExternalGasHandlers.find(level, neighborPos, direction.getOpposite())"),
                "Pipe connection models must recognize optional Mekanism chemical handlers");
        assertTrue(mekanismCompat.contains("ExternalGasHandlers.register(MekanismCompat::findChemicalHandler)"),
                "Mekanism compatibility must register the reverse chemical-to-native adapter");
        assertTrue(mekanismCompat.contains("new NativeGasHandlerAdapter(handler)"),
                "Mekanism chemical handlers must be adapted to the internal gas API");
    }
}
