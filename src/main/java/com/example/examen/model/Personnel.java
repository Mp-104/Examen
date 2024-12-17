package com.example.examen.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Personnel {
    //TODO - in progress


    public Personnel(Long id,
                     String firstName,
                     String lastName,
                     String rank,
                     String branch,
                     String homeAddress,
                     String countryAllegiance,
                     byte[] picture,
                     List<byte[]> pictures,
                     String image,
                     List<String> images,
                     CustomUser customUser) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.branch = branch;
        this.homeAddress = homeAddress;
        this.countryAllegiance = countryAllegiance;
        this.picture = picture;
        this.pictures = pictures;
        this.image = image;
        this.images = images;
        this.customUser = customUser;
    }

    public Personnel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String rank;
    private String branch;
    private String homeAddress;
    private String countryAllegiance;
    private String description;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Transient
    private byte[] picture;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "personnel_pictures", joinColumns = @JoinColumn(name = "personnel_id"))
    @Column(name = "picture")
    @Transient
    private List<byte[]> pictures;


    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name= "personnel_images", joinColumns = @JoinColumn(name = "personnel_id"))
    @Column(name= "image")
    private List<String> images;

    @ManyToOne
    @Basic(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_user_id")
    private CustomUser customUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCountryAllegiance() {
        return countryAllegiance;
    }

    public void setCountryAllegiance(String countryAllegiance) {
        this.countryAllegiance = countryAllegiance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(List<byte[]> pictures) {
        this.pictures = pictures;
    }
}
