package com.example.camel.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class CSVToYamlProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("csv-Processor complete : ${body}");
        List<List<String>> csvRecordList = (List<List<String>>) exchange.getIn().getBody();
        String yamlString = convertToYaml(csvRecordList);
        exchange.getIn().setBody(yamlString);
    }

    public static String convertToYaml(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(obj);
        System.out.println("yaml : " +yaml);
        return yaml;
    }
}
