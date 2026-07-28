from pathlib import Path
from PIL import Image, ImageDraw

TARGET = Path("src/main/resources/assets/toilet_technology/textures/gui/container/gas_generators.png")

if TARGET.exists():
    raise SystemExit(f"Refusing to overwrite existing texture: {TARGET}")

TARGET.parent.mkdir(parents=True, exist_ok=True)
image = Image.new("RGBA", (176, 166), (0, 0, 0, 0))
draw = ImageDraw.Draw(image)
draw.rectangle((0, 0, 175, 165), fill=(198, 198, 198, 255), outline=(80, 80, 80, 255))
draw.rectangle((4, 4, 171, 75), fill=(139, 139, 139, 255), outline=(255, 255, 255, 255))
draw.rectangle((7, 25, 38, 70), fill=(50, 50, 50, 255), outline=(20, 20, 20, 255))
draw.rectangle((137, 25, 168, 70), fill=(50, 50, 50, 255), outline=(20, 20, 20, 255))
draw.rectangle((7, 84, 168, 160), fill=(198, 198, 198, 255), outline=(255, 255, 255, 255))
for row in range(3):
    for column in range(9):
        x = 7 + column * 18
        y = 83 + row * 18
        draw.rectangle((x, y, x + 17, y + 17), fill=(139, 139, 139, 255), outline=(80, 80, 80, 255))
for column in range(9):
    x = 7 + column * 18
    y = 141
    draw.rectangle((x, y, x + 17, y + 17), fill=(139, 139, 139, 255), outline=(80, 80, 80, 255))
image.save(TARGET)
print(TARGET)
