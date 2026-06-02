package com.gscience.security.entities.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "typeOfAccess",schema = "security_schema")
public class TypeOfAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;


    @CreatedDate
    @Column(name = "creates_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = true, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy; // Tracks who soft-deleted or edited the record



    @Builder.Default
    @Version
    @Column(name = "record_version")
    private long version = 0l;


    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeOfAccessEntity)) return false;
        TypeOfAccessEntity typeOfAccessEntity = (TypeOfAccessEntity) o;
        return Objects.equals(this.id, typeOfAccessEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion


    /**
     * this will print the class fields with newline for each field
     *
     * @return
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .toString();
    }

}
