#!/usr/bin/env python
"""Generate dedicated 16×16 RGBA textures for septic tank I/O ports."""

from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
TEXTURES = ROOT / "src/main/resources/assets/toilet_technology/textures/block"


def solid_port_texture(path: Path, base: tuple[int, int, int, int], accent: tuple[int, int, int, int]) -> None:
    """Create the metal side texture shared by all non-front port faces."""
    image = Image.new("RGBA", (16, 16), base)
    draw = ImageDraw.Draw(image)
    draw.rectangle((0, 0, 15, 15), outline=accent)
    draw.line((0, 8, 15, 8), fill=accent)
    draw.line((8, 0, 8, 15), fill=accent)
    image.save(path)


def port_front_texture(
    path: Path,
    base: tuple[int, int, int, int],
    accent: tuple[int, int, int, int],
    symbol: str,
) -> None:
    """Create a framed front panel with a liquid droplet or gas outlet glyph."""
    image = Image.new("RGBA", (16, 16), base)
    draw = ImageDraw.Draw(image)
    dark = tuple(max(channel - 35, 0) for channel in base[:3]) + (255,)
    light = tuple(min(channel + 35, 255) for channel in base[:3]) + (255,)
    draw.rectangle((0, 0, 15, 15), fill=base, outline=dark)
    draw.rectangle((2, 2, 13, 13), outline=light)
    draw.rectangle((3, 3, 12, 12), outline=dark)

    if symbol == "liquid":
        draw.rectangle((5, 8, 10, 11), fill=accent)
        draw.line((5, 7, 10, 7), fill=accent)
        draw.point((7, 5), fill=accent)
        draw.point((8, 6), fill=accent)
    elif symbol == "gas":
        draw.rectangle((6, 5, 9, 10), fill=accent)
        draw.rectangle((5, 6, 10, 8), fill=accent)
        draw.line((7, 11, 8, 11), fill=accent)
    else:
        raise ValueError(f"Unknown port symbol: {symbol}")

    image.save(path)


def main() -> None:
    TEXTURES.mkdir(parents=True, exist_ok=True)
    ports = {
        "septic_tank_liquid_input_port": ((48, 82, 84, 255), (70, 190, 205, 255), (38, 70, 73, 255), (80, 225, 235, 255), "liquid"),
        "septic_tank_gas_valve": ((78, 82, 48, 255), (190, 195, 70, 255), (62, 66, 39, 255), (225, 220, 75, 255), "gas"),
    }
    for name, (side_base, side_accent, front_base, front_accent, symbol) in ports.items():
        side_path = TEXTURES / f"{name}.png"
        front_path = TEXTURES / f"{name}_front.png"
        existing = [path for path in (side_path, front_path) if path.exists()]
        if existing:
            raise FileExistsError("Refusing to overwrite existing texture(s): " + ", ".join(str(path) for path in existing))
        solid_port_texture(side_path, side_base, side_accent)
        port_front_texture(front_path, front_base, front_accent, symbol)
        print(f"generated textures/block/{name}.png and {name}_front.png")


if __name__ == "__main__":
    main()
