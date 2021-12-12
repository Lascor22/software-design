package ru.itmo.sd.tasks.configuration;

import org.springframework.core.convert.converter.Converter;

public class ToStringConverter implements Converter<Object, String> {

    @Override
    public String convert(Object source) {
        return source.toString();
    }
}
