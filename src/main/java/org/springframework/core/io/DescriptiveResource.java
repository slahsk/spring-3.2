package org.springframework.core.io;

public class DescriptiveResource extends AbstractResource {
	
	private final String description;
	
	public DescriptiveResource(String description) {
		this.description = (description != null ? description : "");
	}
}
