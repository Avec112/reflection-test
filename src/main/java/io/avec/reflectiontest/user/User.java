package io.avec.reflectiontest.user;

import io.avec.reflectiontest.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by avec112 on 12.11.19.
 */

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private UUID uuid;
    @Column(unique = true)
    private String username;
    private Classification securityClearence;

    public User(String username, Classification securityClearence) {
        this.username = username;
        this.securityClearence = securityClearence;
    }
}
