package com.example.service;

import com.example.Author;
import com.example.Book;
import com.example.BookAuthorServiceGrpc;
import com.google.protobuf.Descriptors;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class BookAuthorClientService {
    @GrpcClient("grpc-devproblems-service")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-devproblems-service")
    BookAuthorServiceGrpc.BookAuthorServiceStub asynchornousClient;
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId){
      Author authorRequest =   Author.newBuilder().setAuthorId(authorId).build();
      Author authorResponse =synchronousClient.getAuthor(authorRequest);
      return authorResponse.getAllFields();
    };

    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(int autherId) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Author authorRequest = Author.newBuilder().setAuthorId(autherId).build();
        final List<Map<Descriptors.FieldDescriptor,Object>> response = new ArrayList<>();
        asynchornousClient.getBooksByAuthor(authorRequest, new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                response.add(book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
            countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
       boolean await =  countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }
}
