package processing.sample.video;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import processing.sample.Dimensions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VideoProcessing {

    public static void main(String[] args) throws IOException {

        FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        String sampleDir = "/Users/naman.nigam/Desktop/Feeds/Media/video";
        try (Stream<Path> paths = Files.walk(Paths.get(sampleDir))) {
            List<Path> collect = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                Path file = collect.get(i);
                String output = String.format("/Users/naman.nigam/Desktop/Naman/content-processing/media/video/%sthumbnail.png", i);

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

                executor.createJob(defaultVideoAttributesBuilder(file.toAbsolutePath().toString(), output,
                        "", "", 1L, 1, Dimensions.builder().build())).run();

            }
        }
    }

    static FFmpegBuilder defaultVideoAttributesBuilder(String input, String output, String format, String codec,
                                                       Long bitrate, Integer framePerSec, Dimensions dimensions) {
        return new FFmpegBuilder()
                .setInput(input)
                .overrideOutputFiles(true) // override the output if it exists
                .addOutput(output)
                .setFormat(format)
                .setVideoCodec(codec)
                .setVideoBitRate(bitrate)
                .setVideoFrameRate(framePerSec, 1)
                .setVideoResolution(dimensions.getWidth(), dimensions.getHeight())
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .addExtraArgs("-max_muxing_queue_size", "1024")
                .done();
    }
}