"""Create the gas-tank dynamic-renderer layers from the source sprite.

The source is never overwritten. The visible gas window is x=[4, 12), y=[4, 13).
"""
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "src/main/resources/assets/toilet_technology/textures/item"
SOURCE = TEXTURES / "gas_tank.png"
WINDOW = (4, 4, 12, 13)
OUTPUTS = {
    "gas_tank_frame.png": "Opaque tank exterior with a fully transparent gas window.",
    "gas_tank_glass_50.png": "50% opaque glass tint over the gas window only.",
    "gas_tank_contents_mask.png": "Opaque white mask for the gas window only.",
}


def require_new_outputs() -> None:
    existing = [name for name in OUTPUTS if (TEXTURES / name).exists()]
    if existing:
        raise FileExistsError(f"Refusing to overwrite existing textures: {', '.join(existing)}")


def main() -> None:
    if not SOURCE.is_file():
        raise FileNotFoundError(f"Missing source texture: {SOURCE}")
    require_new_outputs()

    source = Image.open(SOURCE).convert("RGBA")
    if source.size != (16, 16):
        raise ValueError(f"Expected a 16x16 source texture, found {source.size}")

    frame = source.copy()
    glass = Image.new("RGBA", source.size, (0, 0, 0, 0))
    mask = Image.new("RGBA", source.size, (0, 0, 0, 0))
    pixels = source.load()
    frame_pixels = frame.load()
    glass_pixels = glass.load()
    mask_pixels = mask.load()

    left, top, right, bottom = WINDOW
    for y in range(top, bottom):
        for x in range(left, right):
            red, green, blue, alpha = pixels[x, y]
            frame_pixels[x, y] = (red, green, blue, 0)
            glass_pixels[x, y] = (red, green, blue, alpha // 2)
            mask_pixels[x, y] = (255, 255, 255, 255)

    frame.save(TEXTURES / "gas_tank_frame.png")
    glass.save(TEXTURES / "gas_tank_glass_50.png")
    mask.save(TEXTURES / "gas_tank_contents_mask.png")


if __name__ == "__main__":
    main()
