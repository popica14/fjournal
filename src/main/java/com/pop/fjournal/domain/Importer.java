package com.pop.fjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Importer.
 */
@Entity
@Table(name = "importer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Importer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @NotNull
    @Column(name = "import_date", nullable = false)
    private Instant importDate;

    @Column(name = "jhi_separator")
    private String separator;

    @ManyToOne
    @JsonIgnoreProperties(value = "importers", allowSetters = true)
    private User owner;

    @ManyToOne
    @JsonIgnoreProperties(value = "importers", allowSetters = true)
    private User uploader;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public Importer file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public Importer fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Instant getImportDate() {
        return importDate;
    }

    public Importer importDate(Instant importDate) {
        this.importDate = importDate;
        return this;
    }

    public void setImportDate(Instant importDate) {
        this.importDate = importDate;
    }

    public String getSeparator() {
        return separator;
    }

    public Importer separator(String separator) {
        this.separator = separator;
        return this;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public User getOwner() {
        return owner;
    }

    public Importer owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getUploader() {
        return uploader;
    }

    public Importer uploader(User user) {
        this.uploader = user;
        return this;
    }

    public void setUploader(User user) {
        this.uploader = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Importer)) {
            return false;
        }
        return id != null && id.equals(((Importer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Importer{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", importDate='" + getImportDate() + "'" +
            ", separator='" + getSeparator() + "'" +
            "}";
    }
}
