package com.example.camel.process;

import com.example.camel.model.Conversion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class XMLToYamlProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        //Conversion currencyExchangeDto = exchange.getIn().getBody(Conversion.class);
        System.out.println("xml-Processor complete : ${body}");
        HashMap<String, Object> xmlRecordMap = (HashMap<String, Object>) exchange.getIn().getBody();
        String yamlString = convertToYaml(xmlRecordMap);
        exchange.getIn().setBody(xmlRecordMap);
    }

    public static String convertToYaml(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(obj);
        System.out.println("yaml : " +yaml);
        return yaml;
    }
}
