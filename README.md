# Toilet Technology

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?logo=minecraft&logoColor=white)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.241-orange)](https://neoforged.net/)

[简体中文](README_zh_CN.md)

**Toilet Technology** is a technology mod for Minecraft 1.21.1 on NeoForge. It centers on collecting, processing, and reclaiming biological waste, connecting toilets, slurry, wastewater, fermentation, and gas treatment into an automatable resource cycle.

> The project is still under development and testing. Recipes, values, structures, and functionality may change between versions.

- **Mod integrations:** Optional gas compatibility with Mekanism is supported. Some PoopSky waste items and slurry are recognized as processing inputs.

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.241 or later |
| Java | 21 |

JEI and Mekanism are optional dependencies. Without Mekanism installed, the mod continues to run using its own gas system.

## Development and Build

Install JDK 21, then run this command from the project root:

```bash
./gradlew build
```

Common development tasks:

```bash
# Launch the development client
./gradlew runClient

# Launch the development server
./gradlew runServer

# Run unit tests
./gradlew test

# Run the data generator
./gradlew runData
```

### Build Verification Without Mekanism

Compile while excluding the Mekanism compatibility layer with:

```bash
./gradlew compileJava -PwithoutMekanism
```

## Addon API

The stable gas API for addon developers is in `cn.tea.toilet.technology.api.**`. See [docs/api/README.md](docs/api/README.md) for usage, capability lifecycle requirements, built-in gas limitations, and local Maven publishing instructions.

The current public API version is `1.0.0`. Dynamic third-party gas registration is outside the scope of API 1.0.

## Author

- Tea_white_

## Acknowledgments

- Sounds and some texture effects reference or use material from [PoopSkyMod](https://github.com/Altnoir/PoopSkyMod), by [Altnoir](https://github.com/Altnoir), which is licensed under the MIT License.

## License

- This project is licensed under the MIT License.
