package demo;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
    	System.out.println(System.getProperty("java.version"));
        String json = Files.readString(
                Path.of("datafiles/project.json")
        );

        ScriptEngine engine =
                new ScriptEngineManager().getEngineByName("nashorn");

        engine.put("jsonText", json);

        Object result = engine.eval(
                "Java.asJSONCompatible(JSON.parse(jsonText))"
        );

        List<Object> products = (List<Object>) result;

        for (Object object : products) {

            Map<String, Object> product =
                    (Map<String, Object>) object;

            String name = (String) product.get("name");
            Number price = (Number) product.get("price");

            System.out.println(
                    name + " / " + price.intValue() + "원"
            );
        }
    }
}