# API 更新日志

## 1.0.0

- 引入稳定的 `cn.tea.toilet.technology.api.gas` 命名空间。
- 添加 `Gas`、`GasStack`、`GasAction`、`IGasHandler` 和 `GasCapabilities`。
- 通过 `ToiletGasRegistry` 添加只读的内置气体查询。
- 添加可复用的 `BasicGasTank` 和可安全模拟的 `GasTransfer` 辅助工具。
- 明确将第三方气体注册、机器实现类、网络 ID 与 Mekanism 桥接类型排除在公共 API 之外。
