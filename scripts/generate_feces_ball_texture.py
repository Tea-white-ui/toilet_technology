from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "src/main/resources/assets/toilet_technology/textures/item/feces_ball.png"


def main():
    if OUTPUT.exists():
        raise FileExistsError(f"Refusing to overwrite existing texture: {OUTPUT}")

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)

    # Pixel-art dung ball with a darker outline and subtle surface highlights.
    outline = (66, 39, 21, 255)
    base = (112, 70, 37, 255)
    shadow = (83, 49, 25, 255)
    highlight = (150, 100, 55, 255)
    for y, span in ((3, (6, 9)), (4, (4, 11)), (5, (3, 12)), (6, (2, 13)),
                    (7, (2, 13)), (8, (2, 13)), (9, (3, 12)), (10, (4, 11)),
                    (11, (5, 10)), (12, (6, 9))):
        draw.line((span[0], y, span[1], y), fill=outline)
    for y, span in ((4, (6, 9)), (5, (5, 10)), (6, (4, 11)), (7, (3, 12)),
                    (8, (3, 12)), (9, (4, 11)), (10, (5, 10)), (11, (6, 9))):
        draw.line((span[0], y, span[1], y), fill=base)
    draw.point((6, 5), fill=highlight)
    draw.point((7, 5), fill=highlight)
    draw.point((5, 6), fill=highlight)
    draw.point((10, 9), fill=shadow)
    draw.point((9, 10), fill=shadow)

    image.save(OUTPUT)


if __name__ == "__main__":
    main()
