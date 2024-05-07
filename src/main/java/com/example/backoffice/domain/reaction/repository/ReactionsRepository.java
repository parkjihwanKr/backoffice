package com.example.backoffice.domain.reaction.repository;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionsRepository extends JpaRepository<Reactions, Long> {

    boolean existsByMemberAndReactorAndEmoji(
            Members toMember, Members fromMember, Emoji emoji);

    boolean existsByIdAndMemberAndReactor(
            Long reactionId, Members toMember, Members fromMember);

    boolean existsByBoardAndReactorAndEmoji(
            Boards board, Members fromMember, Emoji emoji);

    boolean existsByIdAndBoardAndReactor(
            Long reactionId, Boards board, Members fromMember);

    boolean existsByCommentAndReactorAndEmoji(
            Comments comment, Members fromMember, Emoji emoji);

    boolean existsByIdAndCommentAndReactor(
            Long reactionId, Comments comment, Members fromMember);
}
