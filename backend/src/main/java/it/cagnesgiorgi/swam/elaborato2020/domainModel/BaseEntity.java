package it.cagnesgiorgi.swam.elaborato2020.domainModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (unique = true)
    private String uuid;

    protected BaseEntity(String uuid) {
        this.uuid = uuid;
    }

    protected BaseEntity() {

    }

    @Override
    public boolean equals(Object baseEntity) {
        if(baseEntity == null){
            return false;
        }
        if(!this.getClass().equals(baseEntity.getClass())){
            return false;
        }
        return this.uuid.equals(((BaseEntity)baseEntity).uuid);
    }
}
