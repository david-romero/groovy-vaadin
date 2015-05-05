package app.components

import java.util.ArrayList;
import java.util.List;

import app.components.TopSixTheatersChart.Movie
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.Series;

class TopGrossingMoviesChart extends Chart {

    public TopGrossingMoviesChart() {
        setCaption("Top Grossing Movies");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.BAR);
        getConfiguration().getChart().setAnimation(false);
        getConfiguration().getxAxis().getLabels().setEnabled(false);
        getConfiguration().getxAxis().setTickWidth(0);
        getConfiguration().getyAxis().setTitle("");
        setSizeFull();

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

        List<Series> series = new ArrayList<Series>();
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            PlotOptionsBar opts = new PlotOptionsBar();
            opts.setBorderWidth(0);
            opts.setShadow(false);
            opts.setPointPadding(0.4);
            opts.setAnimation(false);
            ListSeries item = new ListSeries(movie.getTitle(), movie.getScore());
            item.setPlotOptions(opts);
            series.add(item);

        }
        getConfiguration().setSeries(series);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);

        PlotOptionsBar opts = new PlotOptionsBar();
        opts.setGroupPadding(0);
        getConfiguration().setPlotOptions(opts);

    }
	
}

