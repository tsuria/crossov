package com.crossover.trial.properties.alext;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

/**
 * Created by alex on 1/16/2016.
 */
public class PropertiesLoaderImpl implements PropertiesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoaderImpl.class);
    private final JsonPropertiesParser jpParser;

    public PropertiesLoaderImpl(JsonPropertiesParser jpParser) {
        this.jpParser = jpParser;
    }


    @Override
    public Properties loadResource(String resource) {

        Preconditions.checkNotNull(resource);
        Properties props = new Properties();

        try {

            InputStream ins = getInputStream(resource);
            String ext = StringUtils.lowerCase(FilenameUtils.getExtension(resource));

            switch (ext) {
                case "json":
                    Map<String, String> parsedValues = jpParser.parse(ins);
                    props.putAll(parsedValues);
                    break;

                case "properties":
                    props.load(ins);
                    break;

                default:
                    LOGGER.error("unexpected file uri extension " + resource + " - " + ext);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("cannot load " + resource, e);
        }
        return props;
    }

    private InputStream getInputStream(String resource) throws IOException {
        InputStream ins;
        if (StringUtils.startsWithIgnoreCase(resource, "classpath:")) {
            String key = StringUtils.removeStartIgnoreCase(resource, "classpath:resources");
            ins = this.getClass().getResourceAsStream(key);
        } else {
            URI uri = URI.create(resource);
            byte[] content = IOUtils.toByteArray(uri);
            ins = new ByteArrayInputStream(content);
        }
        return ins;
    }
}
