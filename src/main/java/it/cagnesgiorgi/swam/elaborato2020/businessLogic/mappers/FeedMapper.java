package it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers;

import it.cagnesgiorgi.swam.elaborato2020.DAO.TagDAO;
import it.cagnesgiorgi.swam.elaborato2020.DAO.ZoneDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.FeedDTO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.ModelFactory;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;


@RequestScoped
@Transactional
public class FeedMapper extends BaseMapper<FeedDTO,Feed> {

    @Inject
    private ZoneDAO zoneDAO;

    @Inject
    private TagDAO tagDAO;

    public FeedDTO convert(Feed feed){
        if(feed == null){
            throw new NullPointerException("Feed was not instantiated!");
        }
        FeedDTO feedDTO = new FeedDTO();
        feedDTO.setName(feed.getName());
        feedDTO.setUrl(feed.getUrl());
        feedDTO.setTopNews(feed.isTopNews());
        feedDTO.setTags(serializeTags(feed));
        Zone zone = feed.getZone();
        feedDTO.setCountryCode(serializeZone(zone));
        //this will also generate Resource Information data
        feedDTO.setId(feed.getId());
        feedDTO.setActive(feed.isActive());
        return feedDTO;
    }

    public void transfer(FeedDTO feedDto, Feed feed){
        if(feed == null){
            throw new NullPointerException("Feed was not instantiated!");
        }
        if(feedDto == null){
            throw new NullPointerException("FeedDTO was not instantiated!");
        }
        feed.setName(feedDto.getName());
        feed.setUrl(feedDto.getUrl());
        feed.setZone(deserializeZone(feedDto.getCountryCode()));
        if(feedDto.getId() != null){
            feed.setId(feedDto.getId());
        }
        if(feedDto.getTags()!=null){
            deserializeTags(feedDto,feed);
        }
        feed.setTopNews(feedDto.isTopNews());
        feed.setActive(feedDto.isActive());
    }

    public void deserializeTags(FeedDTO feedDTO, Feed feed){
        if(feedDTO.getTags()==null){
            return;
        }
        if(feedDTO.getTags().contains(",")) {
            String[] split = feedDTO.getTags().split(",");
            for (String s : split) {
                Tag tag = tagDAO.getTag(Long.parseLong(s));
                if (tag != null) {
                    feed.addTag(tag);
                }
            }
        }else{
            Tag tag = tagDAO.getTag(Long.parseLong(feedDTO.getTags()));
            if (tag != null) {
                feed.addTag(tag);
            }
        }
    }

    public String serializeTags(Feed feed){
        List<Tag> tags = feed.getTags();
        if(tags!=null) {
            if (tags.size() != 0) {
                StringBuilder sb = new StringBuilder();
                for (Tag t : tags) {
                    sb.append(t.getId()).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                return sb.toString();
            }
        }
        return null;
    }

    public Zone deserializeZone(String countryCode){
        if(countryCode!=null) {
            return zoneDAO.getByCountryCode(countryCode);
        }else{
            return null;
        }
    }

    public String serializeZone(Zone zone){
        if(zone!=null) {
            return zone.getCountryCode();
        }else{
            return null;
        }
    }
}
