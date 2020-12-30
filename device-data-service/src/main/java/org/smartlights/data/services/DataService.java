package org.smartlights.data.services;


import org.smartlights.data.dto.DeviceDataDTO;

public interface DataService {

    boolean handleData(DeviceDataDTO data);

    boolean handleData(Long deviceID, DeviceDataDTO data);
}
