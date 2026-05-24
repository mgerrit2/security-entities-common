package com.gscience.security.entities.common.security;


import com.gscience.security.entities.common.humanresources_schema.UserEntity;
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
@Table(name = "roles",schema = "security_schema")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(name = "default_role")
    private Boolean defaultRole;

    @Builder.Default
    @Version
    @Column(name = "record_version")
    private Long version = 0l;

    //region Hash and equals code
    @Override
     public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        RolesEntity rolesEntity = (RolesEntity) o;
        return Objects.equals(this.id, rolesEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

}
