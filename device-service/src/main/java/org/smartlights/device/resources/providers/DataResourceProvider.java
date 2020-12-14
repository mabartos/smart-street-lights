package org.smartlights.device.resources.providers;

import org.smartlights.device.dto.DeviceDataDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.DeviceDataRepository;
import org.smartlights.device.resources.DataResource;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.utils.Constants;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class DataResourceProvider implements DataResource {

    private final DeviceSession session;
    private final DeviceDataRepository dataRepository;
    private final DeviceSerializer serializer;
    private final Long parentID;

    public DataResourceProvider(DeviceSession session) {
        this.session = session;
        this.dataRepository = session.getDeviceDataRepository();
        this.parentID = session.getActualDeviceID();
        this.serializer = session.getDeviceSerializer();
    }

    @GET
    public Stream<DeviceDataDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return dataRepository.getAllFromParent(parentID, firstResult, maxResults).map(serializer::entityToModel);
    }

    @GET
    @Path("recent/{timestamp}")
    public Stream<DeviceDataDTO> getAllRecentThan(@PathParam("timestamp") Timestamp timestamp,
                                                  @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                                  @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return dataRepository.getAllRecentThan(parentID, timestamp, firstResult, maxResults).map(serializer::entityToModel);
    }

    @GET
    @Path("recent/{timestamp}")
    public Stream<DeviceDataDTO> getAllOlderThan(@PathParam("timestamp") Timestamp timestamp,
                                                 @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return dataRepository.getAllOlderThan(parentID, timestamp, firstResult, maxResults).map(serializer::entityToModel);
    }

    @DELETE
    @Path("{timestamp}")
    public boolean removeOlderThan(@PathParam("timestamp") Timestamp timestamp) {
        return dataRepository.removeOlderThan(parentID, timestamp);
    }
}
