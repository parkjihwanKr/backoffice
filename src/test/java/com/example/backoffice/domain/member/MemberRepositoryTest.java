package com.example.backoffice.domain.member;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.repository.MembersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class MemberRepositoryTest {

    @Autowired
    private MembersRepository membersRepository;

    @BeforeEach
    void setUp(){
        Members member = Members.builder()
                .id(1L)
                .memberName("notLoginMember")
                .password("12341234")
                .email("test1@naver.com")
                .build();

        membersRepository.save(member);
    }

    @Test
    @DisplayName("setUp test success")
    public void setUpSuccess(){
        // given -> 데이터
        Optional<Members> member = membersRepository.findById(1L);
        // when -> 증명하고 싶은 메서드
        // then -> 증명 과정
        Assertions.assertThat(1L).isEqualTo(member.get().getId());
    }

    @Test
    @DisplayName("findByMemberName success")
    public void findByMemberNameSuccess(){
        // given
        String adminName = membersRepository.findById(1L).orElseThrow().getMemberName();
        // when
        Optional<Members> admin = membersRepository.findByMemberName(adminName);
        // then
        Assertions.assertThat(adminName).isEqualTo(admin.get().getMemberName());
    }
}
