package com.logman.producer.log.app.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface LogDataOutput {
    String OUTPUT = "log-data";

    @Output(OUTPUT)
    MessageChannel output();

}
