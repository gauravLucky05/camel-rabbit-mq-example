package com.example.camel.routes;

import com.example.camel.process.CSVProcessor;
import com.example.camel.process.JsonProcessor;
import com.example.camel.process.XMLProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.YAMLDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FileUploadRoute extends RouteBuilder {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TEXT_CSV_VALUE = "text/csv";

    @Override
    public void configure() throws Exception {

        YAMLDataFormat yaml = new YAMLDataFormat();

        restConfiguration().bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "REST API")
                .apiProperty("api.version", "v1")
                .apiContextRouteId("doc-api")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("mustBeJAXBElement", "false");

            rest("/fileUpload?name={name}").post().routeId("post-router")
                    .to("direct:fileDataProcess");

            from("direct:fileDataProcess")
                    .log("Data Details : Body : ${body} fileName  : ${header.name} ")
                    .choice()
                        .when(header(CONTENT_TYPE).isEqualTo(MediaType.APPLICATION_XML_VALUE))
                            .log("routing to direct:messageToXMLQueue")
                            .to("direct:messageToXMLQueue")
                        .when(header(CONTENT_TYPE).isEqualTo(MediaType.APPLICATION_JSON_VALUE))
                            .log("routing to direct:messageToJsonQueue")
                            .to("direct:messageToJsonQueue")
                        .when(header(CONTENT_TYPE).isEqualTo(TEXT_CSV_VALUE))
                            .log("routing to direct:messageToCSVQueue")
                            .to("direct:messageToCSVQueue")
                        .otherwise()
                            .log("routing to direct:error")
                            .to("direct:messageToDeadLetterQueue");

            from("direct:messageToJsonQueue").routeId("messageToJsonQueue")
                    .doTry()
                        .log("Trying to send : ${body}")
                        .setBody(body())
                        .process(new JsonProcessor())
                        .to("rabbitmq:amq.direct?autoDelete=false&declare=false" +
                                "&queue=messageToJsonQueue&routingKey=messageToJsonQueue" +
                                "&hostname=localhost&portNumber=5672&username=guest&password=guest")
                        .log("Message Sent!")
                    .doCatch(Exception.class)
                        .log("json-processing-error")
                        .to("direct:messageToDeadLetterQueue")
                    .end();

            from("direct:messageToXMLQueue").routeId("messageToXMLQueue")
                    .doTry()
                        .log("Trying to send : ${body}")
                        .unmarshal().jacksonXml()
                        .process(new XMLProcessor()).marshal(yaml)
                        .to("rabbitmq:amq.direct?autoDelete=false" +
                            "&declare=false&queue=messageToXMLQueue" +
                            "&routingKey=messageToXMLQueue&hostname=localhost" +
                            "&portNumber=5672&username=guest&password=guest")
                        .log("Message Sent!")
                    .doCatch(Exception.class)
                        .log("xml-processing-error")
                        .to("direct:messageToDeadLetterQueue")
                    .end();

            from("direct:messageToCSVQueue").routeId("messageToCSVQueue")
                    .doTry()
                        .log("Trying to send : ${body}")
                        .unmarshal().csv().process(new CSVProcessor()).marshal(yaml)
                        .to("rabbitmq:amq.direct?autoDelete=false" +
                            "&declare=false&queue=messageToCSVQueue" +
                            "&routingKey=messageToCSVQueue&hostname=localhost" +
                            "&portNumber=5672&username=guest&password=guest")
                    .log("Message Sent!")
                    .doCatch(Exception.class)
                        .log("xml-processing-error")
                        .to("direct:messageToDeadLetterQueue")
                    .end();

            from("direct:messageToDeadLetterQueue").routeId("messageToDeadLetterQueue")
                    .log("Trying to send : ${body}")
                    .setBody(body())
                    .to("rabbitmq:amq.direct?autoDelete=false" +
                            "&declare=false&queue=messageToDeadLetterQueue" +
                            "&routingKey=messageToDeadLetterQueue&hostname=localhost" +
                        "&portNumber=5672&username=guest&password=guest")
                    .log("Message Sent!")
                    .end();

        }
    }

