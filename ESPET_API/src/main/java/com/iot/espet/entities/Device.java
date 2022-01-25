package com.iot.espet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "device", schema = "espet", catalog = "")
public class Device implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id_device;

  @Basic
  @NotNull
  @Pattern(regexp = "^([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2})$")
  @Column(name = "mac", nullable = false)
  private String mac;

  @Basic
  @Column(name = "last_espet")
  private Integer last_espet;

  @Basic
  @Column(name = "login_token")
  private String login_token;

  @Basic
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "token_date")
  private Date token_date;

  @ManyToOne(cascade = CascadeType.MERGE)
  @NotNull
  @JsonBackReference
  @JoinColumn(name = "id_user", referencedColumnName = "id_user")
  private User user;


  // Getters, setters
  public Integer getId_device() { return id_device; }
  public void setId_device(Integer id_device) { this.id_device = id_device; }

  public String getMac() { return mac; }
  public void setMac(String mac) { this.mac = mac; }

  public Integer getLast_espet() { return last_espet; }
  public void setLast_espet(Integer last_espet) { this.last_espet = last_espet; }

  public String getLogin_token() { return login_token; }
  public void setLogin_token(String login_token) { this.login_token = login_token; }

  public Date getToken_date() { return token_date; }
  public void setToken_date(Date token_date) { this.token_date = token_date; }

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
}

