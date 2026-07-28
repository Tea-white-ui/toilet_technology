# API Changelog

## 1.0.0

- Introduced the stable `cn.tea.toilet.technology.api.gas` namespace.
- Added `Gas`, `GasStack`, `GasAction`, `IGasHandler`, and `GasCapabilities`.
- Added read-only built-in gas lookup through `ToiletGasRegistry`.
- Added reusable `BasicGasTank` and simulation-safe `GasTransfer` helpers.
- Explicitly excluded third-party gas registration, machine implementation classes, wire IDs, and
  Mekanism bridge types from the public API.
