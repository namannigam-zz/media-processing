package processing.sample.video;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class VideoBitRate {

    private static OptionalLong getVideoBitrate(FFmpegProbeResult probeResult) {
        List<FFmpegStream> str = new ArrayList<>(probeResult.getStreams());
        long maxBitrate = Long.MIN_VALUE;
        for(FFmpegStream stream : str) {
            if (stream.codec_type.equals(FFmpegStream.CodecType.VIDEO)) {
                long bitrate = stream.bit_rate;
                maxBitrate = Math.max(bitrate, maxBitrate);
            }
        }
        return probeResult.getStreams().stream()
                .filter(a -> a.codec_type.equals(FFmpegStream.CodecType.VIDEO))
                .mapToLong(a -> a.bit_rate)
                .max();
    }

    public static void main(String[] args) throws IOException {
        FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
        FFmpegProbeResult fFmpegProbeResult = ffprobe.probe("/Users/naman.nigam/GitHub/content-processing/issues/20190726-RAW7FXABZY3MKOMJX0-CGTo9RzL.mov");
        getVideoBitrate(fFmpegProbeResult);
    }
}
