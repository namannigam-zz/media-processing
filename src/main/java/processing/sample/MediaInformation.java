package processing.sample;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MediaInformation {

    public static void main(String[] args) throws IOException {
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
        String sampleDir = "/Users/naman.nigam/GitHub/content-processing/sample";
        try (Stream<Path> paths = Files.walk(Paths.get(sampleDir))) {
            List<Path> collect = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path file : collect) {
                FFmpegProbeResult probeResult = ffprobe.probe(file.toAbsolutePath().toString());

                FFmpegFormat format = probeResult.getFormat();
                System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs",
                        format.filename,
                        format.format_long_name,
                        format.duration
                );

                FFmpegStream stream = probeResult.getStreams().get(0);
                System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx\n",
                        stream.codec_long_name,
                        stream.width,
                        stream.height
                );

                Long bitrate = probeResult.getStreams()
                        .stream()
                        .filter(a -> a.codec_type.equals(FFmpegStream.CodecType.VIDEO))
                        .map(a -> a.bit_rate)
                        .findFirst()
                        .orElse(null);

                System.out.println(bitrate);
            }
        }
    }
}