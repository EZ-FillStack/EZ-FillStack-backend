package com.ezwell.backend.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KopisFacilityDto {

    @JacksonXmlProperty(localName = "adres")
    private String address;     // 시설 상세 주소

    @JacksonXmlProperty(localName = "la")
    private String latitude;    // 위도

    @JacksonXmlProperty(localName = "lo")
    private String longitude;   // 경도
}