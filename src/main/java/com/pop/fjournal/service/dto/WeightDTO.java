package com.pop.fjournal.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.pop.fjournal.domain.Weight} entity.
 */
public class WeightDTO implements Serializable {
    
    private Long id;

    @NotNull
    @DecimalMin(value = "40")
    @DecimalMax(value = "150")
    private Float value;

    private ZonedDateTime date;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String observation;


    private Long myWeigthId;

    private String myWeigthLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Long getMyWeigthId() {
        return myWeigthId;
    }

    public void setMyWeigthId(Long userId) {
        this.myWeigthId = userId;
    }

    public String getMyWeigthLogin() {
        return myWeigthLogin;
    }

    public void setMyWeigthLogin(String userLogin) {
        this.myWeigthLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeightDTO)) {
            return false;
        }

        return id != null && id.equals(((WeightDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeightDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", date='" + getDate() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", observation='" + getObservation() + "'" +
            ", myWeigthId=" + getMyWeigthId() +
            ", myWeigthLogin='" + getMyWeigthLogin() + "'" +
            "}";
    }
}
