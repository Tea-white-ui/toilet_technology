#!/usr/bin/env python
"""Generate the 16x16 item texture for the sealing component."""

from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
TEXTURE = ROOT / "src/main/resources/assets/toilet_technology/textures/item/sealing_component.png"


def main() -> None:
    TEXTURE.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    copper = (184, 105, 55, 255)
    copper_highlight = (236, 159, 94, 255)
    dark = (82, 50, 39, 255)
    slime = (112, 180, 82, 255)
    slime_highlight = (174, 224, 119, 255)

    # Copper sealing ring with a green slime seal in the centre.
    draw.rectangle((3, 2, 12, 13), fill=dark)
    draw.rectangle((4, 2, 11, 3), fill=copper_highlight)
    draw.rectangle((4, 4, 11, 11), fill=copper)
    draw.rectangle((5, 5, 10, 10), fill=slime)
    draw.rectangle((6, 5, 8, 6), fill=slime_highlight)
    draw.rectangle((4, 12, 11, 13), fill=dark)
    draw.point((12, 4), fill=copper_highlight)
    draw.point((3, 11), fill=copper_highlight)
    image.save(TEXTURE)
    print(f"Generated {TEXTURE}")


if __name__ == "__main__":
    main()