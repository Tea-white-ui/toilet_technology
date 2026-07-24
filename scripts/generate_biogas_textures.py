#!/usr/bin/env python
"""Generate placeholder textures for biogas and its sealed tank container."""

from pathlib import Path
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "src/main/resources/assets/toilet_technology/textures"


def write_png(path: Path, pixels: list[list[tuple[int, int, int, int]]]) -> None:
    height = len(pixels)
    width = len(pixels[0])
    raw = b"".join(b"\x00" + bytes(channel for pixel in row for channel in pixel) for row in pixels)

    def chunk(kind: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data))

    png = (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(raw, 9))
        + chunk(b"IEND", b"")
    )
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


def biogas_texture() -> list[list[tuple[int, int, int, int]]]:
    palette = [(220, 228, 197, 170), (188, 202, 151, 185), (151, 169, 107, 195)]
    pixels = []
    for y in range(16):
        row = []
        for x in range(16):
            color = palette[(x * 3 + y * 5 + (x * y) // 7) % len(palette)]
            alpha = color[3] + (15 if (x + y) % 5 == 0 else 0)
            row.append((*color[:3], min(alpha, 220)))
        pixels.append(row)
    return pixels


def tank_texture() -> list[list[tuple[int, int, int, int]]]:
    transparent = (0, 0, 0, 0)
    pixels = [[transparent for _ in range(16)] for _ in range(16)]
    dark, steel, light = (54, 61, 60, 255), (126, 139, 132, 255), (201, 211, 195, 255)
    gas, gas_light = (121, 143, 76, 255), (172, 187, 117, 255)

    for y in range(3, 14):
        inset = 2 if y in (3, 13) else 1
        for x in range(3 + inset, 13 - inset):
            edge = x in (3 + inset, 12 - inset) or y in (3, 13)
            pixels[y][x] = dark if edge else (light if x == 6 else steel)
    for y in range(7, 12):
        for x in range(5, 11):
            pixels[y][x] = gas_light if x == 6 else gas
    for x in range(5, 11):
        pixels[2][x] = dark
    pixels[1][6] = pixels[1][7] = pixels[1][8] = pixels[1][9] = steel
    pixels[4][4] = pixels[5][3] = pixels[6][3] = dark
    pixels[4][11] = pixels[5][12] = pixels[6][12] = dark
    return pixels


def main() -> None:
    targets = {
        TEXTURES / "block/biogas.png": biogas_texture(),
        TEXTURES / "item/biogas_tank.png": tank_texture(),
    }
    for path, pixels in targets.items():
        write_png(path, pixels)
        print(f"generated {path.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
