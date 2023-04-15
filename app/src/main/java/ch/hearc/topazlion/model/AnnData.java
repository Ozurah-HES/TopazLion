package ch.hearc.topazlion.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// https://www.baeldung.com/jackson-nested-values
// https://www.baeldung.com/jackson-xml-serialization-and-deserialization

public class AnnData {
    // @JacksonXmlProperty(isAttribute = true, localName = "precision") // Cannot use this, because it is nested nodes
    private String precision;

    private String image;

    private String mainTitle;

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getPrecision() {
        return precision;
    }

    public String getImage() {
        return image;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    @Override
    public String toString() {
        return "AnnData ["
                + "precision=" + precision
                + ", image=" + image
                + ", mainTitle=" + mainTitle
                + "]";
    }

    @JacksonXmlProperty(localName = "manga")
    private void unpackMangaData(Map<String, Object> mainRoot) {
        unpackData(mainRoot);
    }

    @JacksonXmlProperty(localName = "anime")
    private void unpackAnimeData(Map<String, Object> mainRoot) {
        unpackData(mainRoot);
    }

    private void unpackData(Map<String, Object> mainRoot) {
        // Retrive the precision (it is an attribute)
        precision = (String)mainRoot.get("precision");

        // Get the info block (contains the main information)
        List<Map<String, Object>> info = (List<Map<String, Object>>) mainRoot.get("info");


        // Retrive the main title
        Map<String, Object> mainTitleBlock = info.stream()
                .filter(item -> item.get("type").equals("Main title"))
                .findFirst().get();
        mainTitle = (String) mainTitleBlock.get(""); // Leaf is stored in "" key

        // Retrive the biggest image
        Map<String, Object> pictureListBlock = info.stream()
                .filter(item -> item.get("type").equals("Picture"))
                .findFirst().get();

        List<Map<String, Object>> imagesBlock = (List<Map<String, Object>>) pictureListBlock.get("img");

        // Get the biggest image (by area)
        Map<String, Object> biggestImageBlock = imagesBlock.stream().max(
                (Map<String, Object> a, Map<String, Object> b) -> {
                    var aSize = Integer.parseInt((String) a.get("width")) * Integer.parseInt((String) a.get("height"));
                    var bSize = Integer.parseInt((String) b.get("width")) * Integer.parseInt((String) b.get("height"));
                    return aSize - bSize;
                }).get();
        image = (String) biggestImageBlock.get("src");
    }
}
