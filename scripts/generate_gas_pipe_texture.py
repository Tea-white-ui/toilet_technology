from pathlib import Path

try:
    from PIL import Image
except ImportError as exc:
    raise SystemExit("Pillow is required: python -m pip install Pillow") from exc

TARGETS = [
    Path("src/main/resources/assets/toilet_technology/textures/block/gas_pipe.png"),
]

existing = [path for path in TARGETS if path.exists()]
if existing:
    raise SystemExit("Refusing to overwrite existing texture(s): " + ", ".join(map(str, existing)))

# Temporary 16x16 opaque metal pipe texture. It is intentionally simple so it can be replaced later.
image = Image.new("RGBA", (16, 16), (70, 78, 82, 255))
pixels = image.load()
for y in range(16):
    for x in range(16):
        checker = ((x // 4) + (y // 4)) % 2
        base = 72 + checker * 7
        pixels[x, y] = (base, base + 8, base + 11, 255)

# Riveted border and a subtle cyan pressure stripe.
for i in range(16):
    pixels[i, 0] = pixels[i, 15] = (37, 43, 46, 255)
    pixels[0, i] = pixels[15, i] = (37, 43, 46, 255)
for x, y in ((2, 2), (13, 2), (2, 13), (13, 13)):
    pixels[x, y] = (151, 161, 165, 255)
for x in range(2, 14):
    pixels[x, 7] = (42, 118, 128, 255)
    pixels[x, 8] = (65, 161, 169, 255)

for path in TARGETS:
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path)
    print(f"Created {path}")
