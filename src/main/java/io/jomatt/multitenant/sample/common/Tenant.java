package io.jomatt.multitenant.sample.common;

import io.jomatt.multitenant.sample.util.UrlUtils;
import io.jomatt.multitenant.sample.config.db.CurrentTenantResolver;
import io.quantics.multitenant.tenantdetails.TenantDetails;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "tenant", schema = CurrentTenantResolver.DEFAULT_SCHEMA)
public class Tenant implements TenantDetails {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "schema", nullable = false, unique = true)
    private String schema;

    @NotNull
    @Column(name = "issuer", nullable = false, unique = true)
    private String issuer;

    @Override
    public String getJwkSetUrl() {
        return this.issuer + "/protocol/openid-connect/certs";
    }

}
