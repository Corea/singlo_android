package com.garagestory.singlo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecorder {
	private static final int RECORDER_BPP = 16;
	private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp";
	private static final String AUDIO_RECORDER_TEMP_EXTENSION = ".raw";
	private static final String AUDIO_RECORDER_FINAL_FILE = "record_final";
	private static final String AUDIO_RECORDER_FINAL_EXTENSION = ".wav";
	private static final int RECORDER_SAMPLERATE = 8000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private int hash;
	private int bufferSize;
	private AudioRecord recorder = null;
	private Thread recordingThread = null;

	private boolean isRecording = false;
	private boolean isStarted = false;

	private Context context;

	private String tempFilename;
	private String realFilename;

	// getCacheFilename

	public AudioRecorder(Context context, int hash) {
		this.context = context;
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
				RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

		this.hash = hash;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public String getTempFilename() {
		return tempFilename;
	}

	public void startRecording() {
		if (isStarted) {
			return;
		}
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, bufferSize);
		recorder.startRecording();
		isRecording = true;
		isStarted = true;
		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				writeAudioDataToFile(false);
			}
		}, "AudioRecorder Thread");

		recordingThread.start();
	}

	public void pauseRecording() {
		if (recorder != null) {
			isRecording = false;

			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
		}
	}

	public void resumeRecording() {
		if (!isStarted || isRecording) {
			return;
		}
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, bufferSize);
		recorder.startRecording();
		isRecording = true;
		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				writeAudioDataToFile(true);
			}
		}, "AudioRecorder Thread");

		recordingThread.start();
	}
/*
	public void stopRecording() {
		isRecording = false;
		isStarted = false;

		if (recorder != null) {
			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
		}

		realFilename = Utility.getCacheFilename(context,
				AUDIO_RECORDER_FINAL_FILE + AUDIO_RECORDER_FINAL_EXTENSION,
				true);
		copyWaveFile(tempFilename, realFilename);
		deleteTempFile();
	}
*/
	private void writeAudioDataToFile(boolean is_resume) {
		byte data[] = new byte[bufferSize];
		tempFilename = Utility
				.getCacheFilename(context, AUDIO_RECORDER_TEMP_FILE + hash
						+ AUDIO_RECORDER_TEMP_EXTENSION, !is_resume);
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(tempFilename, is_resume);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int read = 0;

		if (os != null) {
			while (isRecording) {
				read = recorder.read(data, 0, bufferSize);
				if (AudioRecord.ERROR_INVALID_OPERATION != read) {
					try {
						os.write(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void WriteWaveFileHeader(FileOutputStream out,
			long totalAudioLen, long totalDataLen, long longSampleRate,
			int channels, long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = RECORDER_BPP; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}

	private void deleteTempFile() {
		File file = new File(tempFilename);

		file.delete();
	}

	private static void deleteTempFile(String filename) {
		File file = new File(filename);

		file.delete();
	}


	public static void mergeRecorder(Context context,
			List<AudioRecorder> audioRecorderList) {

		String realFilename = Utility.getCacheFilename(context,
				AUDIO_RECORDER_FINAL_FILE + AUDIO_RECORDER_FINAL_EXTENSION,
				true);

		long totalAudioLen = 0;
		for (int i = 0; i < audioRecorderList.size(); i++) {
			try {
				FileInputStream in = new FileInputStream(audioRecorderList.get(
						i).getTempFilename());
				totalAudioLen += in.getChannel().size();
				in.close();
			} catch (Exception e) {

			}
		}

		try {
			FileInputStream in = null;
			FileOutputStream out = new FileOutputStream(realFilename);
			long totalDataLen = totalAudioLen + 44;
			long longSampleRate = RECORDER_SAMPLERATE;
			int channels = 2;
			long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;

			int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
					RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
			byte[] data = new byte[bufferSize];

			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);
			for (int i = 0; i < audioRecorderList.size(); i++) {
				in = new FileInputStream(audioRecorderList.get(i)
						.getTempFilename());

				while (in.read(data) != -1) {
					out.write(data);
				}

				in.close();
				deleteTempFile(audioRecorderList.get(i).getTempFilename());
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
