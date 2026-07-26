package cn.tea.toilet.technology.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cn.tea.toilet.technology.gas.GasRegistry;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

/** Generates optional Mekanism-compatible data without linking against its API. */
public final class MekanismGeneratorCompatDataProvider implements DataProvider {
    private static final int BIOGAS_BURN_TICKS = 40;
    private static final long BIOGAS_ENERGY_PER_TICK = 100;
    private static final int METHANE_BURN_TICKS = 20;
    private static final long METHANE_ENERGY_PER_TICK = 800;

    private final PackOutput output;

    public MekanismGeneratorCompatDataProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        String biogasId = GasRegistry.BIOGAS.id().toString();
        String methaneId = GasRegistry.METHANE.id().toString();
        Path gaseousTag = output.getOutputFolder()
                .resolve("data/mekanism/tags/mekanism/chemical/gaseous.json");
        Path fuelDataMap = output.getOutputFolder()
                .resolve("data/mekanism/data_maps/mekanism/chemical/chemical_attribute_fuel.json");

        JsonObject gaseous = new JsonObject();
        JsonArray gaseousValues = new JsonArray();
        gaseousValues.add(biogasId);
        gaseousValues.add(methaneId);
        gaseous.add("values", gaseousValues);

        JsonObject fuel = new JsonObject();
        JsonObject values = new JsonObject();
        JsonObject biogasFuel = new JsonObject();
        biogasFuel.addProperty("burn_time", BIOGAS_BURN_TICKS);
        biogasFuel.addProperty("energy", BIOGAS_ENERGY_PER_TICK);
        values.add(biogasId, biogasFuel);
        JsonObject methaneFuel = new JsonObject();
        methaneFuel.addProperty("burn_time", METHANE_BURN_TICKS);
        methaneFuel.addProperty("energy", METHANE_ENERGY_PER_TICK);
        values.add(methaneId, methaneFuel);
        fuel.add("values", values);

        return CompletableFuture.allOf(
                DataProvider.saveStable(cachedOutput, gaseous, gaseousTag),
                DataProvider.saveStable(cachedOutput, fuel, fuelDataMap));
    }

    @Override
    public String getName() {
        return "Mekanism Generators compatibility";
    }
}