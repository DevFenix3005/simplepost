package com.rebirth.simplepost;

import com.rebirth.simplepost.services.GreetingService;
import com.rebirth.simplepost.services.dtos.Greeting;
import com.rebirth.simplepost.web.controllers.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GreetingController.class)
public class HttpRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService service;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        Mockito.when(service.sayHello("Haskell")).thenReturn(new Greeting(1L, "hello Haskell!"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/greeting/Haskell"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"id\":1,\"greet\":\"hello Haskell!\"}")));
    }

}
