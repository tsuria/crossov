package com.crossover.trial.properties.alext.properties.converts;

import com.crossover.trial.properties.alext.properties.BaseProperty;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alex on 1/14/2016.
 */
public class EnumPropertyParser implements PropertyParser<Enum> {

    private final Class<? extends Enum> enumType;
    private final Map<Enum, String> enumMembers;

    public EnumPropertyParser(Class<? extends Enum> enumType) {
        this.enumType = enumType;

        enumMembers = new TreeMap<>();
        for (Enum member : enumType.getEnumConstants()) {
            enumMembers.put(member, member.name());
        }
    }


    @Override
    public boolean isValidValue(String value) {
        Enum parsed = findEnumMemberByName(value);

        return parsed != null;
    }

    @Override
    public BaseProperty<Enum> parseValue(String name, String value) {

        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(isValidValue(value));

        Enum member = findEnumMemberByName(value);

        return new BaseProperty<Enum>(name, member, value);
    }

    @Override
    public Class getSupportedType() {
        return enumType;
    }

    private Enum findEnumMemberByName(String name) {
        for (Enum member : enumMembers.keySet()) {
            String memberName = enumMembers.get(member);
            if (StringUtils.equalsIgnoreCase(memberName, name)) {
                return member;
            }
        }
        return null;
    }



}