package com.ezwell.backend.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KopisEventDto {

    @JacksonXmlProperty(localName = "mt20id")
    private String eventId;

    @JacksonXmlProperty(localName = "prfnm")
    private String eventName;

    @JacksonXmlProperty(localName = "prfpdfrom")
    private String startDate;

    @JacksonXmlProperty(localName = "prfpdto")
    private String endDate;

    @JacksonXmlProperty(localName = "fcltynm")
    private String facilityName;

    @JacksonXmlProperty(localName = "poster")
    private String posterUrl;

    @JacksonXmlProperty(localName = "genrenm")
    private String genre;

    @JacksonXmlProperty(localName = "prfstate")
    private String state;
}