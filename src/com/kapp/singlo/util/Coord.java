package com.kapp.singlo.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Coord implements Parcelable {

	public double x;
	public double y;
	public boolean stop;

	public Coord(double x, double y, boolean stop) {
		this.x = x;
		this.y = y;
		this.stop = stop;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(x);
		out.writeDouble(y);
		if (stop) {
			out.writeInt(1);
		} else {
			out.writeInt(0);
		}
	}

	public Coord(Parcel in) {
		x = in.readDouble();
		y = in.readDouble();
		int tmp = in.readInt();
		if (tmp == 1) {
			stop = true;
		} else {
			stop = false;
		}
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
		// TODO Auto-generated method stub
		return 0;
	}

}
