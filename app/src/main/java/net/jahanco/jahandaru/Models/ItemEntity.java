package net.jahanco.jahandaru.Models;

/**
 * Created by Mr R00t on 4/22/2017.
 */
//id 	name 	en_name
// scientific_name 	family 	nature
// specifications 	ingredients 	properties
// 	contraindications 	organs 	habitat 	construction
public class ItemEntity {
    public static final int TYPE_NORMAL=0;
    public static final int TYPE_PRODUCT=1;
    private int id;
    private String name;
    private String en_name;
    private String scientific_name;
    private String family;
    private String nature;
    private String specifications;
    private String ingredients;
    private String properties;
    private String contraindications;
    private String organs;
    private String habitat;
    private String construction;
    private String backImage;
    private String image;
    private boolean purchase ;
    private String price;
    private String tags;
    private boolean favorite;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getOrgans() {
        return organs;
    }

    public void setOrgans(String organs) {
        this.organs = organs;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", en_name='" + en_name + '\'' +
                ", scientific_name='" + scientific_name + '\'' +
                ", family='" + family + '\'' +
                ", nature='" + nature + '\'' +
                ", specifications='" + specifications + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", properties='" + properties + '\'' +
                ", contraindications='" + contraindications + '\'' +
                ", organs='" + organs + '\'' +
                ", habitat='" + habitat + '\'' +
                ", construction='" + construction + '\'' +
                ", backImage='" + backImage + '\'' +
                ", image='" + image + '\'' +
                ", purchase=" + purchase +
                ", price='" + price + '\'' +
                ", tags='" + tags + '\'' +
                ", favorite=" + favorite +
                ", type=" + type +
                '}';
    }
}
