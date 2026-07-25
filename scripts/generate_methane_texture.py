import os
import struct
import zlib

path = "src/main/resources/assets/toilet_technology/textures/block/methane.png"
if os.path.exists(path):
    raise FileExistsError(f"Refusing to overwrite existing texture: {path}")

width = height = 16
pixels = bytearray()
for y in range(height):
    for x in range(width):
        wave = (x * 13 + y * 7) % 30
        pixels.extend((0, 96 + wave, 128 + (x * 7 + y * 11) % 40, 145 + (x * 5 + y * 3) % 55))

scanlines = b"".join(
    b"\x00" + bytes(pixels[y * width * 4:(y + 1) * width * 4])
    for y in range(height)
)

def chunk(kind, data):
    return (
        struct.pack(">I", len(data))
        + kind
        + data
        + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)
    )

png = (
    b"\x89PNG\r\n\x1a\n"
    + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
    + chunk(b"IDAT", zlib.compress(scanlines, 9))
    + chunk(b"IEND", b"")
)
with open(path, "xb") as texture:
    texture.write(png)

print(f"Created {path}: {width}x{height}, {len(png)} bytes")
