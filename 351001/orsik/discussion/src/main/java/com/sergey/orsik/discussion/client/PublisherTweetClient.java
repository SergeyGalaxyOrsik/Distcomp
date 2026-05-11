package com.sergey.orsik.discussion.client;

import com.sergey.orsik.discussion.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class PublisherTweetClient {

    private static final String TWEET_PATH = "/api/v1.0/tweets/{id}";

    private final RestClient publisherRestClient;

    public PublisherTweetClient(RestClient publisherRestClient) {
        this.publisherRestClient = publisherRestClient;
    }

    public PublisherTweetDto fetchTweet(long tweetId) {
        try {
            PublisherTweetDto dto = publisherRestClient.get()
                    .uri(TWEET_PATH, tweetId)
                    .retrieve()
                    .body(PublisherTweetDto.class);
            if (dto == null || dto.creatorId() == null) {
                throw new EntityNotFoundException("Tweet", tweetId);
            }
            return dto;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new EntityNotFoundException("Tweet", tweetId);
        }
    }

    public void requireTweetExists(Long tweetId) {
        fetchTweet(tweetId);
    }
}
