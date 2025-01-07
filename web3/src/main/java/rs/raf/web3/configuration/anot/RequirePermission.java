package rs.raf.web3.configuration.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD) // Can only be used on methods
@Retention(RetentionPolicy.RUNTIME) // Retain this annotation at runtime
public @interface RequirePermission {
    String value(); // Permission type (e.g., "create", "delete", etc.)
}

