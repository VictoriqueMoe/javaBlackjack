package moe.victorique.blackjack.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import moe.victorique.blackjack.constants.PlayStatus;


@Converter(autoApply = true)
public class PlayStatusConverter implements AttributeConverter<PlayStatus, String> {

    @Override
    public String convertToDatabaseColumn(final @Nullable PlayStatus playStatus) {
        if (playStatus == null) {
            return null;
        }
        return playStatus.getStatus();
    }

    @Override
    public PlayStatus convertToEntityAttribute(final @Nullable String status) {
        if (status == null) {
            return null;
        }
        // Convert string value back to enum
        for (final var playStatus : PlayStatus.values()) {
            if (playStatus.getStatus().equals(status)) {
                return playStatus;
            }
        }
        throw new IllegalArgumentException("Unknown play status: " + status);
    }
}
