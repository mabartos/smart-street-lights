package org.smartlights.device.services;

import org.smartlights.device.dto.DeviceDataDTO;

public interface DeviceService {

    boolean handleData(DeviceDataDTO data);

    boolean handleData(Long deviceID, DeviceDataDTO data);
}
