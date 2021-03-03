package io.github.coolmineman.coolconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class CoolConfigTest {
    @Test
    void test() {
        TestConfig config = CoolConfig.create(TestConfig.class);
        assertNotNull(config);
        assertNotNull(config.getSchema());
        assertNotNull(config.getData());
        assertEquals(2, config.speed());
        assertEquals(Map.of(5, "Lol", 8, "wat"), config.bruh());
        HashMap<String, NestedTextNode> fileMap = new HashMap<>();
        fileMap.put("speed", NestedTextNode.of("5"));
        fileMap.put("epic_list", NestedTextNode.of(List.of(NestedTextNode.of("5"), NestedTextNode.of("6"), NestedTextNode.of("7"))));
        fileMap.put("is_epic", NestedTextNode.of("true"));
        fileMap.put("bruh", NestedTextNode.of(Map.of("6", NestedTextNode.of("Yeet"), "9", NestedTextNode.of("Epic"))));
        CoolConfigNt.load(config, NestedTextNode.of(fileMap));
        assertEquals(5, config.speed());
        assertEquals(List.of(5, 6, 7), config.epic_list());
        assertEquals(Map.of(6, "Yeet", 9, "Epic"), config.bruh());
        assertEquals(true, config.is_epic());
    }

    public interface TestConfig extends Config {
        default int speed() {
            return 2;
        }
        default Map<Integer, String> bruh() {
            return Map.of(5, "Lol", 8, "wat");
        }
        List<Integer> epic_list();
        boolean is_epic();
    }
}
