package dev.danvega.customdeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public record Blog(List<Post> posts) {
}
