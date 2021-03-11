package com.rebirth.simplepost.configuration;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class MapperConfiguration {

    @Bean
    public Mapper dozerMapper() throws IOException {
        List<String> xmlList = new ArrayList<>();
        xmlList.add("dozer/mapper.xml");
        log.info(xmlList.toString());

        return DozerBeanMapperBuilder.create()
                .withMappingFiles(xmlList)
                .build();
    }


}
