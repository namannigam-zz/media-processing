package processing.sample.audio;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioTrim {

    public static void main(String[] args) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        String inputFilePath2 = "/Users/naman.nigam/GitHub/content-processing/sample/file_example_MP3_5MG.mp3";
        String outputFilePath2 = "/Users/naman.nigam/GitHub/content-processing/sample/1.mp3";

        executor.createJob(audioTrimFFMpegBuilder(inputFilePath2, outputFilePath2, 20000, 60000)).run();
    }

    static FFmpegBuilder audioTrimFFMpegBuilder(String input, String output, int startOffSetInMillis, int durationInMillis) {
        return new FFmpegBuilder()
                .setInput(input)
                .overrideOutputFiles(true)
                .addOutput(output)
                .setStartOffset(startOffSetInMillis, TimeUnit.MILLISECONDS)
                .setDuration(durationInMillis, TimeUnit.MILLISECONDS)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();
    }
}