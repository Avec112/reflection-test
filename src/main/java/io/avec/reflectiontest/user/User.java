package io.avec.reflectiontest.user;

import io.avec.reflectiontest.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by avec112 on 12.11.19.
 */

@Data
@AllArgsConstructor
public class User {
    private String username;
    private Classification securityClearence;

}
