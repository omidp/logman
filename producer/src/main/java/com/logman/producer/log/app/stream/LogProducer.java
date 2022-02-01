package com.logman.producer.log.app.stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.logman.producer.log.model.Log;

import reactor.core.publisher.Mono;

@Component
@EnableBinding(LogDataOutput.class)
public class LogProducer {

    private static final Logger LOG = LoggerFactory.getLogger(LogProducer.class);

    private final MessageChannel messageChannel;

    public LogProducer(LogDataOutput dataOutput) {
        this.messageChannel = dataOutput.output();
    }

    public Mono<Void> sendEvent(Log event) {
        return Mono.fromSupplier(() -> buildMessage(event))
                .doFirst(() -> LOG.debug("==== Sending message {}", event))
                .map(messageChannel::send)
                .doOnSuccess(sent -> LOG.debug("==== Event sent? {}", sent))
                .then();
    }

    protected Message<Log> buildMessage(Log event) {
        return MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.MESSAGE_KEY, event.getId())
                .build();
    }
}
