package com.example;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BookAuthrorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase{
    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        TempDb
                .getAuthorsFromTempDb()
                .stream().filter(author -> author.getAuthorId() == request.getAuthorId()).
                findFirst().
                ifPresent(author ->{
                    Author response = Author.newBuilder()
                                    .setAuthorId(author.getAuthorId())
                            .setFirstName(author.getFirstName())
                            .setLastName(author.getLastName())
                            .setGender(author.getGender())
                            .setBookId(author.getBookId())
                            .build();
                    responseObserver.onNext(response);

                });
        responseObserver.onCompleted();
    }

    @Override
    public void getBooksByAuthor(Author request, StreamObserver<Book> responseObserver) {
        TempDb.getBooksFromTempDb()
                .stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}