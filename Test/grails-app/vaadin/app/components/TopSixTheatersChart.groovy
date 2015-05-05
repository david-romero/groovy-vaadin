package app.components

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;

class TopSixTheatersChart extends Chart {

    public TopSixTheatersChart() {
        super(ChartType.PIE);

        setCaption("Popular Movies");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.PIE);
        getConfiguration().getChart().setAnimation(false);
        setWidth("100%");
        setHeight("90%");

        DataSeries series = new DataSeries();

        List<Movie> movies = new ArrayList<Movie>();
		
		Movie m1 = new Movie();
		m1.setScore(25)
		m1.setTitle("ESASDSA")
		movies.add(m1)
		
		m1 = new Movie();
		m1.setScore(5)
		m1.setTitle("EkllllklklklkA")
		movies.add(m1)
		
		m1 = new Movie();
		m1.setScore(10)
		m1.setTitle("lllllllllll")
		movies.add(m1)
		
		m1 = new Movie();
		m1.setScore(20)
		m1.setTitle("rtytrtyrty")
		movies.add(m1)
		
		m1 = new Movie();
		m1.setScore(15)
		m1.setTitle("ESffgSA")
		movies.add(m1)
		
		m1 = new Movie();
		m1.setScore(25)
		m1.setTitle("piopiopio")
		movies.add(m1)
		
		
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            DataSeriesItem item = new DataSeriesItem(movie.getTitle(),
                    movie.getScore());
            series.add(item);
        }
        getConfiguration().setSeries(series);

        PlotOptionsPie opts = new PlotOptionsPie();
        opts.setBorderWidth(0);
        opts.setShadow(false);
        opts.setAnimation(false);
        getConfiguration().setPlotOptions(opts);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);
    }

	public final class Movie {
		private long id;
		private String title;
		private String synopsis;
		private String thumbUrl;
		private String posterUrl;
		private int duration;
		private Date releaseDate;
		private int score;
	
		public void setId(final long id) {
			this.id = id;
		}
	
		public void setTitle(final String title) {
			this.title = title;
		}
	
		public void setSynopsis(final String synopsis) {
			this.synopsis = synopsis;
		}
	
		public void setThumbUrl(final String thumbUrl) {
			this.thumbUrl = thumbUrl;
		}
	
		public void setPosterUrl(final String posterUrl) {
			this.posterUrl = posterUrl;
		}
	
		public void setDuration(final int duration) {
			this.duration = duration;
		}
	
		public Date getReleaseDate() {
			return releaseDate;
		}
	
		public void setReleaseDate(final Date releaseDate) {
			this.releaseDate = releaseDate;
		}
	
		public int getScore() {
			return score;
		}
	
		public void setScore(final int score) {
			this.score = score;
		}
	
		public String getTitle() {
			return title;
		}
	
		public String getSynopsis() {
			return synopsis;
		}
	
		public String getThumbUrl() {
			return thumbUrl;
		}
	
		public String getPosterUrl() {
			return posterUrl;
		}
	
		public int getDuration() {
			return duration;
		}
	
		public long getId() {
			return id;
		}
	
	}
	
}
