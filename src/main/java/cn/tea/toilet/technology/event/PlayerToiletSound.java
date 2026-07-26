package cn.tea.toilet.technology.event;

final class PlayerToiletSound {
    private static final float MIN_FART_PITCH = 0.8F;
    private static final float FART_PITCH_VARIATION = 0.4F;

    private PlayerToiletSound() {
    }

    static float getFartSoundPitch(float randomValue) {
        return MIN_FART_PITCH + randomValue * FART_PITCH_VARIATION;
    }
}
