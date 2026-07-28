# 厕所技艺（Toilet Technology）

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62B47A?logo=minecraft&logoColor=white)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.241-orange)](https://neoforged.net/)

**厕所技艺**是一个面向 Minecraft 1.21.1 的 NeoForge 科技模组。模组以生物排泄物的收集、处理与资源化利用为主题，将厕所、粪液、污水、发酵和气体处理串联为一套可自动化的资源循环系统。

> 项目仍在开发与测试阶段；配方、数值、结构和功能可能随版本调整。

- **模组联动**：支持与 Mekanism 的可选气体兼容；可识别 PoopSky 的部分粪便物品和粪液作为加工输入。

## 环境要求

| 项目 | 版本 |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.241 或更高版本 |
| Java | 21 |

JEI 与 Mekanism 均为可选依赖；未安装 Mekanism 时，模组仍可使用自身的气体系统运行。

## 开发与构建

请先安装 JDK 21，然后在项目根目录执行：

```bash
./gradlew build
```

常用开发任务：

```bash
# 启动开发客户端
./gradlew runClient

# 启动开发服务端
./gradlew runServer

# 运行单元测试
./gradlew test

# 运行数据生成器
./gradlew runData
```

### 无 Mekanism 构建验证

Mekanism 兼容层可以通过以下任务排除后进行编译验证：

```bash
./gradlew compileJava -PwithoutMekanism
```

## 附属模组 API

面向附属模组开发者的稳定气体 API 位于 `cn.tea.toilet.technology.api.**`。使用方式、Capability 生命周期、内置气体限制和本地 Maven 发布说明见 [docs/api/README.md](docs/api/README.md)。

公共 API 当前版本为 `1.0.0`；第三方气体动态注册不属于 API 1.0 的范围。

## 作者

- Tea_white_

## 致谢

- 声音与部分纹理效果参考/使用自 [PoopSkyMod](https://github.com/Altnoir/PoopSkyMod)，作者为 [Altnoir](https://github.com/Altnoir)，项目采用 MIT License。

## 许可证

- 本项目采用 MIT License。
