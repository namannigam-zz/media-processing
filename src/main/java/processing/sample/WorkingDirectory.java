package processing.sample;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.RunProcessFunction;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;

public class WorkingDirectory {

    public static void main(String[] args) throws IOException {
        RunProcessFunction func = new RunProcessFunction();
        func.setWorkingDirectory(System.getProperty("user.dir"));

        FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg", func);
        FFprobe ffprobe = new FFprobe("/path/to/ffprobe", func);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("input")
                .addOutput("output.mp4")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a two-pass encode
        executor.createTwoPassJob(builder).run();
    }
}