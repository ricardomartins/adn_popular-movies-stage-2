package pt.rikmartins.adn.popularmoviesstage2.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import pt.rikmartins.adn.popularmoviesstage2.domain.model.MovieInfo;

public class MovieInfoImpl implements MovieInfo, Parcelable {

    private Integer id;

    private String title;

    private String overview;

    private String posterPath;

    private String releaseDate;

    private Float voteAverage;

    protected MovieInfoImpl(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readFloat();
        }
    }

    public MovieInfoImpl(MovieInfo movieInfo) {
        id = movieInfo.getId();
        title = movieInfo.getTitle();
        overview = movieInfo.getOverview();
        posterPath = movieInfo.getPosterPath();
        releaseDate = movieInfo.getReleaseDate();
        voteAverage = movieInfo.getVoteAverage();
    }

    public static final Creator<MovieInfoImpl> CREATOR = new Creator<MovieInfoImpl>() {
        @Override
        public MovieInfoImpl createFromParcel(Parcel in) {
            return new MovieInfoImpl(in);
        }

        @Override
        public MovieInfoImpl[] newArray(int size) {
            return new MovieInfoImpl[size];
        }
    };

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(voteAverage);
        }
    }
}
