package com.pop.fjournal.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.pop.fjournal.domain.Importer} entity.
 */
public class ImporterDTO implements Serializable {
    
    private Long id;

    @Lob
    private byte[] file;

    private String fileContentType;
    @NotNull
    private Instant importDate;

    private String separator;


    private Long ownerId;

    private String ownerLogin;

    private Long uploaderId;

    private String uploaderLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Instant getImportDate() {
        return importDate;
    }

    public void setImportDate(Instant importDate) {
        this.importDate = importDate;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long userId) {
        this.uploaderId = userId;
    }

    public String getUploaderLogin() {
        return uploaderLogin;
    }

    public void setUploaderLogin(String userLogin) {
        this.uploaderLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImporterDTO)) {
            return false;
        }

        return id != null && id.equals(((ImporterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImporterDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", importDate='" + getImportDate() + "'" +
            ", separator='" + getSeparator() + "'" +
            ", ownerId=" + getOwnerId() +
            ", ownerLogin='" + getOwnerLogin() + "'" +
            ", uploaderId=" + getUploaderId() +
            ", uploaderLogin='" + getUploaderLogin() + "'" +
            "}";
    }
}
