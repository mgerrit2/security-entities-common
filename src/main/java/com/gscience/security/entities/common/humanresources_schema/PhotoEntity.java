package com.gscience.security.entities.common.humanresources_schema;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "photos", schema = "humanresources_schema")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type")
    private String contentType; // bijv. "image/jpeg"


    /**
     * this annotation are for postgresql
     */
    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image_data")
    private byte[] imageData;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private OffsetDateTime uploadedAt;

    @Column(name = "active")
    private Boolean active;

    //region optional getters
    public Optional<Long> getId(){
        return Optional.ofNullable(this.id);
    }

    public Optional<String> getFilename(){
        return Optional.ofNullable(this.fileName);
    }

    public Optional<String> getContentTyp(){
        return Optional.ofNullable(this.contentType);
    }

    public Optional<byte[]> getImageData(){
        return Optional.ofNullable(this.imageData);
    }

    public Optional<OffsetDateTime> getUploadedAt(){
        return Optional.ofNullable(this.uploadedAt);
    }

    public Optional<Boolean> getActive(){
        return Optional.ofNullable(this.active);
    }

    public Optional<PersonsEntity> getPerson(){
        return Optional.ofNullable(this.person);
    }
    //endregion



    //region mapping

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonsEntity person;

    //endregion

    @Builder.Default
    @Version
    @Column(name = "record_version")
    private long version = 0l;


    /**
     * this will print the class fields with newline for each field
     *
     * @return
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .setExcludeFieldNames("person")
                .toString();
    }

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoEntity)) return false;
        PhotoEntity photoEntity = (PhotoEntity) o;
        return Objects.equals(this.id, photoEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

}
