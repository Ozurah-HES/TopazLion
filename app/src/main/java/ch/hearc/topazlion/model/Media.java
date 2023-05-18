package ch.hearc.topazlion.model;

import java.util.Objects;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * <p>A media, like "One Piece", "Naruto", ...</p>
 * <p>It is linked to a type and a category</p>
 */
public class Media {
    private Long id;

    @Size(min = 1, max = 255, message = "Le nom du média ne doit pas dépasser 255 caractères")
    @NotBlank(message = "Le nom du média ne doit pas être vide")
    private String name;

    @PositiveOrZero(message = "Le nombre de parution doit être positif")
    @NotNull(message = "Le dernier vu ne doit pas être vide")  
    @NumberFormat(style= Style.NUMBER) 
    private int nbPublished;

    private String imgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getNbPublished() {
        return nbPublished;
    }

    public void setNbPublished(int nbPublished) {
        this.nbPublished = nbPublished;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        return Objects.equals(id, ((Media) obj).id);
    }
}
