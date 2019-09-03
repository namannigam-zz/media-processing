package processing.sample.video;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoTrimming {

    static FFmpegBuilder videoTrimBuilder(String input, String output, int startOffSetInMillis, int durationInMillis) {
        return new FFmpegBuilder()
                .setInput(input)
                .addExtraArgs("max_muxing_queue_size=4096")
                .overrideOutputFiles(true)
                .addOutput(output)
                .setStartOffset(startOffSetInMillis, TimeUnit.MILLISECONDS)
                .setDuration(durationInMillis, TimeUnit.MILLISECONDS)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .setVideoBitRate(700000)
                .setFormat("mp4")
                .done();
    }


    public static void main(String[] args) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        String inputFilePath1 = "/Users/naman.nigam/GitHub/content-processing/sample/MEDJT66YPCDWMLN8LZ-processed-0-RAW5HCETIPBNWII0FI-CGTFs6b1.mp4";
        String outputFilePath1 = "/Users/naman.nigam/GitHub/content-processing/sample/CGTojBYh-3.mp4";
        executor.createTwoPassJob(videoTrimBuilder(inputFilePath1, outputFilePath1, 57262, 60000)).run();
    }
}