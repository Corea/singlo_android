package com.garagestory.singlo.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Coord implements Parcelable {

	public double x;
	public double y;
	public int draw_type;
	public int paint_type;

	public Coord(double x, double y, int draw_type, int paint_type) {
		this.x = x;
		this.y = y;
		this.draw_type = draw_type;
		this.paint_type = paint_type;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(x);
		out.writeDouble(y);
		out.writeInt(draw_type);
		out.writeInt(paint_type);
	}

	public Coord(Parcel in) {
		x = in.readDouble();
		y = in.readDouble();
		draw_type = in.readInt();
		paint_type = in.readInt();
	}

	public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>() {
		public Coord createFromParcel(Parcel in) {
			return new Coord(in);
		}

		public Coord[] newArray(int size) {
			return new Coord[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
