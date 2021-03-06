package com.vpnbeast.vpnbeastservice.persistent.entity;

import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated( EnumType.STRING)
    @Column(name = "name")
    private RoleType name;

}