package com.webstore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "email_notification")
public class EmailNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;


  @Column(name = "reportstamp")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
  private Date reportStamp = new Date();

  @Column(name = "sendstamp")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss")
  private Date sendStamp;

  @Column(name = "email")
  private String email;
  @Column(name = "head")
  private String head;

  @Lob
  @Column(length=100000
          ,name = "text")
  private byte[] text;

  @Column(name = "issend")
  private String issend;

  @Column(name = "error")
  private String error;

  public EmailNotification(String email, String head, byte[] text) {
    this.email = email;
    this.head = head;
    this.text = text;
  }

  public EmailNotification() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public Date getReportstamp() {
    return reportStamp;
  }

  public void setReportstamp(Date reportstamp) {
    this.reportStamp = reportstamp;
  }


  public Date getSendstamp() {
    return sendStamp;
  }

  public void setSendstamp(Date sendstamp) {
    this.sendStamp = sendstamp;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getHead() {
    return head;
  }

  public void setHead(String head) {
    this.head = head;
  }

  public String getIssend() {
    return issend;
  }

  public void setIssend(String issend) {
    this.issend = issend;
  }


  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public byte[] getText() {
    return text;
  }

  public void setText(byte[] text) {
    this.text = text;
  }

}
