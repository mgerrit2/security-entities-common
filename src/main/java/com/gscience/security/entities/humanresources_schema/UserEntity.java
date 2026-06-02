package com.gscience.security.entities.humanresources_schema;

import com.gscience.security.entities.security.JWTEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users",schema = "humanresources_schema")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cdsid",length = 8)
    private String cdsid;

    @Column(name = "hash_password")
    private String hashPassword;

    //region TOTP implementation
    // De definitieve geheime sleutel voor TOTP
    @Column(name = "secret")
    private String secret;

    // Een tijdelijke sleutel die wordt gebruikt tijdens het scan-proces (setup)
    @Column(name = "temp_secret")
    private String tempSecret;

    // Indicator of de gebruiker 2FA daadwerkelijk heeft
    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;
    //endregion

    /**
     * <pre>
     * What it contains: A long, unique string starting with S-1-5-21-....
     *
     * Purpose: This is the true, immutable passport of a Windows user. Even if a system administrator changes the username from gerri to m.gerrits, the SID remains exactly the same.
     * In enterprise environments,
     * this is the most reliable way to uniquely track and bind a Windows identity.
     * <pre>
     */
    @Column(name = "\"userSid\"") // Quotes protect the mixed casing in PostgreSQL
    private String userSid;

    @Builder.Default
    @Column(name = "active")
    private boolean active = true;

    @CreatedDate
    @Column(name = "creates_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy; // Tracks who soft-deleted or edited the record

    //@Setter(AccessLevel.PROTECTED)
    @Version
    @Column(name = "record_version")
    private Long version;

    //region getters
    public Optional<String> getSecret(){return Optional.ofNullable(this.secret);}


    public Optional<String> getGetCdsid(){
        return Optional.ofNullable(this.cdsid);
    }

    public Optional<OffsetDateTime> getCreatedAt() {
        // Since 'creates_at' is 'nullable = false' and managed by @CreatedDate,
        // it should always have a value after persistence.
        return Optional.ofNullable(this.createdAt);
    }

    public Optional<String> getCreatedBy() {
        // Since 'created_by' is 'nullable = false' and managed by @CreatedBy,
        // it should always have a value after persistence.
        return Optional.ofNullable(this.createdBy);
    }

    public Optional<OffsetDateTime> getUpdatedAt() {
        // 'updated_at' is 'nullable = false', so it should always have a value.
        return Optional.ofNullable(this.updatedAt);
    }

    //endregion

    //region mapping
    // De fysieke koppeling naar de tabel 'persons'
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", unique = true)
    private PersonsEntity person;

    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private JWTEntity jwtConfiguration;

    @Builder.Default
    @OneToMany(
            mappedBy = "user",           // Points to the 'user' field in UserRolesEntity
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<UserRolesEntity> roles = new ArrayList<>();


    //region mapping functions
    public boolean hasRole(String roleName) {
       return roles != null && roles.stream()
                .anyMatch(r -> r.getRole().equalsIgnoreCase(roleName));
    }

    // Update your helper method to set BOTH sides
    public void addRole(UserRolesEntity role) {
        roles.add(role);
        role.setUser(this); // CRITICAL: This sets the foreign key for the INSERT
    }
    //endregion

    //endregion

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
        if (!(o instanceof UserEntity)) return false;
        UserEntity userEntity = (UserEntity) o;
        return Objects.equals(this.id, userEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion




}
