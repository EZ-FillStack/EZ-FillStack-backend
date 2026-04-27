package com.ezwell.backend.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KopisEventDetailDto {

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

    @JacksonXmlProperty(localName = "prfcast")
    private String cast;

    @JacksonXmlProperty(localName = "prfcrew")
    private String crew;

    @JacksonXmlProperty(localName = "prfruntime")
    private String runtime;

    @JacksonXmlProperty(localName = "prfage")
    private String ageLimit;

    @JacksonXmlProperty(localName = "entrpsnm")
    private String company;

    @JacksonXmlProperty(localName = "pcseguidance")
    private String price;

    @JacksonXmlProperty(localName = "poster")
    private String posterUrl;

    @JacksonXmlProperty(localName = "sty")
    private String story;

    @JacksonXmlProperty(localName = "genrenm")
    private String genre;

    @JacksonXmlProperty(localName = "prfstate")
    private String state;

    @JacksonXmlProperty(localName = "dtguidance")
    private String schedule;

    // 상세 이미지 목록
    @JacksonXmlElementWrapper(localName = "styurls")
    @JacksonXmlProperty(localName = "styurl")
    private List<String> detailImageUrls;
}