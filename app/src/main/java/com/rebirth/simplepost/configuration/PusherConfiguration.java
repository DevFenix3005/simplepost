package com.rebirth.simplepost.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pusher.rest.Pusher;
import com.rebirth.simplepost.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class PusherConfiguration {

    @Bean
    public Pusher pusher() {
        /*
         *    app_id = "244788"
         *    key = "40e1db2f5dcb09daa268"
         *    secret = "70e227401368da98094e"
         *    cluster = "mt1"
         * */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.dateTimeFormat);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter out, LocalDateTime value) throws IOException {
                        if (value == null) {
                            out.nullValue();
                        } else {
                            out.value(dateTimeFormatter.format(value));
                        }
                    }
                    @Override
                    public LocalDateTime read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        } else {
                            return LocalDateTime.parse(in.nextString(), dateTimeFormatter);
                        }
                    }
                })
                .create();

        log.info("FUE CREADO EL PUSHER!!");
        Pusher pusher = new Pusher("244788", "40e1db2f5dcb09daa268", "70e227401368da98094e");
        pusher.setCluster("mt1");
        pusher.setEncrypted(true);
        pusher.setGsonSerialiser(gson);
        return pusher;
    }


}
