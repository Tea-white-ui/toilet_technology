package cn.tea.toilet.technology;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Shared mod identity and logging infrastructure.
 */
public final class ModConstants {
    public static final String MOD_ID = "toilet_technology";
    public static final Logger LOGGER = LogUtils.getLogger();

    private ModConstants() {
    }
}
