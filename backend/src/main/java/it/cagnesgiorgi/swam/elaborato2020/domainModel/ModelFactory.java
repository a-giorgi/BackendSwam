package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import java.util.UUID;

public class ModelFactory {
	private ModelFactory() {}
	public static User user(){
		return new User(UUID.randomUUID().toString());
	}
	public static Administrator administrator(){
		return new Administrator(UUID.randomUUID().toString());
	}
	public static Feed feed(){
		return new Feed(UUID.randomUUID().toString());
	}
	public static Tag tag(){
		return new Tag(UUID.randomUUID().toString());
	}
	public static Zone zone(){
		return new Zone(UUID.randomUUID().toString());
	}
}
