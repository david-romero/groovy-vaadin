package es.test.util.common

import com.vaadin.data.util.converter.Converter
import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource

class ImageConverter implements Converter<Resource, String> {
	@Override
	public String convertToModel(Resource value,
			Class<? extends String> targetType, Locale l)
	throws Converter.ConversionException {
		return "not needed";
	}

	@Override
	public Resource convertToPresentation(String value,
			Class<? extends Resource> targetType, Locale l)
	throws Converter.ConversionException {
		return new ExternalResource(value)
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<Resource> getPresentationType() {
		return Resource.class;
	}
}

