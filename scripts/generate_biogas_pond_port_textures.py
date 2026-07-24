#!/usr/bin/env python
"""Generate clearly distinguishable placeholder front textures for biogas pond ports."""

from pathlib import Path
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "src/main/resources/assets/toilet_technology/textures/block"
TRANSPARENT = (0, 0, 0, 0)


def write_png(path: Path, pixels: list[list[tuple[int, int, int, int]]]) -> None:
    raw = b"".join(b"\x00" + bytes(channel for pixel in row for channel in pixel) for row in pixels)

    def chunk(kind: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data))

    png = (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", 16, 16, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(raw, 9))
        + chunk(b"IEND", b"")
    )
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


def port_front(base: tuple[int, int, int], accent: tuple[int, int, int], symbol: str) -> list[list[tuple[int, int, int, int]]]:
    pixels = [[(*base, 255) for _ in range(16)] for _ in range(16)]
    dark = tuple(max(channel - 45, 0) for channel in base)
    light = tuple(min(channel + 45, 255) for channel in base)

    for index in range(16):
        pixels[0][index] = (*dark, 255)
        pixels[15][index] = (*dark, 255)
        pixels[index][0] = (*dark, 255)
        pixels[index][15] = (*dark, 255)
    for x in range(2, 14):
        pixels[2][x] = (*light, 255)
        pixels[13][x] = (*dark, 255)
    for y in range(2, 14):
        pixels[y][2] = (*light, 255)
        pixels[y][13] = (*dark, 255)

    glyphs = {
        "up": {(7, 4), (8, 4), (6, 5), (7, 5), (8, 5), (9, 5), (7, 6), (8, 6), (7, 7), (8, 7), (7, 8), (8, 8), (7, 9), (8, 9), (7, 10), (8, 10), (7, 11), (8, 11)},
        "in": {(4, 7), (5, 7), (6, 7), (7, 7), (8, 7), (9, 7), (10, 7), (11, 7), (9, 5), (10, 6), (9, 8), (10, 9)},
        "out": {(4, 7), (5, 7), (6, 7), (7, 7), (8, 7), (9, 7), (10, 7), (11, 7), (5, 5), (6, 6), (5, 8), (6, 9)},
        "drop": {(7, 4), (8, 4), (6, 5), (7, 5), (8, 5), (9, 5), (5, 6), (6, 6), (7, 6), (8, 6), (9, 6), (10, 6), (5, 7), (6, 7), (7, 7), (8, 7), (9, 7), (10, 7), (5, 8), (6, 8), (7, 8), (8, 8), (9, 8), (10, 8), (6, 9), (7, 9), (8, 9), (9, 9), (7, 10), (8, 10)},
    }
    for x, y in glyphs[symbol]:
        pixels[y][x] = (*accent, 255)
    return pixels


def main() -> None:
    ports = {
        "biogas_pond_gas_valve_front.png": ((80, 75, 50), (245, 220, 70), "up"),
        "biogas_pond_item_input_port_front.png": ((48, 85, 53), (95, 235, 115), "in"),
        "biogas_pond_fluid_input_port_front.png": ((42, 69, 95), (85, 180, 250), "drop"),
        "biogas_pond_item_output_port_front.png": ((100, 62, 40), (250, 165, 65), "out"),
    }
    for name, (base, accent, symbol) in ports.items():
        path = TEXTURES / name
        write_png(path, port_front(base, accent, symbol))
        print(f"generated {path.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
