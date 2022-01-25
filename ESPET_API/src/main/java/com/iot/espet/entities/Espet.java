package com.iot.espet.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iot.espet.util.ValidRegister;
import com.iot.espet.util.ValidSave;

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
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "espet", schema = "espet", catalog = "")
public class Espet implements Serializable {

  @Id
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id_pet;

  @Basic
  @NotNull(groups = {ValidSave.class, ValidRegister.class})
  @Size(min = 3, max = 10)
  @Column(name = "name", nullable = false)
  private String name;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "happiness", nullable = false)
  private Double happiness;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "hunger", nullable = false)
  private Double hunger;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "fatigue", nullable = false)
  private Double fatigue;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "age", nullable = false)
  private Double age;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "health", nullable = false)
  private Double health;

  @Basic
  @Null(groups = ValidRegister.class)
  @NotNull(groups = ValidSave.class)
  @Column(name = "type", nullable = false)
  private String type;

  @ManyToOne(cascade = CascadeType.MERGE)
  @NotNull(groups = ValidRegister.class)
  @Null(groups = ValidSave.class)
  @JsonBackReference
  @JoinColumn(name = "id_user", referencedColumnName = "id_user")
  private User user;


  // Getters, setters
  public Integer getId_pet() { return id_pet; }
  public void setId_pet(Integer id_pet) { this.id_pet = id_pet; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Double getHappiness() { return happiness; }
  public void setHappiness(Double happiness) { this.happiness = happiness; }

  public Double getHunger() { return hunger; }
  public void setHunger(Double hunger) { this.hunger = hunger; }

  public Double getFatigue() { return fatigue; }
  public void setFatigue(Double fatigue) { this.fatigue = fatigue; }

  public Double getAge() { return age; }
  public void setAge(Double age) { this.age = age; }

  public Double getHealth() { return health; }
  public void setHealth(Double health) { this.health = health; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
}

