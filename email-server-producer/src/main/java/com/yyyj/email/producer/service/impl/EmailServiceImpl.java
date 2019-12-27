package com.yyyj.email.producer.service.impl;

import com.yyyj.email.producer.service.EmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @Description TODO
 * @Author becktte
 * @Date 2019/4/23
 * @Version 1.0
 **/
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.routekey}")
    private String routekey;

    @Value("${mq.exchange}")
    private String exchange;

    @Override
    public void sendEmail(String message) {
        try {
            rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String s) {
                    if (ack) {
                        System.out.println("确认已发送");
                    } else {

                    }
                }
            });
            rabbitTemplate.convertAndSend(exchange, routekey, message);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("EmailService exception:" + ExceptionUtils.getMessage(e));
        }
    }
}
