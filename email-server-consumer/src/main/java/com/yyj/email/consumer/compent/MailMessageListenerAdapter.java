package com.yyj.email.consumer.compent;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.yyj.email.consumer.model.MailMessageModel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @Description TODO
 * @Author becktte
 * @Date 2019/4/23
 * @Version 1.0
 **/
@Component
public class MailMessageListenerAdapter extends MessageListenerAdapter {
    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.username}")
    private String mailUsername;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(message.getMessageProperties().getConsumerQueue());
        try {
            // 解析RabbitMQ消息体
            String messageBody = new String(message.getBody());
            MailMessageModel mailMessageModel = JSONObject.toJavaObject(JSONObject.parseObject(messageBody), MailMessageModel.class);
            // 发送邮件
            String to =  mailMessageModel.getTo();
            String subject = mailMessageModel.getSubject();
            String text = mailMessageModel.getText();
            sendHtmlMail(to, subject, text);
            System.out.println("send mail success");
            // 手动ACK
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
        }
    }

    /**
     * 发送邮件
     * @param to
     * @param subject
     * @param text
     * @throws Exception
     */
    private void sendHtmlMail(String to, String subject, String text) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(mailUsername);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        // 发送邮件
        mailSender.send(mimeMessage);
    }
}
