package com.example.camel.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CSVProcessor implements Processor {
    ObjectMapper objectMapper =new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("In CXVProcessor body :" +exchange.getIn().getBody());
        List<List<String>> csvRecordList = (List<List<String>>) exchange.getIn().getBody();
        exchange.getIn().setBody(csvRecordList);
    }

}
