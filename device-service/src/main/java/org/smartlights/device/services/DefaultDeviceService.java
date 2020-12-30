package org.smartlights.device.services;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultDeviceService implements DeviceService {
    private static final Logger logger = Logger.getLogger(DefaultDeviceService.class.getName());
}
