package com.librbary.main.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Objects;

public class YamlConfig implements PropertySourceFactory {
  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
    var factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());
    var properties = factory.getObject();
    return Objects.isNull(properties)
        ? null
        : new PropertiesPropertySource(Objects.requireNonNull(resource.getResource().getFilename()),
            properties);
  }
}
