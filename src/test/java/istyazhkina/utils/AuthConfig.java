package istyazhkina.utils;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:AuthConfig.properties")
public interface AuthConfig extends Config {
    String username();
    String password();
}
