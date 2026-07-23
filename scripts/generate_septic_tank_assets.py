from pathlib import Path
from PIL import Image, ImageDraw
import json

ROOT = Path(__file__).resolve().parents[1] / "src" / "main" / "resources"
ASSETS = ROOT / "assets" / "toilet_technology"

def write_json(path: Path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

def texture(path: Path, base, accent):
    path.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", (16, 16), base)
    draw = ImageDraw.Draw(image)
    draw.rectangle((0, 0, 15, 15), outline=accent)
    draw.line((0, 8, 15, 8), fill=accent)
    draw.line((8, 0, 8, 15), fill=accent)
    image.save(path)

def gui(path: Path):
    path.parent.mkdir(parents=True, exist_ok=True)
    image = Image.new("RGBA", (176, 185), (198, 198, 198, 255))
    draw = ImageDraw.Draw(image)
    draw.rectangle((0, 0, 175, 184), outline=(55, 55, 55, 255))
    for box in [(15, 20, 64, 51), (15, 54, 64, 85)]:
        draw.rectangle(box, fill=(35, 35, 35, 255), outline=(240, 240, 240, 255))
    for x, y in [(77, 36), (77, 60), (123, 23), (153, 23), (123, 43), (153, 43), (123, 63), (153, 63)]:
        draw.rectangle((x - 1, y - 1, x + 16, y + 16), fill=(90, 90, 90, 255), outline=(245, 245, 245, 255))
    for row in range(3):
        for col in range(9):
            x, y = 8 + col * 18, 103 + row * 18
            draw.rectangle((x - 1, y - 1, x + 16, y + 16), fill=(90, 90, 90, 255), outline=(235, 235, 235, 255))
    for col in range(9):
        x, y = 8 + col * 18, 161
        draw.rectangle((x - 1, y - 1, x + 16, y + 16), fill=(90, 90, 90, 255), outline=(235, 235, 235, 255))
    image.save(path)

for name in ("septic_tank_controller", "septic_tank_wall"):
    variants = {"": {"model": f"toilet_technology:block/{name}"}}
    if name == "septic_tank_controller":
        variants = {
            "facing=north": {"model": "toilet_technology:block/septic_tank_controller"},
            "facing=east": {"model": "toilet_technology:block/septic_tank_controller", "y": 90},
            "facing=south": {"model": "toilet_technology:block/septic_tank_controller", "y": 180},
            "facing=west": {"model": "toilet_technology:block/septic_tank_controller", "y": 270}
        }
    write_json(ASSETS / "blockstates" / f"{name}.json", {"variants": variants})
    if name == "septic_tank_controller":
        model = {"parent": "minecraft:block/cube", "textures": {
            "down": "toilet_technology:block/septic_tank_controller",
            "up": "toilet_technology:block/septic_tank_controller",
            "north": "toilet_technology:block/septic_tank_controller_front",
            "south": "toilet_technology:block/septic_tank_controller",
            "west": "toilet_technology:block/septic_tank_controller",
            "east": "toilet_technology:block/septic_tank_controller",
            "particle": "toilet_technology:block/septic_tank_controller"
        }}
    else:
        model = {"parent": "minecraft:block/cube_all", "textures": {"all": f"toilet_technology:block/{name}"}}
    write_json(ASSETS / "models" / "block" / f"{name}.json", model)
    write_json(ASSETS / "models" / "item" / f"{name}.json", {"parent": f"toilet_technology:block/{name}"})
    write_json(ROOT / "data" / "toilet_technology" / "loot_table" / "blocks" / f"{name}.json", {
        "type": "minecraft:block", "pools": [{"bonus_rolls": 0.0, "conditions": [{"condition": "minecraft:survives_explosion"}],
        "entries": [{"type": "minecraft:item", "name": f"toilet_technology:{name}"}], "rolls": 1.0}]})

texture(ASSETS / "textures" / "block" / "septic_tank_controller.png", (72, 82, 84, 255), (118, 150, 118, 255))
texture(ASSETS / "textures" / "block" / "septic_tank_controller_front.png", (52, 64, 66, 255), (80, 220, 110, 255))
texture(ASSETS / "textures" / "block" / "septic_tank_wall.png", (90, 96, 98, 255), (55, 61, 63, 255))
gui(ASSETS / "textures" / "gui" / "container" / "septic_tank.png")

translations = {
    "en_us": {
        "block.toilet_technology.septic_tank_controller": "Septic Tank Controller",
        "block.toilet_technology.septic_tank_wall": "Septic Tank Wall",
        "message.toilet_technology.septic_tank_invalid": "The 3×4×3 septic tank structure is incomplete",
        "gui.toilet_technology.gas": "Gas: %s / %s mB",
        "gui.toilet_technology.liquid": "Liquid: %s / %s mB"
    },
    "zh_cn": {
        "block.toilet_technology.septic_tank_controller": "化粪池控制器",
        "block.toilet_technology.septic_tank_wall": "化粪池壁",
        "message.toilet_technology.septic_tank_invalid": "3×4×3 化粪池结构不完整",
        "gui.toilet_technology.gas": "气体：%s / %s mB",
        "gui.toilet_technology.liquid": "液体：%s / %s mB"
    }
}
for locale, values in translations.items():
    lang_path = ROOT.parent.parent / "generated" / "resources" / "assets" / "toilet_technology" / "lang" / f"{locale}.json"
    existing = json.loads(lang_path.read_text(encoding="utf-8")) if lang_path.exists() else {}
    existing.update(values)
    write_json(lang_path, existing)

print("Generated septic tank placeholder assets and data files.")
