package it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers;

import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.TagDTO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;

public class TagMapper extends BaseMapper<TagDTO, Tag>{
    @Override
    public TagDTO convert(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setActive(tag.isActive());
        tagDTO.setName(tag.getName());
        tagDTO.setId(tag.getId());
        return tagDTO;
    }

    @Override
    public void transfer(TagDTO tagDTO, Tag tag) {
        tag.setName(tagDTO.getName());
        tag.setActive(tagDTO.isActive());
    }
}
