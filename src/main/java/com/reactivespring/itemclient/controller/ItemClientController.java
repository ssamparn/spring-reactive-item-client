package com.reactivespring.itemclient.controller;

import com.reactivespring.itemclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/retrieve/allItems")
    public Flux<Item> getAllItemsUsingRetrieve() {

        Flux<Item> itemFlux = webClient.get()
                .uri("/items")
                .retrieve()
                .bodyToFlux(Item.class);

        return itemFlux;

    }

    @GetMapping("/exchange/allItems")
    public Flux<Item> getAllItemsUsingExchnage() {

        Flux<Item> itemFlux = webClient.get()
                .uri("/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class));

        return itemFlux;

    }

    @GetMapping("/retrieve/singleItem")
    public Mono<Item> retriveAnItem() {
        String id = "ExistingId";
        return webClient.get()
                .uri("/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class);
    }

    @GetMapping("/exchange/singleItem")
    public Mono<Item> exchangeAnItem() {
        String id = "ExistingId";
        return webClient.get()
                .uri("/items/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class));
    }

    @PostMapping("/create/item")
    public Mono<Item> createItem(@RequestBody Item item) {

        Mono<Item> createdItemMono = webClient.post()
                .uri("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class);

        return createdItemMono;
    }

    @PutMapping("/update/item/{itemId}")
    public Mono<Item> updateItem(@RequestBody Item item, @PathVariable String itemId) {

        Mono<Item> updatedItemMono = webClient.put()
                .uri("/items/{itemId}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class);

        return updatedItemMono;
    }

    @DeleteMapping("/delete/item/{itemId}")
    public Mono<Void> deleteItem(@PathVariable String itemId) {

        return webClient.delete()
                .uri("/items/{itemId}", itemId)
                .retrieve()
                .bodyToMono(Void.class);

    }

}
