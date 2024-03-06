package com.desfio.transactionservice.producer;


import com.desfio.transactionservice.dto.NotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Value("${notification.topic}")
    private String notificationTopic;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(NotificationDTO notificationDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(notificationDTO);
        kafkaTemplate.send(notificationTopic, message);
    }
}
