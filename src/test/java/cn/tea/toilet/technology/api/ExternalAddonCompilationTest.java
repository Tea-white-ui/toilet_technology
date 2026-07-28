package cn.tea.toilet.technology.api;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExternalAddonCompilationTest {
    @Test
    void externalAddonCanCompileUsingOnlyThePublicGasApi() throws IOException {
        Path fixtureDirectory = Files.createTempDirectory("toilet-technology-api-addon");
        Path source = fixtureDirectory.resolve("ExampleAddon.java");
        Files.writeString(source, """
                package example.addon;

                import cn.tea.toilet.technology.api.gas.GasAction;
                import cn.tea.toilet.technology.api.gas.GasStack;
                import cn.tea.toilet.technology.api.gas.IGasHandler;
                import cn.tea.toilet.technology.api.gas.ToiletGasRegistry;

                public final class ExampleAddon {
                    public GasStack simulateInsert(IGasHandler handler) {
                        GasStack offered = new GasStack(ToiletGasRegistry.BIOGAS, 100);
                        return handler.insertGas(0, offered, GasAction.SIMULATE);
                    }
                }
                """);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "A JDK is required to compile the external addon fixture");
        int result = compiler.run(null, null, null,
                "-classpath", System.getProperty("java.class.path"),
                "-d", fixtureDirectory.resolve("classes").toString(),
                source.toString());

        assertEquals(0, result);
    }
}
