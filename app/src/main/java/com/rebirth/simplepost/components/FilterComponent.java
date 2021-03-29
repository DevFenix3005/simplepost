package com.rebirth.simplepost.components;

import com.rebirth.simplepost.utils.filters.QryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class FilterComponent {

    private static final String QUERY_PATTERN = "^(?<field>\\w+)(\\.)(?<logic>\\w+)(=)(?<value>[A-Za-z#0-9\\s]+)$";
    private final Pattern pattern = Pattern.compile(QUERY_PATTERN);

    public Map<String, QryFilter> query2Filtro(String query) {
        Map<String, QryFilter> filterMap = new HashMap<>();
        String[] filtros = query.split(",");
        for (String filtroSerializado : filtros) {

            Matcher matcher = pattern.matcher(filtroSerializado);
            if (matcher.find()) {
                String field = matcher.group("field");
                String logic = matcher.group("logic").toUpperCase(Locale.ROOT);
                String value = matcher.group("value");

                QryFilter qryFilter;
                switch (logic) {
                    case "IN":
                        String[] values = value.split("#");
                        qryFilter = new QryFilter(field, logic, values);
                        break;
                    case "LIKE":
                        qryFilter = new QryFilter(field, logic, "%" + value.toUpperCase(Locale.ROOT) + "%");
                        break;
                    default:
                        qryFilter = new QryFilter(field, logic, value);
                }

                filterMap.put(field, qryFilter);
            }

        }
        return filterMap;

    }


}
