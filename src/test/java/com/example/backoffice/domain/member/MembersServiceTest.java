package com.example.backoffice.domain.member;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.repository.MembersRepository;
import com.example.backoffice.domain.member.service.MembersServiceImplV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class MembersServiceTest {

    @InjectMocks
    private MembersServiceImplV1 membersService;

    @Mock
    private MembersRepository membersRepository;

    private Members mainAdmin;

    private Members memberOne;

    private Members memberTwo;

    private Members memberThree;

    private Members memberFour;

    @BeforeEach
    public void setUp(){
        mainAdmin = Members.builder()
                .id(1L)
                .name("mainAdmin")
                .password("12341234")
                .memberName("mainAdmin")
                .email("mainAdmin@naver.com")
                .contact("010-1111-1212")
                .profileImageUrl("profile111.png")
                .address("여기 살아요")
                .role(MemberRole.MAIN_ADMIN)
                .department(MemberDepartment.HR)
                .position(MemberPosition.CEO)
                .remainingVacationDays(4)
                .salary(207000000L)
                .onVacation(false)
                .build();

        memberOne = Members.builder()
                .id(2L)
                .name("memberOne")
                .password("123412341")
                .memberName("memberOne")
                .email("memberOne@naver.com")
                .contact("010-1111-1212")
                .profileImageUrl("profile.png")
                .role(MemberRole.ADMIN)
                .department(MemberDepartment.HR)
                .position(MemberPosition.MANAGER)
                .address("여기 살껄요?")
                .remainingVacationDays(4)
                .salary(77000000L)
                .onVacation(false)
                .build();

        memberTwo = Members.builder()
                .id(3L)
                .name("memberTwo")
                .password("12341234")
                .memberName("memberTwo")
                .email("memberTwo@naver.com")
                .contact("010-2222-1212")
                .profileImageUrl("profile2.png")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.HR)
                .position(MemberPosition.ASSISTANT_MANAGER)
                .address("여기 살껄?")
                .remainingVacationDays(4)
                .salary(67000000L)
                .onVacation(false)
                .build();

        memberThree = Members.builder()
                .id(4L)
                .name("memberThree")
                .password("123412341234")
                .memberName("memberThree")
                .email("memberThree@naver.com")
                .contact("010-2222-1313")
                .profileImageUrl("profile3.png")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.IT)
                .position(MemberPosition.INTERN)
                .address("여기 살껄요?")
                .remainingVacationDays(4)
                .salary(35000000L)
                .onVacation(false)
                .build();

        memberFour = Members.builder()
                .id(4L)
                .name("memberFour")
                .password("1234444234")
                .memberName("memberFour")
                .email("memberFour@naver.com")
                .contact("010-4444-1313")
                .profileImageUrl("profile4.png")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.SALES)
                .position(MemberPosition.INTERN)
                .address("여기 살껄요요?")
                .remainingVacationDays(4)
                .salary(35000000L)
                .onVacation(false)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("findById Success")
    public void findByIdSuccess(){
        // given
        when(membersRepository.findById(memberOne.getId())).thenReturn(Optional.of(memberOne));
        // when
        Members foundMember = membersService.findById(2L);
        // then
        assertEquals(foundMember.getId(), 2L);
    }

    @Test
    @Order(2)
    @DisplayName("findById Exception : NOT_FOUND_MEMBER")
    public void findByIdNotFoundMember(){
        // when
        MembersCustomException e
                = assertThrows(MembersCustomException.class,
                ()-> membersService.findById(memberOne.getId()));

        // then
        assertEquals(e.getMessage(), MembersExceptionCode.NOT_FOUND_MEMBER.getMessage());
    }
    @Test
    @Order(3)
    @DisplayName("checkDifferentMember Success")
    public void checkDifferentMemberSuccess(){
        // given
        when(membersRepository.findById(memberTwo.getId())).thenReturn(Optional.of(memberTwo));
        // when
        Members checkedMember
                = membersService.checkDifferentMember(memberOne.getId(), memberTwo.getId());
        // then
        assertEquals(memberTwo.getMemberName(), checkedMember.getMemberName());
    }

    @Test
    @Order(4)
    @DisplayName("checkDifferentMember Exception : MATCHED_LOGIN_MEMBER")
    public void checkDifferentMemberMatchedLoginMember(){
        // given
        Long sameMemberOneId = 2L;
        // when
        MembersCustomException e
                = assertThrows(MembersCustomException.class,
                ()-> membersService.checkDifferentMember(memberOne.getId(), sameMemberOneId));
        // then
        assertEquals(e.getMessage(), MembersExceptionCode.MATCHED_LOGIN_MEMBER.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("checkDifferentMember Exception : NOT_FOUND_MEMBER")
    public void checkDifferentMemberNotFoundMember(){
        // repository에 저장이 되어있다는 when().thenReturn문이 없기에
        // repository에 해당 member 정보가 저장되어져 있지 않다는 설정
        // when
        MembersCustomException e
                = assertThrows(MembersCustomException.class,
                ()-> membersService.checkDifferentMember(memberOne.getId(), memberTwo.getId()));
        // then
        assertEquals(e.getMessage(), MembersExceptionCode.NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("findByEmailOrMemberNameOrAddressOrContact Success")
    public void findByEmailOrMemberNameOrAddressOrContactSuccess(){
        // given
        when(membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                memberOne.getEmail(), memberOne.getMemberName(),
                memberOne.getAddress(), memberOne.getContact()
        )).thenReturn(Optional.of(memberOne));

        // when
        Members foundMember
                = membersService.findByEmailOrMemberNameOrAddressOrContact(
                        memberOne.getEmail(), memberOne.getMemberName(),
                memberOne.getAddress(), memberOne.getContact());

        // then
        assertEquals(memberOne.getEmail(), foundMember.getEmail());
        assertEquals(memberOne.getMemberName(), foundMember.getMemberName());
        assertEquals(memberOne.getAddress(), foundMember.getAddress());
        assertEquals(memberOne.getContact(), foundMember.getContact());
    }

    @Test
    @Order(7)
    @DisplayName("existsById Success")
    public void existsByIdSuccess(){
        // given
        when(membersRepository.existsById(memberOne.getId())).thenReturn(true);
        // when
        Boolean isTrue = membersService.existsById(memberOne.getId());
        // then
        assertTrue(isTrue);
    }

    @Test
    @Order(8)
    @DisplayName("save && signup Success")
    public void saveSuccess(){
        // given
        when(membersRepository.save(memberOne)).thenReturn(memberOne);

        // when
        Members saveMember = membersService.save(memberOne);

        // then
        verify(membersRepository).save(memberOne);
        assertEquals(memberOne.getMemberName(), saveMember.getMemberName());
        assertEquals(memberOne.getId(), saveMember.getId());
        assertEquals(memberOne.getEmail(), saveMember.getEmail());
        assertEquals(memberOne.getName(), saveMember.getName());
    }

    @Test
    @Order(9)
    @DisplayName("deleteById Success")
    public void deleteByIdSuccess(){
        // given
        Long memberId = 2L;
        // when
        membersService.deleteById(memberId);
        // then
        verify(membersRepository).deleteById(memberId);
    }

    @Test
    @Order(10)
    @DisplayName("findAllById Success")
    public void findAllByIdSuccess(){
        // given
        List<Long> foundIdList
                = List.of(mainAdmin.getId(), memberOne.getId(), memberTwo.getId());

        when(membersRepository.findAllById(foundIdList))
                .thenReturn(List.of(mainAdmin, memberOne, memberTwo));

        // when
        List<Members> foundMemberList = membersService.findAllById(foundIdList);

        // then
        assertTrue(foundMemberList.containsAll(List.of(mainAdmin, memberOne, memberTwo)));
    }

    @Test
    @Order(11)
    @DisplayName("findByDepartmentNotInAndIdNotIn Success")
    public void findByDepartmentNotInAndIdNotInSuccess(){
        // given
        List<MemberDepartment> excludedDepartmentList = List.of(MemberDepartment.IT);
        List<Long> excludedIdList = List.of(1L);

        when(membersRepository.findByDepartmentNotInAndIdNotIn(excludedDepartmentList, excludedIdList))
                .thenReturn(List.of(memberOne, memberTwo));

        // when
        List<Members> memberList
                = membersService.findByDepartmentNotInAndIdNotIn(
                        excludedDepartmentList, excludedIdList);

        // then
        assertEquals(2, memberList.size());
        assertEquals(2L, memberList.get(0).getId());
        assertEquals(3L, memberList.get(1).getId());
    }

    @Test
    @Order(12)
    @DisplayName("findByIdAndRoleAndDepartment Success")
    public void findByIdAndRoleAndDepartmentSuccess() {
        // given
        Long foundId = memberOne.getId();
        MemberRole foundRole = memberOne.getRole();
        MemberDepartment foundDepartment = memberOne.getDepartment();

        when(membersRepository.findByIdAndRoleAndDepartment(foundId, foundRole, foundDepartment))
                .thenReturn(Optional.of(memberOne));
        // when
        Members foundMember
                = membersService.findByIdAndRoleAndDepartment(foundId, foundRole, foundDepartment);

        // then
        assertEquals(foundMember.getId(), foundId);
    }

    @Test
    @Order(13)
    @DisplayName("findHRManager Success")
    public void findHRManagerSuccess(){
        // given
        when(membersRepository.findByPositionAndDepartment(
                MemberPosition.MANAGER, MemberDepartment.HR))
                .thenReturn(Optional.of(memberOne));
        // when
        Members hrManager = membersService.findHRManager();
        // then
        assertEquals(memberOne.getDepartment(), hrManager.getDepartment());
        assertEquals(memberOne.getPosition(), hrManager.getPosition());
    }

    @Test
    @Order(14)
    @DisplayName("findHRManager Exception : NOT_FOUND_HR_MANAGER")
    public void findHRManagerNotFoundHRManager(){
        // given
        when(membersRepository.findByPositionAndDepartment(
                MemberPosition.MANAGER, MemberDepartment.HR))
                .thenReturn(Optional.empty());
        // when
        MembersCustomException e
                = assertThrows(MembersCustomException.class, ()-> membersService.findHRManager());
        // then
        assertEquals(e.getMessage(), MembersExceptionCode.NOT_FOUND_HR_MANAGER.getMessage());
    }

    @Test
    @Order(15)
    @DisplayName("findMemberTotalCount Success")
    public void findMemberTotalCountSuccess(){
        // given
        Long expectedTotalCount = 4L;
        when(membersRepository.count()).thenReturn(expectedTotalCount);
        // when
        Long memberTotalCount = membersService.findMemberTotalCount();
        // then
        assertEquals(memberTotalCount, expectedTotalCount);
    }

    @Test
    @Order(16)
    @DisplayName("findByMemberName Success")
    public void findByMemberNameSuccess(){
        // given
        String foundMemberName = memberOne.getMemberName();
        when(membersRepository.findByMemberName(foundMemberName))
                .thenReturn(Optional.of(memberOne));
        // when
        Members foundMember = membersService.findByMemberName(foundMemberName);
        // then
        assertEquals(foundMember.getMemberName(), foundMemberName);
    }

    @Test
    @Order(17)
    @DisplayName("findByMemberName Exception : NOT_FOUND_MEMBER")
    public void findByMemberNameNotFoundMember(){
        // given
        String foundMemberName = memberOne.getMemberName();
        when(membersRepository.findByMemberName(foundMemberName))
                .thenReturn(Optional.empty());
        // when
        MembersCustomException e = assertThrows(
                MembersCustomException.class,
                () -> membersService.findByMemberName(foundMemberName));

        // then
        assertEquals(e.getMessage(), MembersExceptionCode.NOT_FOUND_MEMBER.getMessage());
    }

    @Test
    @Order(18)
    @DisplayName("findAll Success")
    public void findAllSuccess(){
        // given
        when(membersRepository.findAll())
                .thenReturn(List.of(mainAdmin, memberOne, memberTwo, memberThree));

        // when
        List<Members> memberList = membersService.findAll();

        // then
        assertEquals(memberList.size(), 4);
        assertEquals(memberList.get(0).getId(), mainAdmin.getId());
        assertEquals(memberList.get(1).getId(), memberOne.getId());
        assertEquals(memberList.get(2).getId(), memberTwo.getId());
        assertEquals(memberList.get(3).getId(), memberThree.getId());
    }

    @Test
    @Order(19)
    @DisplayName("findAllByDepartment Success")
    public void findAllByDepartmentSuccess(){
        // given
        when(membersRepository.findAllByDepartment(MemberDepartment.HR))
                .thenReturn(List.of(mainAdmin, memberOne, memberTwo));

        // when
        List<Members> hrDepartmentMemberList = membersService.findAllByDepartment(MemberDepartment.HR);

        // then
        assertEquals(hrDepartmentMemberList.size(), 3);
        assertEquals(hrDepartmentMemberList.get(0).getDepartment(), mainAdmin.getDepartment());
        assertEquals(hrDepartmentMemberList.get(1).getDepartment(), memberOne.getDepartment());
        assertEquals(hrDepartmentMemberList.get(2).getDepartment(), memberTwo.getDepartment());
        assertEquals(hrDepartmentMemberList.get(0).getId(), mainAdmin.getId());
        assertEquals(hrDepartmentMemberList.get(1).getId(), memberOne.getId());
        assertEquals(hrDepartmentMemberList.get(2).getId(), memberTwo.getId());
    }

    @Test
    @Order(20)
    @DisplayName("findAllByPosition Success")
    public void findAllByPositionSuccess(){
        // given
        when(membersRepository.findAllByPosition(MemberPosition.INTERN))
                .thenReturn(List.of(memberThree, memberFour));
        // when
        List<Members> samePositionMemberList
                = membersService.findAllByPosition(MemberPosition.INTERN);

        // then
        assertEquals(samePositionMemberList.size(), 2);
        assertEquals(samePositionMemberList.get(0).getPosition(), memberThree.getPosition());
        assertEquals(samePositionMemberList.get(1).getPosition(), memberFour.getPosition());
        assertEquals(samePositionMemberList.get(0).getId(), memberThree.getId());
        assertEquals(samePositionMemberList.get(1).getId(), memberFour.getId());
    }

    @Test
    @Order(21)
    @DisplayName("findAllExceptLoginMember Success")
    public void findAllExceptLoginMemberSuccess(){
        // given
        Long loginMemberId = mainAdmin.getId();
        when(membersRepository.findAllByIdNotIn(Collections.singletonList(loginMemberId)))
                .thenReturn(List.of(memberOne, memberTwo, memberThree, memberFour));

        // when
        List<Members> memberListExceptLoginMember = membersService.findAllExceptLoginMember(loginMemberId);

        // then
        assertEquals(memberListExceptLoginMember.size(), 4);
        assertEquals(memberListExceptLoginMember.get(0).getId(), memberOne.getId());
        assertEquals(memberListExceptLoginMember.get(1).getId(), memberTwo.getId());
        assertEquals(memberListExceptLoginMember.get(2).getId(), memberThree.getId());
        assertEquals(memberListExceptLoginMember.get(3).getId(), memberFour.getId());
    }
}
