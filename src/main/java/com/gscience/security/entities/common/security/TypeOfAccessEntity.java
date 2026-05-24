package com.gscience.security.entities.common.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

}
