package com.example;

import com.example.service.BookAuthorClientService;
import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Descriptor;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {
    BookAuthorClientService bookAuthorClientService;
    @GetMapping("/author/{id}")
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable String id){
    return bookAuthorClientService.getAuthor(Integer.parseInt(id));
    }
    @GetMapping("book/{authorId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(@PathVariable String authorId) throws InterruptedException {
        return bookAuthorClientService.getBooksByAuthor(Integer.parseInt(authorId));
    }
}