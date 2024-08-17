package com.example.backoffice.domain.member;

import com.example.backoffice.domain.file.service.FilesServiceImplV1;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1;
import com.example.backoffice.domain.member.service.MembersServiceImplV1;
import com.example.backoffice.domain.notification.entity.Notifications;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.global.security.MemberDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class MembersServiceFacadeTest {

    /*method list
    * // createOneForAdmin, createOneForSignup, readOne, updateOne,
    * updateOneForAttribute, updateOneForSalary, updateOneForProfileImage
    * */

    private MemberDetailsImpl memberDetails;

    @InjectMocks
    private MembersServiceFacadeImplV1 membersServiceFacadeImpl;

    @Mock
    private MembersServiceImplV1 membersService;

    @Mock
    private FilesServiceImplV1 filesService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationsServiceV1 notificationsService;

    @BeforeEach
    public void setLoginUser(){
        memberDetails = new MemberDetailsImpl(
                Members.builder()
                        .id(2L)
                        .name("test User Name")
                        .password("12341234")
                        .memberName("test User NickName")
                        .email("test1234@naver.com")
                        .contact("010-2222-1212")
                        .profileImageUrl("profile.png")
                        .role(MemberRole.EMPLOYEE)
                        .department(MemberDepartment.HR)
                        .position(MemberPosition.ASSISTANT_MANAGER)
                        .remainingVacationDays(4)
                        .salary(57000000L)
                        .onVacation(false)
                        .build());

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                        memberDetails, memberDetails.getPassword(),
                        memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("MemberDetails Initialization Test")
    public void testMemberDetailsInitialization() {

        assertNotNull(memberDetails, "MemberDetails should not be null");
        Members members = memberDetails.getMembers();
        Long memberId = memberDetails.getMembers().getId();
        System.out.println(memberId);
        assertNotNull(members, "Members inside MemberDetails should not be null");
        assertEquals(2L, members.getId(), "Member ID should be 2L");
        assertEquals("test User Name", members.getName(), "Member name should be 'test User Name'");
        assertEquals("test User NickName", members.getMemberName(), "Member nickname should be 'test User NickName'");
        assertEquals("12341234", members.getPassword(), "Member password should be '12341234'");
        assertEquals("010-2222-1212", members.getContact(), "Member contact should be '010-2222-1212'");
        assertEquals(MemberRole.EMPLOYEE, members.getRole(), "Member role should be EMPLOYEE");
        assertEquals(MemberDepartment.HR, members.getDepartment(), "Member department should be HR");
        assertEquals(MemberPosition.ASSISTANT_MANAGER, members.getPosition(), "Member position should be ASSISTANT_MANAGER");
    }
    @Test
    @Order(1)
    @DisplayName("signup Success")
    public void signupSuccess(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");

        // when
        MembersResponseDto.CreateOneDto responseDto =
                membersServiceFacadeImpl.createOneForSignup(requestDto);

        // then
        verify(membersService, times(1)).signup(any(Members.class));
        assertEquals(requestDto.getMemberName(), responseDto.getMemberName());
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getContact(), responseDto.getContact());
    }

    @Test
    @Order(2)
    @DisplayName("signup Exception : NOT_MATCHED_PASSWORD")
    public void signupNotMatchedPassword(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341231")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        // 밑에 해당하는 encode 작업이 일어나기 전에 Exception 터지는데, 불필요한 메서드
        // given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersServiceFacadeImpl.createOneForSignup(requestDto));

        // then
        // Exception
        // 1. 예외가 터지는 파라미터 검증
        // 2. 예외 메세지 검증
        // 3. 예외가 터진 코드 밑의 코드가 일어나는지?
        assertNotEquals(requestDto.getPasswordConfirm(), requestDto.getPassword());
        assertEquals(MembersExceptionCode.NOT_MATCHED_PASSWORD.getMessage(),
                e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @Order(3)
    @DisplayName("signup Exception : MATCHED_MEMBER_INFO_EMAIL")
    public void signupMatchedMemberInfoEmail(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        Members existingMember = Members.builder()
                .name("testMemberName123")
                .memberName("testIdd")
                .address("어디시 어딘구 어딘가요")
                .email("test@naver.com")
                .contact("010-1211-1111")
                .build();

        when(membersService.findByEmailOrMemberNameOrAddressOrContact(
                anyString(), anyString(), anyString(), anyString())).thenReturn(existingMember);

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersServiceFacadeImpl.createOneForSignup(requestDto));

        // then
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_EMAIL.getMessage(), e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersService, never()).save(any(Members.class));
    }

    @Test
    @Order(4)
    @DisplayName("signup Exception : MATCHED_MEMBER_INFO_ADDRESS")
    public void signupMatchedMemberAddressInfo(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        Members existingMember = Members.builder()
                .name("testMemberName123")
                .memberName("testIdd")
                .address("어디시 어딘구 어딘가")
                .email("testdd@naver.com")
                .contact("010-1211-1111")
                .build();

        when(membersService.findByEmailOrMemberNameOrAddressOrContact(
                anyString(), anyString(), anyString(), anyString())).thenReturn(existingMember);

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersServiceFacadeImpl.createOneForSignup(requestDto));

        // then
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_ADDRESS.getMessage(), e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersService, never()).save(any(Members.class));
    }

    @Test
    @Order(5)
    @DisplayName("signup Exception : MATCHED_MEMBER_INFO_CONTACT")
    public void signupMatchedMemberInfoContact(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        Members existingMember = Members.builder()
                .name("testMemberName123")
                .memberName("testIdd")
                .address("어디시 어딘구 어딘가요")
                .email("testdd@naver.com")
                .contact("010-1111-1111")
                .build();

        when(membersService.findByEmailOrMemberNameOrAddressOrContact(
                anyString(), anyString(), anyString(), anyString())).thenReturn(existingMember);

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersServiceFacadeImpl.createOneForSignup(requestDto));

        // then
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_CONTACT.getMessage(), e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersService, never()).save(any(Members.class));
    }

    @Test
    @Order(6)
    @DisplayName("signup Exception : MATCHED_MEMBER_INFO_MEMBER_NAME")
    public void signupMatchedMemberInfoMemberName(){
        // given
        MembersRequestDto.CreateOneDto requestDto =
                MembersRequestDto.CreateOneDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        Members existingMember = Members.builder()
                .name("testMemberName123")
                .memberName("testId")
                .address("어디시 어딘구 어딘가요")
                .email("testdd@naver.com")
                .contact("010-1211-1111")
                .build();

        when(membersService.findByEmailOrMemberNameOrAddressOrContact(
                anyString(), anyString(), anyString(), anyString())).thenReturn(existingMember);

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersServiceFacadeImpl.createOneForSignup(requestDto));

        // then
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_MEMBER_NAME.getMessage(), e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersService, never()).save(any(Members.class));
    }

    @Test
    @Order(7)
    @DisplayName("readOne Success")
    public void readOneSuccess(){
        // when
        Members member = memberDetails.getMembers();
        Long memberId = 2L;

        // 내부 메서드인 matchedMember에 해당하는 메서드를 부르지 않고
        // 내부 메서드에 존재하는 findById를 통해 검증
        when(membersService.findById(memberId)).thenReturn(member);

        MembersResponseDto.ReadOneDto responseDto =
                membersServiceFacadeImpl.readOne(memberId, member);

        // then
        assertEquals(responseDto.getMemberName(), memberDetails.getUsername());
        assertEquals(responseDto.getRole(), memberDetails.getMembers().getRole());
    }

    @Test
    @Order(8)
    @DisplayName("readOne Exception : NOT_MATCHED_INFO")
    public void readOneNotMatchedInfo(){
        // given
        Members loginMember = memberDetails.getMembers();
        Long accessedMemberId = 1L;

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.readOne(accessedMemberId, loginMember));
        // then
        assertEquals(
                MembersExceptionCode.NOT_MATCHED_INFO.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("updateOne Success")
    public void updateOneSuccess(){
        // given
        MembersRequestDto.UpdateOneDto requestDto =
                MembersRequestDto.UpdateOneDto.builder()
                        // memberName 중복 x, email 중복 x, contact 중복 x
                        .name("updateName")
                        .memberName("test User NickName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .email("test12@naver.com")
                        .address("경기도 고양시 어딘가 어딘구")
                        .contact("010-2222-3333")
                        .introduction("hello!")
                        .build();

        given(passwordEncoder.encode(requestDto.getPassword()))
                .willReturn("encodedPassword");

        MultipartFile image = getMultipartFile();

        // when
        when(membersServiceFacadeImpl.matchLoginMember(
                memberDetails.getMembers(), 2L)).thenReturn(memberDetails.getMembers());
        MembersResponseDto.UpdateOneDto responseDto =
                membersServiceFacadeImpl.updateOne(
                        2L, memberDetails.getMembers(), image, requestDto);

        // then
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getName(), responseDto.getName());
        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getContact(), responseDto.getContact());

    }

    // updateOne의 matchedLoginMember Exception 상황
    @Test
    @Order(10)
    @DisplayName("matchedLoginMember Exception : NOT_MATCHED_INFO " +
            "case LoginMemberName is not equals to RequestDto's memberName")
    public void matchedLoginMemberNotMatchedMemberName(){
        // given
        Long notMatchedMemberId = 1L;
        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.matchLoginMember(memberDetails.getMembers(), notMatchedMemberId));
        // then
        assertEquals(
                MembersExceptionCode.NOT_MATCHED_INFO.getMessage(), e.getMessage());
    }

    @Test
    @Order(11)
    @DisplayName("updateOne Exception : NOT_MATCHED_PASSWORD")
    public void updateOneNotMatchedPassword(){
        // given
        MembersRequestDto.UpdateOneDto requestDto =
                MembersRequestDto.UpdateOneDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("Not Matched Password")
                        .memberName("test User NickName")
                        .address("updated Address")
                        .email("updated@naver.com")
                        .contact("010-1111-1111")
                        .build();

        MultipartFile image = getMultipartFile();
        when(membersServiceFacadeImpl.
                matchLoginMember(memberDetails.getMembers(), 2L))
                .thenReturn(memberDetails.getMembers());

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.updateOne(
                        2L, memberDetails.getMembers(), image, requestDto));

        // then
        assertEquals(requestDto.getMemberName(), memberDetails.getMembers().getMemberName());
        assertNotEquals(requestDto.getPassword(), requestDto.getPasswordConfirm());
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(
                MembersExceptionCode.NOT_MATCHED_PASSWORD.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(12)
    @DisplayName("updateOne Exception : MATCHED_MEMBER_INFO_EMAIL")
    public void updateOneMatchedEmail(){
        // given
        Members matchedEmailMember = Members.builder()
                .id(10L)
                .name("anotherName")
                .password("123412341234")
                .memberName("anotherMemberName")
                .email("test1234@naver.com")
                .contact("010-2122-1212")
                .profileImageUrl(" ")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.SALES)
                .position(MemberPosition.INTERN)
                .remainingVacationDays(4)
                .salary(57000000L)
                .onVacation(false)
                .build();

        String matchedEmail = matchedEmailMember.getEmail();

        MembersRequestDto.UpdateOneDto requestDto =
                MembersRequestDto.UpdateOneDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("test User NickName")
                        .address("updated Address")
                        .email(matchedEmail)
                        .contact("010-1111-1111")
                        .build();

        MultipartFile image = getMultipartFile();

        // when
        when(membersServiceFacadeImpl.
                matchLoginMember(memberDetails.getMembers(), 2L))
                .thenReturn(memberDetails.getMembers());
        when(membersService.findAllExceptLoginMember(memberDetails.getMembers().getId())).thenReturn(
                List.of(memberDetails.getMembers(), matchedEmailMember));

        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.updateOne(
                        2L, memberDetails.getMembers(), image, requestDto));

        // then
        assertEquals(requestDto.getMemberName(), memberDetails.getMembers().getMemberName());
        assertEquals(requestDto.getPassword(), requestDto.getPasswordConfirm());
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_EMAIL.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("updateOne Exception : MATCHED_MEMBER_INFO_CONTACT")
    public void updateOneMatchedContact(){
        // given
        Members matchedContactMember = Members.builder()
                .id(10L)
                .name("anotherName")
                .password("123412341234")
                .memberName("anotherMemberName")
                .email("test123411@naver.com")
                .contact("010-2222-1212")
                .profileImageUrl(" ")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.SALES)
                .position(MemberPosition.INTERN)
                .remainingVacationDays(4)
                .salary(57000000L)
                .onVacation(false)
                .build();

        String matchedContact = matchedContactMember.getContact();

        MembersRequestDto.UpdateOneDto requestDto =
                MembersRequestDto.UpdateOneDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("test User NickName")
                        .address("updated Address")
                        .email("test12341234@naver.com")
                        .contact(matchedContact)
                        .build();

        MultipartFile image = getMultipartFile();
        // String documentPath = "path/to/document.pdf";

        // when
        when(membersServiceFacadeImpl.
                matchLoginMember(memberDetails.getMembers(), 2L))
                .thenReturn(memberDetails.getMembers());
        when(membersService.findAllExceptLoginMember(memberDetails.getMembers().getId())).thenReturn(
                List.of(memberDetails.getMembers(), matchedContactMember));

        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.updateOne(
                        2L, memberDetails.getMembers(), image, requestDto));

        // then
        assertEquals(requestDto.getMemberName(), memberDetails.getMembers().getMemberName());
        assertEquals(requestDto.getPassword(), requestDto.getPasswordConfirm());
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(
                MembersExceptionCode.MATCHED_MEMBER_INFO_CONTACT.getMessage(),
                e.getMessage());
    }
    @Test
    @Order(14)
    @DisplayName("updateOneForAttribute Success")
    public void updateOneForAttributeSuccess(){
        // given
        MemberRole role = MemberRole.ADMIN;
        MembersRequestDto.UpdateOneForAttributeDto requestDto =
                MembersRequestDto.UpdateOneForAttributeDto.builder()
                        .department(memberDetails.getMembers().getDepartment().toString())
                        .position("MANAGER")
                        .role(role.toString())
                        .memberName(memberDetails.getMembers().getMemberName())
                        .build();

        MultipartFile file = getMultipartFile();
        Members mainAdmin = setMainAdmin();

        Members updateMember = memberDetails.getMembers();
        when(membersService.readOneForDifferentMemberCheck(
                mainAdmin.getId(), updateMember.getId())).thenReturn(updateMember);
        when(notificationsService.saveForChangeMemberInfo(
                mainAdmin.getMemberName(), updateMember.getMemberName(),
                updateMember.getDepartment())).thenReturn(Notifications.builder().build());

        // when
        // 로그인 멤버가 메인 어드민이고 메인 어드민이 해당 직원을 변경하는 메서드
        MembersResponseDto.UpdateOneForAttributeDto responseDto
                = membersServiceFacadeImpl.updateOneForAttribute(
               updateMember.getId(), mainAdmin, requestDto, file);

        // then
        verify(filesService).createOneForMemberRole(file, updateMember);
        verify(notificationsService).saveForChangeMemberInfo(
                mainAdmin.getMemberName(), updateMember.getMemberName(),
                updateMember.getDepartment());
        assertEquals(requestDto.getMemberName(), responseDto.getMemberName());
        assertEquals(requestDto.getPosition(), responseDto.getMemberPosition().toString());
        assertEquals(requestDto.getDepartment(), requestDto.getDepartment().toString());
        assertEquals(requestDto.getRole(), requestDto.getRole().toString());
    }

    @Test
    @Order(15)
    @DisplayName("updateOneForAttribute Exception : RESTRICTED_ACCESS_MEMBER Change MainAdmin")
    public void updateOneForAttributeExceptionTestCaseOne(){
        // given
        Members updateMember = setMainAdmin();
        MembersRequestDto.UpdateOneForAttributeDto requestDto =
                MembersRequestDto.UpdateOneForAttributeDto.builder()
                        .memberName(updateMember.getMemberName())
                        .role(updateMember.getRole().toString())
                        .department(updateMember.getDepartment().toString())
                        .position(updateMember.getPosition().toString())
                        .build();
        MultipartFile file = getMultipartFile();

        // when
        // ASSIST_MANAGER가 MAIN_ADMIN의 권한, 직책, 직위를 변경하려는 경우
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.updateOneForAttribute(
                        updateMember.getId(), memberDetails.getMembers(), requestDto, file));

        // then
        assertEquals(updateMember.getMemberName(), setMainAdmin().getMemberName());
        assertEquals(
                MembersExceptionCode.RESTRICTED_ACCESS_MEMBER.getMessage(), e.getMessage());
    }

    @Test
    @Order(16)
    @DisplayName("updateOneForAttribute Exception : RESTRICTED_ACCESS_MEMBER" +
            " Change Manager By Assist Manager" )
    public void updateOneForAttributeExceptionCaseTwo(){
        // given
        Members managerMember = Members.builder()
                .id(3L)
                .memberName("parkjihoon")
                .role(MemberRole.ADMIN)
                .department(MemberDepartment.HR)
                .position(MemberPosition.MANAGER)
                .build();

        MembersRequestDto.UpdateOneForAttributeDto requestDto =
                MembersRequestDto.UpdateOneForAttributeDto.builder()
                        .memberName(managerMember.getMemberName())
                        .role(managerMember.getRole().toString())
                        .position(managerMember.getPosition().getPosition())
                        .department(managerMember.getDepartment().getDepartment())
                        .build();

        MultipartFile file = getMultipartFile();

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class
                , ()-> membersServiceFacadeImpl.updateOneForAttribute(
                        managerMember.getId(), memberDetails.getMembers(), requestDto, file));

        // then
        assertEquals(
                MembersExceptionCode.RESTRICTED_ACCESS_MEMBER.getMessage(), e.getMessage());
    }

    @Test
    @Order(17)
    @DisplayName(
            "updateOneForAttribute Exception : RESTRICTED_ACCESS_MEMBER" +
                    " Change IT Assist Manager By IT Manager" )
    public void updateOneForAttributeExceptionCaseThree(){
        // given
        Members itManagerMember = Members.builder()
                .id(3L)
                .memberName("parkjihoon")
                .role(MemberRole.ADMIN)
                .department(MemberDepartment.IT)
                .position(MemberPosition.MANAGER)
                .build();

        Members itInternMember = Members.builder()
                .id(4L)
                .memberName("parkjihun")
                .role(MemberRole.EMPLOYEE)
                .department(MemberDepartment.IT)
                .position(MemberPosition.ASSISTANT_MANAGER)
                .build();

        MembersRequestDto.UpdateOneForAttributeDto requestDto =
                MembersRequestDto.UpdateOneForAttributeDto.builder()
                        .memberName(itInternMember.getMemberName())
                        .role(MemberRole.MAIN_ADMIN.getAuthority())
                        .department(itInternMember.getDepartment().getDepartment())
                        .position(MemberPosition.STAFF.getPosition())
                        .build();

        MultipartFile file = getMultipartFile();

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class
                , ()-> membersServiceFacadeImpl.updateOneForAttribute(
                        itManagerMember.getId(), itInternMember, requestDto, file));

        // then
        assertEquals(
                MembersExceptionCode.RESTRICTED_ACCESS_MEMBER.getMessage(), e.getMessage());
    }

    @Test
    @Order(18)
    @DisplayName("updateOneForProfileImage Success")
    public void updateOneForProfileImageSuccess(){
        // given
        MultipartFile image
                = new MockMultipartFile(
                "profileImage", "updateProfile.png",
                "application/png", "PNG content".getBytes());

        when(membersServiceFacadeImpl.matchLoginMember(
                memberDetails.getMembers(), 2L))
                .thenReturn(memberDetails.getMembers());
        // when
        MembersResponseDto.UpdateOneForProfileImageDto responseDto
                = membersServiceFacadeImpl.updateOneForProfileImage(
                        memberDetails.getMembers().getId(), memberDetails.getMembers(), image);

        // then
        assertEquals(memberDetails.getMembers().getId(), responseDto.getMemberId());
        assertEquals(memberDetails.getMembers().getMemberName(), responseDto.getFromMemberName());
        assertEquals(
                memberDetails.getMembers().getProfileImageUrl(), responseDto.getProfileImageUrl());
        verify(filesService).createImage(image);
    }

    @Test
    @Order(19)
    @DisplayName("deleteMemberProfileImage Success")
    public void deleteOneForProfileImageSuccess() {
        // given
        String imageOriginalFileName = memberDetails.getMembers().getProfileImageUrl();

        when(membersServiceFacadeImpl.matchLoginMember(
                memberDetails.getMembers(), 2L))
                .thenReturn(memberDetails.getMembers());

        // when
        MembersResponseDto.DeleteOneForProfileImageDto responseDto
                = membersServiceFacadeImpl.deleteOneForProfileImage(
                        memberDetails.getMembers().getId(), memberDetails.getMembers());

        // then
        verify(filesService).deleteImage(imageOriginalFileName);
        assertEquals(memberDetails.getMembers().getId(), responseDto.getMemberId());
        assertEquals(
                memberDetails.getMembers().getMemberName(), responseDto.getFromMemberName());
    }

    @Test
    @Order(20)
    @DisplayName("deleteMemberProfileImage Exception : NOT_BLANK_IMAGE_FILE")
    public void deleteMemberProfileImageNotBlankImageFile() {
        // given
        Members blankImageOwner = Members.builder()
                .id(3L)
                .profileImageUrl("")
                .memberName("blank!")
                .build();

        when(membersServiceFacadeImpl.matchLoginMember(
                blankImageOwner, blankImageOwner.getId())).thenReturn(blankImageOwner);

        // when
        MembersCustomException e
                = assertThrows(MembersCustomException.class,
                ()-> membersServiceFacadeImpl.deleteOneForProfileImage(
                        blankImageOwner.getId(), blankImageOwner));
        // then
        assertEquals(
                MembersExceptionCode.NOT_BLANK_IMAGE_FILE.getMessage(), e.getMessage());
    }

    @Test
    @Order(21)
    @DisplayName("deleteOne Success")
    public void deleteOneSuccess(){
        // given
        when(membersServiceFacadeImpl.matchLoginMember(
                memberDetails.getMembers(), memberDetails.getMembers().getId()))
                .thenReturn(memberDetails.getMembers());

        // when
        membersServiceFacadeImpl.deleteOne(
                memberDetails.getMembers().getId(), memberDetails.getMembers());

        // then
        verify(membersService).deleteById(memberDetails.getMembers().getId());
    }

    private MultipartFile getMultipartFile(){
        return new MockMultipartFile(
                "file", "document.pdf",
                "application/pdf", "PDF content".getBytes());
    }

    private Members setMainAdmin(){
        return Members.builder()
                .id(1L)
                .memberName("admin")
                .role(MemberRole.MAIN_ADMIN)
                .position(MemberPosition.CEO)
                .department(MemberDepartment.HR)
                .build();
    }
}
