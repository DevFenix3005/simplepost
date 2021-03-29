package com.rebirth.simplepost.customejooq;

import org.jibx.schema.codegen.extend.DefaultNameConverter;
import org.jibx.schema.codegen.extend.NameConverter;
import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;
import org.jooq.tools.StringUtils;

public class MyCustomeJooqNameGenerator extends DefaultGeneratorStrategy {

    private final NameConverter nameTools = new DefaultNameConverter();

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        if (mode == Mode.POJO) {
            return super.getJavaPackageName(definition, mode).replace("pojos", "dtos");
        } else if (mode == Mode.DAO) {
            return super.getJavaPackageName(definition, mode).replace("daos", "repositories");
        } else {
            return super.getJavaPackageName(definition, mode);
        }
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {

        String javaClassName;
        if (mode == Mode.POJO || mode == Mode.DAO) {
            StringBuilder result = new StringBuilder();
            String outputName = nameTools.depluralize(definition.getOutputName());
            result.append(StringUtils.toCamelCase(
                    outputName.replace(' ', '_')
                            .replace('-', '_')
                            .replace('.', '_')
            ));
            result.append(mode == Mode.POJO ? "Dto" : "Repository");
            return result.toString();
        } else {
            javaClassName = nameTools.depluralize(super.getJavaClassName(definition, mode));
        }
        return nameTools.depluralize(javaClassName);
    }
}
