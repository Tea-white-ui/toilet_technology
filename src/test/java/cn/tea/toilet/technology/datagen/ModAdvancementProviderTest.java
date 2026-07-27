package cn.tea.toilet.technology.datagen;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModAdvancementProviderTest {

    @Test
    void definesEachProgressionAdvancementExactlyOnceInTreeOrder() {
        List<String> ids = ModAdvancementProvider.advancementIds();

        assertEquals(List.of(
                "root",
                "obtain_feces",
                "dry_feces",
                "build_septic_tank",
                "build_biogas_pond",
                "use_biogas_residue",
                "purify_sewage",
                "refine_methane"
        ), ids);
        assertEquals(ids.size(), ids.stream().distinct().count());
        assertTrue(ids.contains("root"));
    }
}
