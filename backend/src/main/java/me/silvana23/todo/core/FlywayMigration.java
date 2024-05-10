package me.silvana23.todo.core;

import io.quarkus.flyway.runtime.FlywayBuildTimeConfig;
import io.quarkus.flyway.runtime.FlywayRuntimeConfig;
import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;

@Startup
public class FlywayMigration {
    public FlywayMigration(
            SessionFactory sessionFactory,
            FlywayRuntimeConfig flywayRuntimeConfig,
            FlywayBuildTimeConfig flywayBuildTimeConfig,
            @ConfigProperty(name = "quarkus.datasource.reactive.url") String datasourceUrl,
            @ConfigProperty(name = "quarkus.datasource.username") String datasourceUsername,
            @ConfigProperty(name = "quarkus.datasource.password") String datasourcePassword
    ) {
        System.out.println(flywayBuildTimeConfig.defaultDataSource.locations.toString());

        Flyway flyway = Flyway.configure()
                .dataSource(
                        datasourceUrl.replace("vertx-reactive:", "jdbc:"),
                        datasourceUsername,
                        datasourcePassword
                )
                .defaultSchema(flywayRuntimeConfig.defaultDataSource.defaultSchema.orElse("public"))
                .load();

        if (flywayRuntimeConfig.defaultDataSource.repairAtStart) {
            flyway.repair();
        }

        if (flywayRuntimeConfig.defaultDataSource.migrateAtStart) {
            flyway.migrate();
        }
    }
}
