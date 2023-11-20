package com.tohant.om2d.di;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

public class YamlConfigSource implements ConfigSource {

    private final Map<String, Object> yamlData;

    public YamlConfigSource(String yamlFilePath, Class<?> clazz) throws IOException {
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(yamlFilePath);
        Yaml yaml = new Yaml();
        yamlData = yaml.load(inputStream);
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public String getProperty(String propertyName) {
        Object propertyValue = yamlData.get(propertyName);
        return (propertyValue != null) ? propertyValue.toString() : null;
    }

}
