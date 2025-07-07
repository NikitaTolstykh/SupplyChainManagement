package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailDto {
    private String to;
    private String subject;
    private String templateName;
    private Object templateData;
}
