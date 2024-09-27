package dev.kayange.Multivendor.E.commerce.constants;

import java.util.List;

public final class SystemRoles {
    public static final List<String> SYSTEM_SUPER_ADMIN = List.of("SUPER_ADMIN", "ADMIN", "MANAGER", "VENDOR", "CUSTOMER");
    public static final List<String> SYSTEM_ADMIN = List.of("ADMIN", "MANAGER", "VENDOR", "CUSTOMER");
    public static final List<String> SYSTEM_MANAGER = List.of( "MANAGER", "VENDOR", "CUSTOMER");
    public static final List<String> SYSTEM_VENDOR = List.of( "VENDOR", "CUSTOMER");
    public static final List<String> SYSTEM_USER = List.of( "CUSTOMER");
    public static final List<String> SYSTEM_VISITOR = List.of( );

}
