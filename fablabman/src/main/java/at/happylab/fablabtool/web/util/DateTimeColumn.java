package at.happylab.fablabtool.web.util;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Source: http://apache-wicket.1842946.n4.nabble.com/Date-format-in-DataTable-td1887346.html

public class DateTimeColumn<T> extends PropertyColumn<T> {
	// **********************************************************************************************************************
	// Fields
	// **********************************************************************************************************************

	private static final long serialVersionUID = -1285893946332340828L;
	private final String format;

	// **********************************************************************************************************************
	// Constructors
	// **********************************************************************************************************************

	public DateTimeColumn(IModel<String> displayModel, String propertyExpression, String format) {
		super(displayModel, propertyExpression);
		this.format = format;
	}

	public DateTimeColumn(IModel iModel, String sortProperty, String propertyExpression, String format) {
		super(iModel, sortProperty, propertyExpression);
		this.format = format;
	}

	// **********************************************************************************************************************
	// Other Methods
	// **********************************************************************************************************************

	@SuppressWarnings("unchecked")
	@Override
	protected IModel createLabelModel(IModel iModel) {
		return new DateTimeModel(super.createLabelModel(iModel));
	}

	// **********************************************************************************************************************
	// Inner Classes
	// **********************************************************************************************************************

	private class DateTimeModel implements IModel<String> {
		private final IModel inner;
		private static final long serialVersionUID = 190887916985140272L;

		private DateTimeModel(IModel inner) {
			this.inner = inner;
		}

		public void detach() {
			inner.detach();
		}

		public String getObject() {
			Date dateTime = (Date) inner.getObject();
			if (dateTime == null) {
				return "";
			}
			final Date date = dateTime;
			SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
			return dateFormatter.format(date);
		}

		public void setObject(String s) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
			try {
				Date date = dateFormatter.parse(s);
				inner.setObject(new Date(date.getTime()));
			} catch (ParseException e) {
				throw new WicketRuntimeException("Unable to parse date.", e);
			}
		}
	}
}
