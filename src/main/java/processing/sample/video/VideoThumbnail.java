package processing.sample.video;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VideoThumbnail {

    public static void main(String[] args) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        String sampleDir = "/Users/naman.nigam/GitHub/content-processing/issues";
        try (Stream<Path> paths = Files.walk(Paths.get(sampleDir))) {
            List<Path> collect = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                Path file = collect.get(i);
                String output = String.format("/Users/naman.nigam/GitHub/content-processing/issues/%sthumbnail.png", i);

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

                executor.createJob(videoThumbnailBuilder(file.toAbsolutePath().toString(), output)).run();
            }
        }
    }



    static FFmpegBuilder videoThumbnailBuilder(String input, String output) {
        return new FFmpegBuilder()
                .setInput(input)
                .addOutput(output)
                .setFrames(1) // the first frame
                .setStartOffset(0, TimeUnit.MILLISECONDS)
                .setVideoFilter("select='gte(n\\,10)',scale=700:-1")
                .done();
    }
}