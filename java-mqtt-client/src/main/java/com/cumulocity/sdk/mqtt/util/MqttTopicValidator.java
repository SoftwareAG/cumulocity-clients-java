package com.cumulocity.sdk.mqtt.util;

import lombok.experimental.UtilityClass;

import static com.cumulocity.sdk.mqtt.model.MqttTopics.*;

@UtilityClass
public class MqttTopicValidator {

    public static boolean isTopicValidForPublish(String topic) {
        if (topic == null || topic.isEmpty()) {
            return false;
        }

        return isSmartRestStaticPublishTopic(topic) || isSmartRestLegacyPublishTopic(topic)
                || isSmartRestPublishTopic(topic) || isSmartRestTemplateCreationPublishTopic(topic);
    }


    public static boolean isTopicValidForSubscribe(String topic) {
        if (topic == null || topic.isEmpty()) {
            return false;
        }

        return isSmartRestExceptionsTopic(topic) || isSmartRestStaticSubscribeTopic(topic)
                || isSmartRestLegacySubscribeTopic(topic) || isSmartRestSubscribeTopic(topic)
                || isSmartRestTemplateCreationSubscribeTopic(topic) || isSmartRestLegacyOperationsTopic(topic);
    }


    private static boolean isSmartRestExceptionsTopic(String topic) {
        return topic.contains(BASE_SMARTREST_EXCEPTION_SUBSCRIBE);
    }

    private static boolean isSmartRestStaticPublishTopic(String topic) {
        return topic.matches(BASE_SMARTREST_STATIC_PUBLISH_PERSISTENT) ||
                topic.startsWith(BASE_SMARTREST_STATIC_PUBLISH_PERSISTENT + TOPIC_SEPARATOR);
    }

    private static boolean isSmartRestStaticSubscribeTopic(String topic) {
        return topic.contains(BASE_SMARTREST_STATIC_SUBSCRIBE);
    }

    private static boolean isSmartRestLegacyPublishTopic(String topic) {
        return topic.matches(BASE_SMARTREST_LEGACY_PUBLISH_PERSISTENT) ||
                topic.startsWith(BASE_SMARTREST_LEGACY_PUBLISH_PERSISTENT + TOPIC_SEPARATOR);
    }

    private static boolean isSmartRestLegacySubscribeTopic(String topic) {
        return topic.contains(BASE_SMARTREST_LEGACY_SUBSCRIBE);
    }

    private static boolean isSmartRestLegacyOperationsTopic(String topic) {
        return topic.contains(BASE_SMARTREST_LEGACY_OPERATIONS_PERSISTENT);
    }

    private static boolean isSmartRestPublishTopic(String topic) {
        return topic.startsWith(BASE_SMARTREST_CUSTOM_TEMPLATE_PUBLISH_PERSISTENT + TOPIC_SEPARATOR);
    }

    private static boolean isSmartRestSubscribeTopic(String topic) {
        return topic.contains(BASE_SMARTREST_CUSTOM_TEMPLATE_SUBSCRIBE);
    }

    private static boolean isSmartRestTemplateCreationPublishTopic(String topic) {
        return topic.startsWith(BASE_SMARTREST_TEMPLATE_CREATION_PUBLISH_PERSISTENT + TOPIC_SEPARATOR);
    }

    private static boolean isSmartRestTemplateCreationSubscribeTopic(String topic) {
        return topic.contains(BASE_SMARTREST_TEMPLATE_CREATION_SUBSCRIBE);
    }
}
