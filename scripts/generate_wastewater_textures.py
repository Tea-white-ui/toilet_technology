#!/usr/bin/env python
"""Generate placeholder wastewater textures without ever overwriting source art."""

from pathlib import Path
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
TARGETS = {
    ROOT / "src/main/resources/assets/toilet_technology/textures/block/wastewater.png": (16, 112),
    ROOT / "src/main/resources/assets/toilet_technology/textures/block/wastewater_flowing.png": (32, 128),
    ROOT / "src/main/resources/assets/toilet_technology/textures/item/wastewater_bucket.png": (16, 16),
}


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


def wastewater_texture(width: int, height: int) -> list[list[tuple[int, int, int, int]]]:
    palette = [(49, 108, 101, 190), (63, 132, 117, 200), (36, 83, 83, 205), (90, 153, 125, 190)]
    return [
        [palette[(x * 5 + y * 3 + (x * y) // 11) % len(palette)] for x in range(width)]
        for y in range(height)
    ]


def bucket_texture() -> list[list[tuple[int, int, int, int]]]:
    transparent = (0, 0, 0, 0)
    pixels = [[transparent for _ in range(16)] for _ in range(16)]
    outline, metal, shine = (43, 53, 58, 255), (133, 151, 156, 255), (203, 219, 218, 255)
    wastewater, foam = (49, 108, 101, 255), (104, 164, 137, 255)
    for y in range(4, 14):
        inset = 2 if y in (4, 13) else 1
        for x in range(3 + inset, 13 - inset):
            edge = x in (3 + inset, 12 - inset) or y in (4, 13)
            pixels[y][x] = outline if edge else (shine if x == 6 else metal)
    for y in range(8, 12):
        for x in range(5, 11):
            pixels[y][x] = foam if (x + y) % 4 == 0 else wastewater
    for x in range(5, 11):
        pixels[3][x] = outline
    for x in range(6, 10):
        pixels[2][x] = metal
    pixels[5][3] = pixels[6][3] = pixels[5][12] = pixels[6][12] = outline
    return pixels


def main() -> None:
    existing = [path for path in TARGETS if path.exists()]
    if existing:
        raise FileExistsError("Refusing to overwrite existing texture(s): " + ", ".join(str(path.relative_to(ROOT)) for path in existing))
    for path, (width, height) in TARGETS.items():
        pixels = bucket_texture() if path.name == "wastewater_bucket.png" else wastewater_texture(width, height)
        write_png(path, pixels)
        print(f"generated {path.relative_to(ROOT)}: {width}x{height}")


if __name__ == "__main__":
    main()