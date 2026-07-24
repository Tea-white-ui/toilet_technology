from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[1]
OUTPUTS = {
    "metal_mesh": ROOT / "src/main/resources/assets/toilet_technology/textures/item/metal_mesh.png",
    "multi_layer_sintered_metal_mesh": ROOT / "src/main/resources/assets/toilet_technology/textures/item/multi_layer_sintered_metal_mesh.png",
}


def main():
    for output in OUTPUTS.values():
        output.parent.mkdir(parents=True, exist_ok=True)
        image = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(image)

        # Draw two crossing metallic strands as a transparent inventory texture.
        for offset in range(-16, 17, 6):
            draw.line((offset, 0, offset + 16, 16), fill=(155, 164, 169, 255), width=2)
            draw.line((offset + 16, 0, offset, 16), fill=(77, 84, 88, 255), width=1)
        for offset in range(-16, 17, 6):
            draw.line((offset, 0, offset + 16, 16), fill=(77, 84, 88, 255), width=1)
            draw.line((offset + 16, 0, offset, 16), fill=(155, 164, 169, 255), width=2)

        image.save(output)


if __name__ == "__main__":
    main()
