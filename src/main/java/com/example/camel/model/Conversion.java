package com.example.camel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"conversion"})
@XmlRootElement(name="Conversion", namespace="http://global.aon.bz/schema/cbs/archive/errorresource/0")
public class Conversion implements Serializable {
      private static final long serialVersionUID = 5851038813219503043L;
      @XmlAttribute
      int usd;
      @XmlAttribute
      int inr;
}

