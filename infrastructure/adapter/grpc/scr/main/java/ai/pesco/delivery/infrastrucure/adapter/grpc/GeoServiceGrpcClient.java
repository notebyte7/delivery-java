package ai.pesco.delivery.infrastrucure.adapter.grpc;

import geo.GeoGrpc;
import geo.GetGeolocationReply;
import geo.GetGeolocationRequest;
import io.grpc.ManagedChannel;
public class GeoServiceGrpcClient extends GeoGrpc.GeoImplBase implements GetLocationPort {

    private final ManagedChannel managedChannel;

    public GeoServiceGrpcClient(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
    }

    @Override
    public Location getFromStreet(String street) {
        GeoGrpc.GeoBlockingStub stub = GeoGrpc.newBlockingStub(managedChannel);

        GetGeolocationRequest request = GetGeolocationRequest.newBuilder()
                .setStreet(street)
                .build();

        GetGeolocationReply response = stub.getGeolocation(request);

        return new Location(response.getLocation().getX(), response.getLocation().getY());
    }
}
