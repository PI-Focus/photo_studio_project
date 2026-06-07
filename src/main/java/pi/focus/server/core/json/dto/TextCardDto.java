package pi.focus.server.core.json.dto;

import pi.focus.server.service.models.TextCard;

import java.util.List;

public record TextCardDto(List<TextCard> cards) {
}
