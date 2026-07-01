package com.logistics.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentStatusEvent implements Serializable {
    private String trackingNumber;
    private String status;
    private String destination;
    private String driverName;
    private String timestamp;
}
