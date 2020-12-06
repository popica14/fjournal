package com.pop.fjournal.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.pop.fjournal.domain.enumeration.MealType;

/**
 * A DTO for the {@link com.pop.fjournal.domain.Meal} entity.
 */
public class MealDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String description;

    private Long quantity;

    private String portionSize;

    @NotNull
    private MealType type;

    @NotNull
    private ZonedDateTime date;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private Long calories;

    private String comment;

    private String recipe;


    private Long myMealId;

    private String myMealLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
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

    public Long getCalories() {
        return calories;
    }

    public void setCalories(Long calories) {
        this.calories = calories;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public Long getMyMealId() {
        return myMealId;
    }

    public void setMyMealId(Long userId) {
        this.myMealId = userId;
    }

    public String getMyMealLogin() {
        return myMealLogin;
    }

    public void setMyMealLogin(String userLogin) {
        this.myMealLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealDTO)) {
            return false;
        }

        return id != null && id.equals(((MealDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", portionSize='" + getPortionSize() + "'" +
            ", type='" + getType() + "'" +
            ", date='" + getDate() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", calories=" + getCalories() +
            ", comment='" + getComment() + "'" +
            ", recipe='" + getRecipe() + "'" +
            ", myMealId=" + getMyMealId() +
            ", myMealLogin='" + getMyMealLogin() + "'" +
            "}";
    }
}
