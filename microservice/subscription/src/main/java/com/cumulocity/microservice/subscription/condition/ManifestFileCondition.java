package com.cumulocity.microservice.subscription.condition;

import com.cumulocity.microservice.properties.ConfigurationFileProvider;
import com.google.common.collect.Iterables;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.nio.file.Path;

public class ManifestFileCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurationFileProvider provider = new ConfigurationFileProvider(context.getEnvironment());
        final Iterable<Path> manifests = provider.find(new String[]{"cumulocity"}, ".json");
        if (!Iterables.isEmpty(manifests)) {
            return ConditionOutcome.match(ConditionMessage.forCondition("Manifest File").available("cumulocity.json"));
        }
        return ConditionOutcome.noMatch(ConditionMessage.forCondition("Manifest File").notAvailable("cumulocity.json"));
    }

}
