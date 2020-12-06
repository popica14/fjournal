package com.pop.fjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.pop.fjournal.domain.enumeration.MealType;

/**
 * A Meal.
 */
@Entity
@Table(name = "meal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Meal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "portion_size")
    private String portionSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MealType type;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "calories")
    private Long calories;

    @Column(name = "comment")
    private String comment;

    @Column(name = "recipe")
    private String recipe;

    @ManyToOne
    @JsonIgnoreProperties(value = "meals", allowSetters = true)
    private User myMeal;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Meal description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Meal quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getPortionSize() {
        return portionSize;
    }

    public Meal portionSize(String portionSize) {
        this.portionSize = portionSize;
        return this;
    }

    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    public MealType getType() {
        return type;
    }

    public Meal type(MealType type) {
        this.type = type;
        return this;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Meal date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Meal photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Meal photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Long getCalories() {
        return calories;
    }

    public Meal calories(Long calories) {
        this.calories = calories;
        return this;
    }

    public void setCalories(Long calories) {
        this.calories = calories;
    }

    public String getComment() {
        return comment;
    }

    public Meal comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRecipe() {
        return recipe;
    }

    public Meal recipe(String recipe) {
        this.recipe = recipe;
        return this;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public User getMyMeal() {
        return myMeal;
    }

    public Meal myMeal(User user) {
        this.myMeal = user;
        return this;
    }

    public void setMyMeal(User user) {
        this.myMeal = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meal)) {
            return false;
        }
        return id != null && id.equals(((Meal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meal{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", portionSize='" + getPortionSize() + "'" +
            ", type='" + getType() + "'" +
            ", date='" + getDate() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", calories=" + getCalories() +
            ", comment='" + getComment() + "'" +
            ", recipe='" + getRecipe() + "'" +
            "}";
    }
}
