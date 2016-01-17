package com.crossover.trial.properties.alext.properties;

import com.crossover.trial.properties.alext.properties.converts.PropertyParser;

import java.util.List;

/**
 * Created by alex on 1/14/2016.
 */
public class PropertyMatcherImpl implements PropertyMatcher {

    private final PropertyParser[] converters;

    public PropertyMatcherImpl(PropertyParser... converters) {

        this.converters = converters;
    }

    @Override
    public BaseProperty parseProperty(String name, List<String> values) {

        String lastValue = values.get(values.size() - 1);
        converter:
        for (PropertyParser converter : converters) {

            for (String value : values) {
                if (!converter.isValidValue(value)) {
                    continue converter;
                }
            }

            return converter.parseValue(name, lastValue);
        }

        //By Default any property can be string
        return new BaseProperty<String>(name, lastValue, lastValue);

    }
}