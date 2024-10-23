package com.example.backoffice.domain.file.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;

public class FilesConverter {

    // entity : id, url, member, board
    public static Files toEntityForMemberRole(String url, Members member) {
        return Files.builder()
                .url(url)
                .member(member)
                .board(null)
                .event(null)
                .build();
    }

    public static Files toEntityForBoards(String url, Boards board) {
        return Files.builder()
                .url(url)
                .member(board.getMember())
                .board(board)
                .event(null)
                .build();
    }

    public static Files toEntityForEvents(String url, Events event){
        return Files.builder()
                .url(url)
                .member(event.getMember())
                .event(event)
                .board(null)
                .build();
    }

    public static Files toEntityForExpense(
            String url, Expense expense, Members owner){
        return Files.builder()
                .url(url)
                .member(owner)
                .event(null)
                .board(null)
                .build();
    }

    public static FilesResponseDto.ReadOneDto toReadOneDto(Files file){
        return FilesResponseDto.ReadOneDto.builder()
                .id(file.getId())
                .url(file.getUrl())
                .build();
    }
}
