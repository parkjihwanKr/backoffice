package com.example.backoffice.domain.file.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;

public class FilesConverter {

    // entity : id, url, member, board
    public static Files toEntityForMemberRole(String url, Members member) {
        return Files.builder()
                .url(url)
                .member(member)
                .board(null)
                .build();
    }

    public static Files toEntityForBoards(String url, Boards board) {
        return Files.builder()
                .url(url)
                .member(board.getMember())
                .board(board)
                .build();
    }
}
