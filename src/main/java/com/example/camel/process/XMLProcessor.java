package com.example.camel.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class XMLProcessor implements Processor {
    ObjectMapper objectMapper =new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("In XMLProcessor body :" +exchange.getIn().getBody());
        HashMap<String, Object> xmlRecordMap = (HashMap<String, Object>) exchange.getIn().getBody();
        exchange.getIn().setBody(xmlRecordMap);
    }

}
