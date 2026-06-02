package com.gscience.security.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gscience.security.entities.humanresources_schema.UserEntity;
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
import java.util.Optional;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "jwt",schema = "security_schema")
public class JWTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * <pre>
     * The cryptographic string used to sign the JWT.
     * This ensures that the token cannot be tampered with by a client after it is issued.
     * </pre>
     */
    @Column(name = "secret_key")
    private String secretKey;

    /**
     * <pre>
     * The lifespan of the token in milliseconds. Once this duration passes,
     * the token is rejected by the system, forcing the user to re-authenticate or use a refresh token.
     * </pre>
     */
    @Column(name = "expiration")
    private Long expiration;

    /**
     * <pre>
     * Identifies the specific recipients that the JWT is intended for (e.g.,
     * the API URL or Client ID of your resource server).
     * If a service receives a token where the aud does not match its own identifier, it must reject it.
     * </pre>
     */
    @Column(name = "audience")
    private String audience;

    /**
     * <pre>
     * A hint placed in the JWT header that tells the validating application
     * which specific public/private key pair was used to sign the token.
     * This is crucial for environments that rotate signing keys regularly.
     * </pre>
     */
    @Column(name = "key_id")
    private String keyIid;

    @Column(name = "authorized_party")
    private String authorizedParty;

    @Column(name = "azpacr_value")
    private String azpacrValue;

    @Column(name = "request_history")
    private String requestHistory;


    /**
     * <pre>
     * Crucial for multi-tenant architectures. It represents the specific corporate directory,
     * sub-organization, or database partition to which the authenticating user belongs.
     * </pre>
     */
    @Column(name = "tenant_id")
    private String tenantId;

    /**
     * <pre>
     * Tracks the layout version of the token payload. If your token structure changes in a future deployment,
     * your backend can use this to parse older vs. newer tokens correctly.
     * </pre>
     */
    @Column(name = "token_version")
    private String tokenVersion;

    /**
     * <pre>
     * An internal or extended claim (often prefixed with xms_ by Microsoft)
     * typically used to convey specific deployment features,
     * conditional access policies, or routing hints.
     * </pre>
     */
    @Column(name = "xms_ftd")
    private String xmsFtd;

    @Builder.Default
    @Column(name = "active")
    private boolean active = true;


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


    //region optional getters
    public Optional<String> getSecretKey(){
        return Optional.ofNullable(this.secretKey);
    }
    public Optional<Long> getExpiration(){
        return Optional.ofNullable(this.expiration);
    }
    public Optional<String> getAudience(){
        return Optional.ofNullable(this.audience);
    }
    public Optional<String> getKeyIid(){
        return Optional.ofNullable(this.keyIid);
    }
    public Optional<String> getAuthorizedParty(){
        return Optional.ofNullable(this.authorizedParty);
    }
    public Optional<String> getTenantId(){
        return Optional.ofNullable(this.tenantId);
    }
    public Optional<String> getTokenVersion(){
        return Optional.ofNullable(this.tokenVersion);
    }
    public Optional<String> getXmsFtd(){
        return Optional.ofNullable(this.xmsFtd);
    }
    public Optional<Boolean> getActive(){
        return Optional.of(this.active);
    }
    public Optional<OffsetDateTime> getCreatedAt(){
        return Optional.of(this.createdAt);
    }
    public Optional<String> getCreatedBy(){
        return Optional.of(this.createdBy);
    }
    public Optional<OffsetDateTime> getUpdatedAt(){
        return Optional.of(this.updatedAt);
    }
    public Optional<String> getUpdatedBy(){
        return Optional.of(this.lastModifiedBy);
    }
    //endregion

    @Builder.Default
    @Version
    @Column(name = "record_version")
    private Long version = 0L;

    // Add this field inside JWTEntity.java

    //region Description
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id") // Komt overeen met de primary key kolom van de 'jwt' tabel
    @lombok.Getter
    @lombok.Setter
    @JsonIgnore
    private UserEntity user;
    //endregion

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JWTEntity jWTEntity)) return false;
        return Objects.equals(this.id, jWTEntity.id);
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
                .setExcludeFieldNames("user")
                .toString();
    }

}
