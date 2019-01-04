package us.dustinj.timezonemap.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

public class SerializationTest {

    @Test
    public void envelopSerializationRoundTrip() {
        Envelope wholeWorld = new Envelope(new LatLon(-90, 180), new LatLon(90, 180));

        assertThat(Serialization.deserializeEnvelope(Serialization.serializeEnvelope(wholeWorld)))
                .isEqualTo(wholeWorld);
    }

    @Test
    @Ignore
    public void timeZoneSerializationRoundTrip() {
        TimeZone expectedTimeZone = new TimeZone("TestTimeZone",
                IntStream.range(1, 5)
                        .mapToObj(polygon -> IntStream.range(1, 3)
                                .mapToObj(ring -> IntStream.range(1, 500)
                                        .mapToObj(point -> new LatLon(
                                                (polygon * ring * point * 10f % 180.0) - 90,
                                                (float) (polygon * ring * point % 360) - 180))
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()));
        ByteBuffer serializedTimeZone = Serialization.serializeTimeZone(expectedTimeZone, 1_000_000);
        TimeZone actualTimeZone = Serialization.deserializeTimeZone(serializedTimeZone, 1_000_000);

        assertThat(actualTimeZone).isEqualTo(expectedTimeZone);
    }
}