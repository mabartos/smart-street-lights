package org.smartlights.device.resources.providers;

import org.smartlights.device.dto.DeviceDataDTO;
import org.smartlights.device.dto.DeviceSerializer;
import org.smartlights.device.entity.DeviceDataEntity;
import org.smartlights.device.entity.repository.DeviceDataRepository;
import org.smartlights.device.resources.DataResource;
import org.smartlights.device.resources.DeviceSession;
import org.smartlights.device.utils.Constants;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@RequestScoped
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

    @POST
    public Response handleData(DeviceDataDTO data) {
        data = serializer.setUpActualDeviceProperties(data, session.getDeviceRepository().getByID(parentID));
        return session.getDeviceService().handleData(parentID, data) ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    public Stream<DeviceDataDTO> getAll(@QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                        @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return addParentID(dataRepository.getAllFromParent(parentID, firstResult, maxResults));
    }

    @GET
    @Path("recent/{timestamp}")
    public Stream<DeviceDataDTO> getAllRecentThan(@PathParam("timestamp") Timestamp timestamp,
                                                  @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                                  @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return addParentID(dataRepository.getAllRecentThan(parentID, timestamp, firstResult, maxResults));
    }

    @GET
    @Path("recent/{timestamp}")
    public Stream<DeviceDataDTO> getAllOlderThan(@PathParam("timestamp") Timestamp timestamp,
                                                 @QueryParam(Constants.FIRST_RESULT_PARAM) Integer firstResult,
                                                 @QueryParam(Constants.MAX_RESULTS_PARAM) Integer maxResults) {
        return addParentID(dataRepository.getAllOlderThan(parentID, timestamp, firstResult, maxResults));
    }

    @DELETE
    @Path("{timestamp}")
    public boolean removeOlderThan(@PathParam("timestamp") Timestamp timestamp) {
        return dataRepository.removeOlderThan(parentID, timestamp);
    }

    private Stream<DeviceDataDTO> addParentID(Stream<DeviceDataEntity> stream) {
        return stream.map(f -> serializer.entityToModel(f, parentID));
    }
}
