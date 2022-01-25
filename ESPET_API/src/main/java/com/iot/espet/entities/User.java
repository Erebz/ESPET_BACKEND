package com.iot.espet.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user", schema = "espet", catalog = "")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id_user;

  @Basic
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Basic
  @NotNull
  @Column(name = "surname", nullable = false)
  private String surname;

  @Basic
  @NotNull
  @Temporal(TemporalType.DATE)
  @Column(name = "birthday", nullable = false)
  private Date birthday;

  @Basic
  @NotNull
  @Column(name = "country", nullable = false)
  private String country;

  @Basic
  @NotNull
  @Column(name = "genre", nullable = false)
  private char genre;

  @Basic
  @NotNull
  @Column(name = "mail", nullable = false)
  private String mail;

  @Basic
  @NotNull
  @Size(min = 8, max = 20)
  @Column(name = "password", nullable = false)
  private String password;

  @Basic
  @NotNull
  @Column(name = "address", nullable = false)
  private String address;

  @Basic
  @Column(name = "login_token")
  private String login_token;

  @Basic
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "token_date")
  private Date token_date;

  @OneToMany(targetEntity=Espet.class, mappedBy="user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Espet> espets = new ArrayList<Espet>();

  @OneToMany(targetEntity=Device.class, mappedBy="user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Device> devices = new ArrayList<Device>();


  // Getters, setters
  public Integer getId_user() { return id_user; }
  public void setId_user(Integer id_user) { this.id_user = id_user; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getSurname() { return surname; }
  public void setSurname(String surname) { this.surname = surname; }

  public Date getBirthday() { return birthday; }
  public void setBirthday(Date birthday) { this.birthday = birthday; }

  public String getCountry() { return country; }
  public void setCountry(String country) { this.country = country; }

  public char getGenre() { return genre; }
  public void setGenre(char genre) { this.genre = genre; }

  public String getMail() { return mail; }
  public void setMail(String mail) { this.mail = mail; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getLogin_token() { return login_token; }
  public void setLogin_token(String login_token) { this.login_token = login_token; }

  public Date getToken_date() { return token_date; }
  public void setToken_date(Date token_date) { this.token_date = token_date; }

  public List<Espet> getEspets() { return espets; }
  public void setEspets(List<Espet> espets) { this.espets = espets; }

  public List<Device> getDevices() { return devices; }
  public void setDevices(List<Device> devices) { this.devices = devices; }
}

