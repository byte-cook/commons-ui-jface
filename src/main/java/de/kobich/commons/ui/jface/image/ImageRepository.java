package de.kobich.commons.ui.jface.image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class ImageRepository {
	private final Device device;
	private final Class<?> source;
	private Map<String, Image> images;
	
	public ImageRepository(Device device, Class<?> source) {
		this.device = device;
		this.source = source;
		this.images = new HashMap<String, Image>();
	}
	
	public Image getImage(String name) {
		if (images.containsKey(name)) {
			return images.get(name);
		}
		InputStream stream = source.getResourceAsStream(name);
		Image img = new Image(device, stream);
		images.put(name, img);
		return img;
	}
	
	public ImageDescriptor getImageDescriptor(String name) {
		return ImageDescriptor.createFromFile(source, name);
	}
	
	public void dispose() {
		for (Image img : images.values()) {
			img.dispose();
		}
	}
}
