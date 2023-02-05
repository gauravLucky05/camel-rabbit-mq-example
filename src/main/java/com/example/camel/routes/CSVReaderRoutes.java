package com.example.camel.routes;

import com.example.camel.model.Conversion;
import com.example.camel.process.JsonToYamlProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class CSVReaderRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("rabbitmq:amq.direct?queue=messageToCSVQueue&autoDelete=false&routingKey=messageToCSVQueue&hostname=localhost&portNumber=5672&username=guest&password=guest")
                .log("got this message from rabbit: ${body}")
                .unmarshal(new JacksonDataFormat(Conversion.class))
                .process(new JsonToYamlProcessor())
                .log("Filename -: ${header.name}")
                .log("Date -: ${date:now:yyyyMMddHHmmss}")
                .to("file:target/output/jsonToYaml?fileName=${header.name}_${date:now:yyyyMMddHHmmss}_jsonToYaml.yaml");
    }
}
