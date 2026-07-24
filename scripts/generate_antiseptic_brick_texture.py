#!/usr/bin/env python
"""Generate the 16x16 texture for the antiseptic brick block."""

from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
TEXTURE = ROOT / "src/main/resources/assets/toilet_technology/textures/block/antiseptic_brick.png"


def main() -> None:
    TEXTURE.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", (16, 16), (116, 136, 128, 255))
    draw = ImageDraw.Draw(image)
    mortar = (58, 72, 67, 255)
    highlight = (155, 175, 162, 255)
    draw.line((0, 0, 15, 0), fill=highlight)
    draw.line((0, 7, 15, 7), fill=mortar)
    draw.line((0, 15, 15, 15), fill=mortar)
    draw.line((0, 0, 0, 15), fill=mortar)
    draw.line((7, 1, 7, 6), fill=mortar)
    draw.line((11, 8, 11, 14), fill=mortar)
    image.save(TEXTURE)
    print(f"Generated {TEXTURE}")


if __name__ == "__main__":
    main()
