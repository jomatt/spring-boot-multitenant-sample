package io.jomatt.multitenant.sample.common.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "usr")
public class User {

    @Id
    @NotNull
    @Column(name = "name", updatable = false, nullable = false, unique = true)
    private String name;

}
