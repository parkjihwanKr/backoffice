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
                .address("어딘시 어딘구")
                .contact("010-2222-3333")
                .build();

        membersRepository.save(member);
    }

    @Test
    @DisplayName("setUp Success")
    public void setUpTestSuccess(){
        // given -> 데이터
        Optional<Members> member = membersRepository.findById(1L);
        // when -> 증명하고 싶은 메서드
        // then -> 증명 과정
        Assertions.assertThat(1L).isEqualTo(member.get().getId());
    }

    @Test
    @DisplayName("save Success")
    public void saveSuccess(){
        // given
        String memberName = "parkjihwan";
        String password = "12341234";
        Members member = Members.builder()
                .id(2L)
                .name(memberName)
                .password(password)
                .build();
        // when
        Members saveMember = membersRepository.save(member);
        // then
        Assertions.assertThat(saveMember.getName()).isEqualTo(memberName);
        Assertions.assertThat(saveMember.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("findByMemberName Success")
    public void findByMemberNameSuccess(){
        // given
        String memberName = "testtest";
        Members member = Members.builder()
                .id(2L)
                .memberName(memberName)
                .build();
        membersRepository.save(member);

        // when
        membersRepository.findByMemberName(memberName);

        // then
        Assertions.assertThat(member.getMemberName()).isEqualTo(memberName);
    }

    @Test
    @DisplayName("findByEmailOrMemberNameOrAddressOrContact Success")
    public void findByEmailOrMemberNameOrAddressOrContactSuccess(){
        // given
        String address = "어딘시 어딘구";
        String email = "test1@naver.com";
        String memberName = "notLoginMember";
        String contact = "010-2222-3333";

        // when
        Optional<Members> member
                = membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                        email, memberName, address, contact);

        // then
        Assertions.assertThat(member.get().getMemberName()).isEqualTo(memberName);
        Assertions.assertThat(member.get().getEmail()).isEqualTo(email);
        Assertions.assertThat(member.get().getContact()).isEqualTo(contact);
        Assertions.assertThat(member.get().getAddress()).isEqualTo(address);
    }
}
