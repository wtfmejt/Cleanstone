package rocks.cleanstone.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import rocks.cleanstone.core.config.CleanstoneConfig;
import rocks.cleanstone.core.config.MinecraftConfig;

@SpringBootApplication
@Component("cleanstoneMainServer")
@Profile(value = "mainServer")
public class CleanstoneMainServer extends CleanstoneServer {

    @Autowired
    public CleanstoneMainServer(CleanstoneConfig cleanstoneConfig, MinecraftConfig minecraftConfig) {
        super(cleanstoneConfig, minecraftConfig);
    }
}
