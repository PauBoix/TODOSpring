package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity(name="usergroup")
public class Group implements Serializable {
    public Group() {
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setOwner(User u){
        this.owner = u;
    }

    public void addMember(User user) { members.add(user); }

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Private.class)
    public User getOwner() {
        return owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

   @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

   @ManyToMany(cascade = CascadeType.ALL)
    private Collection<User> members;
}
