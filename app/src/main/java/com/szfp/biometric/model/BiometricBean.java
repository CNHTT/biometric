package com.szfp.biometric.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 戴尔 on 2018/1/30.
 */

@Entity
public class BiometricBean implements Serializable {

    static final long serialVersionUID = 42L;
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String fingerId;
    private byte[] model;
    @Generated(hash = 1319728172)
    public BiometricBean(Long id, String name, String fingerId, byte[] model) {
        this.id = id;
        this.name = name;
        this.fingerId = fingerId;
        this.model = model;
    }
    @Generated(hash = 1237947042)
    public BiometricBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFingerId() {
        return this.fingerId;
    }
    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }
    public byte[] getModel() {
        return this.model;
    }
    public void setModel(byte[] model) {
        this.model = model;
    }
}
