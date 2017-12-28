package modules;

import com.typesafe.config.Config;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ConfigProvider implements Provider<Config> {

    private final Configuration configuration;

    @Inject
    public ConfigProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Config get() {
        return this.configuration.underlying();
    }

}