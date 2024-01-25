package com.nayaragaspar.gprfid.model.message;

import java.time.LocalDateTime;

public record TagMessage(String epcTag, String ip, LocalDateTime readDateTime) {

}
