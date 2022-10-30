package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role_menu_access")
public class RoleMenuAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_allow")
    private int isAllow;

    @Column(name = "is_read")
    private int isRead;

    @Column(name = "is_write")
    private int isWrite;

    @Column(name = "is_delete")
    private int isDelete;

    @ManyToOne
    @NotNull
    private MenuItem menuItem;

    @ManyToOne
    private UserRole userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(int isAllow) {
        this.isAllow = isAllow;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(int isWrite) {
        this.isWrite = isWrite;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleMenuAccess)) {
            return false;
        }
        return id != null && id.equals(((RoleMenuAccess) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
