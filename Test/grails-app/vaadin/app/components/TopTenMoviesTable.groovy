package app.components

import java.text.DecimalFormat

import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.Table
import com.vaadin.ui.Table.Align
import com.vaadin.ui.Table.ColumnHeaderMode
import com.vaadin.ui.Table.RowHeaderMode
import com.vaadin.ui.themes.ValoTheme

class TopTenMoviesTable extends Table {

    @Override
    protected String formatPropertyValue(final Object rowId,
            final Object colId, final Property<?> property) {
        String result = super.formatPropertyValue(rowId, colId, property);
        if (colId.equals("revenue")) {
            if (property != null && property.getValue() != null) {
                Double r = (Double) property.getValue();
                String ret = new DecimalFormat("#.##").format(r);
                result = '$' + ret;
            } else {
                result = "";
            }
        }
        return result;
    }

    public TopTenMoviesTable() {
        setCaption("Top 10 Titles by Revenue");

        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        setSortEnabled(false);
        setColumnAlignment("revenue", Align.RIGHT);
        setRowHeaderMode(RowHeaderMode.INDEX);
        setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        setSizeFull();

        List<MovieRevenue> movieRevenues = new ArrayList<MovieRevenue>();
		//DUMMY-----------------------------------------------------------
		MovieRevenue mv = new MovieRevenue()
		mv.setRevenue(136546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 1")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(13634.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 18")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(136546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 17")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(13246.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 16")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(36546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 15")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(56.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 14")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(19846.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 13")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(198546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 12")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(99546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 11")
		movieRevenues.add(mv)
		mv = new MovieRevenue()
		mv.setRevenue(99999996546.456)
		mv.setTimestamp(new Date())
		mv.setTitle("Title 10")
		movieRevenues.add(mv)
		//DUMMY-----------------------------------------------------------
        Collections.sort(movieRevenues, new Comparator<MovieRevenue>() {
            @Override
            public int compare(final MovieRevenue o1, final MovieRevenue o2) {
                return o2.getRevenue().compareTo(o1.getRevenue());
            }
        });

        setContainerDataSource(new BeanItemContainer<MovieRevenue>(
                MovieRevenue.class, movieRevenues.subList(0, 10)));

        setVisibleColumns("title", "revenue");
        setColumnHeaders("Title", "Revenue");
        setColumnExpandRatio("title", 2);
        setColumnExpandRatio("revenue", 1);

        setSortContainerPropertyId("revenue");
        setSortAscending(false);
    }

}
