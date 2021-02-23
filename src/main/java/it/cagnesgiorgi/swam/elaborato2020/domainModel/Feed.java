package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Entity
@Table(name = "feed")
public class Feed extends BaseEntity {
	@Column(unique = true)
	private String url;
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Tag> tags;

	@ManyToOne
	private Zone zone;

	private boolean topNews;

	private boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Feed() {
		tags = new ArrayList<Tag>();
	}
	
	public Feed(String uuid) {
		super(uuid);
		tags = new ArrayList<Tag>();
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag){
		tags.add(tag);
	}

	public void removeTag(Tag tag){
		tags.remove(tag);
	}

	public void removeTags(){
		tags.clear();
	}

	public boolean isTopNews() {
		return topNews;
	}

	public void setTopNews(boolean topNews) {
		this.topNews = topNews;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
