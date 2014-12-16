package Interpreter.Motion.Attributes;

public class TimeFormat {
	
	private TimeFormatType format = TimeFormatType.SECONDS;
	
	public enum TimeFormatType{
		SECONDS,
		MILLISECONDS;
	}

	public TimeFormatType getFormat() {
		return format;
	}

	public void setFormat(TimeFormatType format) {
		this.format = format;
	}

	public double scaleToMillis(double delay) {
		switch (format){
		case SECONDS:
			return 1000.0*delay;
		case MILLISECONDS:
		default:
			return delay;
		}
	};

}
