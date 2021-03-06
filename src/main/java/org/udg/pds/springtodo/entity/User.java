package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User implements Serializable {
  /**
   * Default value included to remove warning. Remove or modify at will. *
   */
  private static final long serialVersionUID = 1L;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.tasks = new ArrayList<>();
    this.owner_groups = new ArrayList<>();
    this.member_groups = new ArrayList<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  private String password;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Collection<Task> tasks;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private Collection<Group> owner_groups;

  @ManyToMany(cascade = CascadeType.ALL)
  private Collection<Group> member_groups;

  @JsonView(Views.Private.class)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonView(Views.Private.class)
  public String getEmail() {
    return email;
  }

  @JsonView(Views.Public.class)
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonView(Views.Complete.class)
  public Collection<Task> getTasks() {
    // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
    // that you have to query the object (calling size(), for example) to get the list initialized
    // More: http://www.javabeat.net/jpa-lazy-eager-loading/
    tasks.size();
    return tasks;
  }

    @JsonView(Views.Complete.class)
    public Collection<Group> getGroups() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        owner_groups.size();
        return owner_groups;
    }

    @JsonView(Views.Complete.class)
    public Collection<Group> getMembership() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        member_groups.size();
        return member_groups;
    }

  public void addTask(Task task) {
    tasks.add(task);
  }
  public void addGroup(Group group) {owner_groups.add(group);}
  public void addMembership(Group group) {member_groups.add(group);}

}
