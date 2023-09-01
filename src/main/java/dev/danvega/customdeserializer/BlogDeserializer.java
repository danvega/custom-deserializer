package dev.danvega.customdeserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@JsonComponent
public class BlogDeserializer extends JsonDeserializer<Blog> {

    private static final String DATE_PATTERN = "MM/dd/yyyy";

    @Override
    public Blog deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        JsonNode edges = jsonNode.get("data").get("allPost").get("edges");
        List<Post> posts = new ArrayList<>();
        if(edges.isArray()) {
            for(JsonNode edge : edges) {
                JsonNode node = edge.get("node");
                Post post = new Post(
                        node.get("id").asText(),
                        node.get("title").asText(),
                        node.get("slug").asText(),
                        extractDate(node),
                        node.get("timeToRead").asInt(),
                        extractTags(node)
                );

                posts.add(post);
            }
        }
        return new Blog(posts);
    }

    /**
     * Extracts the date from the given JsonNode and returns it as a LocalDate.
     *
     * @param node The JsonNode containing the date.
     * @return A LocalDate.
     */
    private LocalDate extractDate(JsonNode node) {
        return LocalDate.parse(node.get("date").asText(), DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Extracts tags from the given JsonNode and returns them as a comma-separated string.
     *
     * @param node The JsonNode containing the tags.
     * @return A comma-separated string of tag titles.
     */
    private String extractTags(JsonNode node) {

        if (node == null || !node.has("tags")) {
            return "";
        }

        JsonNode tags = node.get("tags");
        StringJoiner joiner = new StringJoiner(", ");

        if (tags.isArray()) {
            for (JsonNode tag : tags) {
                JsonNode titleNode = tag.get("title");
                if (titleNode != null) {
                    joiner.add(titleNode.asText());
                }
            }
        }

        return joiner.toString();
    }


}
