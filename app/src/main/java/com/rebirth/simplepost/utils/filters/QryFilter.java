package com.rebirth.simplepost.utils.filters;

import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.Arrays;

@Data
public class QryFilter {

    private final String field;
    private final String logic;
    private final Object value;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("field", field)
                .add("logic", logic)
                .add("value", value instanceof String[] ? Arrays.toString((String[]) value) : value.toString())
                .toString();
    }
}
