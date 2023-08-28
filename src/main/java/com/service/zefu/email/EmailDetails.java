package com.service.zefu.email;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailDetails {

    private String recipient;
    private String body;
    private String subject;
    private String attachment;
}
