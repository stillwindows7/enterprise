package io.github.enterprise.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Sheldon on 2017/12/08
 */
@Entity
@Table(name = "local_auth")
public class LocalAuth {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    private String username;

    private String password;

    @Column(name = "createdOn")
    private LocalDate createdOn;

    @Column(name = "lastModified")
    private LocalDate lastModified;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "localAuth")
    private User user;

    public LocalAuth() {
    }

    public LocalAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    private void onCreate() {
        this.createdOn = LocalDate.now();
        this.lastModified = LocalDate.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.lastModified = LocalDate.now();
    }
}
