package com.example.backoffice.domain.reaction.repository;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.entity.Emoji;
import com.example.backoffice.domain.reaction.entity.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionsRepository extends JpaRepository<Reactions, Long> {

    boolean existsByIdAndMemberAndReactor(
            Long reactionId, Members toMember, Members fromMember);
    boolean existsByMemberAndReactorAndEmoji(
            Members toMember, Members fromMember, Emoji emoji);
}
