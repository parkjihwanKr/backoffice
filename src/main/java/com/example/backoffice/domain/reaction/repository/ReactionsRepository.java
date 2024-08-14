package com.example.backoffice.domain.reaction.repository;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.entity.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionsRepository extends JpaRepository<Reactions, Long> {

    boolean existsByMemberAndReactor(
            Members toMember, Members fromMember);

    boolean existsByIdAndMemberAndReactor(
            Long reactionId, Members toMember, Members fromMember);

    boolean existsByBoardAndReactor(
            Boards board, Members fromMember);

    boolean existsByIdAndBoardAndReactor(
            Long reactionId, Boards board, Members fromMember);

    boolean existsByCommentAndReactor(
            Comments comment, Members fromMember);

    boolean existsByIdAndCommentAndReactor(
            Long reactionId, Comments commentOrReply, Members fromMember);

}
