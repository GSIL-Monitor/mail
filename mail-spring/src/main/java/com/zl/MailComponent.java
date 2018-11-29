package com.zl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author zhuolin
 * @program: mail
 * @date 2018/11/29
 * @description: ${description}
 **/
@Component
public class MailComponent {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 发送简单文本文件
     *
     * @param from    发件人
     * @param mailTo  收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail (String from, String mailTo, String subject, String content) {
        String[] to = mailTo.split(",");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    /**
     * 发送html格式邮件
     *
     * @param from
     * @param mailTo
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail (String from, String mailTo, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String[] to = mailTo.split(",");
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    /**
     * 含有附件邮件
     *
     * @param from      发件人邮箱
     * @param mailTo    收件人
     * @param subject   主题
     * @param content   内容
     * @param filePaths 文件路径
     * @throws MessagingException
     */
    public void sendAttachmentsMail (String from, String mailTo, String subject, String content, String... filePaths) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String[] to = mailTo.split(",");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);
        }

        helper.setText(content);
        javaMailSender.send(message);
    }

    /**
     * 发送带静态资源的邮件
     *
     * @param from    发件人邮箱
     * @param mailTo  收件人
     * @param subject 主题
     * @param content 内容
     * @param rscPath 静态文件路径
     * @param rscId
     * @throws MessagingException
     */
    public void sendInlineResourceMail (String from, String mailTo, String subject, String content, String rscPath, String rscId) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String[] to = mailTo.split(",");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);
        javaMailSender.send(message);
    }

    /**
     * 模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 根据模板发送邮件
     *
     * @param from         发件人邮箱
     * @param mailTo       收件人
     * @param subject      主题
     * @param templateName 模板名称
     */
    public void sendTemplateMail (String from, String mailTo, String subject, String templateName, Map<String, String> dataMap) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        String[] to = mailTo.split(",");
        // 获取模板内容
        Context context = new Context();
        context.setVariables(dataMap);
        String emailText = templateEngine.process(templateName, context);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailText, true);
        javaMailSender.send(message);
    }
}