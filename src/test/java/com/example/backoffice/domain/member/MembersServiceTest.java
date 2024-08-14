package com.example.backoffice.domain.member;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class MembersServiceTest {
/*
    private MemberDetailsImpl memberDetails;

    @InjectMocks
    private MembersServiceImpl membersService;

    @Mock
    private FilesService filesService;

    @Mock
    private MembersConverter membersConverter;

    @Mock
    private MembersRepository membersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        memberDetails = new MemberDetailsImpl(
                Members.builder()
                        .id(1L)
                        .name("test User Name")
                        .password("12341234")
                        .memberName("test User NickName")
                        .contact("010-2222-1212")
                        .profileImageUrl(" ")
                        .role(MemberRole.HR)
                        .build()
        );
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                        memberDetails, memberDetails.getPassword(),
                        memberDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Order(1)
    @DisplayName("signup Success")
    public void signupSuccess(){
        // given
        MembersRequestDto.CreateMembersRequestDto requestDto =
                MembersRequestDto.CreateMembersRequestDto.builder()
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
        MembersResponseDto.CreateMembersResponseDto responseDto =
                membersService.signup(requestDto);
        // then
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
        MembersRequestDto.CreateMembersRequestDto requestDto =
                MembersRequestDto.CreateMembersRequestDto.builder()
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
                () -> membersService.signup(requestDto));

        // then
        // Exception
        // 1. 예외가 터지는 파라미터 검증
        // 2. 예외 메세지 검증
        // 3. 예외가 터진 코드 밑의 코드가 일어나는지?
        assertNotEquals(requestDto.getPasswordConfirm(), requestDto.getPassword());
        assertEquals(MembersExceptionCode.NOT_MATCHED_PASSWORD.getMessage(),
                e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersRepository, never()).save(any(Members.class));
    }

    @Test
    @Order(3)
    @DisplayName("signup Exception : EXIST_MEMBER")
    public void signupExistMember(){
        // given
        MembersRequestDto.CreateMembersRequestDto requestDto =
                MembersRequestDto.CreateMembersRequestDto.builder()
                        .name("testMemberName")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("testId")
                        .address("어디시 어딘구 어딘가")
                        .email("test@naver.com")
                        .contact("010-1111-1111")
                        .build();

        Members existingMember = Members.builder().build();
        when(membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(existingMember));

        // when
        Exception e = assertThrows(MembersCustomException.class,
                () -> membersService.signup(requestDto));

        // then
        assertNotEquals(requestDto.getEmail(), existingMember.getEmail());
        assertNotEquals(requestDto.getContact(), existingMember.getContact());
        assertNotEquals(requestDto.getAddress(), existingMember.getAddress());
        assertNotEquals(requestDto.getMemberName(), existingMember.getMemberName());
        assertEquals(
                MembersExceptionCode.EXISTS_MEMBER.getMessage(), e.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(membersRepository, never()).save(any(Members.class));
    }

    @Test
    @Order(4)
    @DisplayName("readInfo Success")
    public void readInfoSuccess(){
        // when
        MembersResponseDto.ReadMemberResponseDto responseDto =
                membersService.readInfo(1L, memberDetails.getMembers());
        // then
        assertEquals(responseDto.getMemberName(), memberDetails.getUsername());
        assertEquals(responseDto.getRole(), memberDetails.getMembers().getRole());
    }

    @Test
    @Order(5)
    @DisplayName("findMember Success")
    public void findMember(){
        // when
        Members member = membersService.findMember(memberDetails.getMembers(), 1L);
        // then
        assertEquals(member.getMemberName(), memberDetails.getUsername());
        assertEquals(member.getId(), memberDetails.getMembers().getId());
    }

    @Test
    @Order(6)
    @DisplayName("findMember Exception : NOT_FOUND_MEMBER")
    public void findMemberNotFoundMember(){
        // when
        Exception e =  assertThrows(
                MembersCustomException.class,
                () -> membersService.findMember(
                        memberDetails.getMembers(), 2L)
        );
        // then
        assertEquals(
                MembersExceptionCode.NOT_FOUND_MEMBER.getMessage(), e.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("updateMember Success")
    public void updateMemberSuccess(){
        // given
        Members existingMember = Members.builder()
                .id(1L)
                .name("test User Name")
                .memberName("test User NickName")
                .password("12341234")
                .address("어디시 어딘구 어딘가")
                .build();

        membersRepository.save(existingMember);  // 저장된 멤버 객체 설정

        MembersRequestDto.UpdateMemberRequestDto requestDto =
                MembersRequestDto.UpdateMemberRequestDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("test User NickName")
                        .address("updated Address")
                        .email("updated@naver.com")
                        .contact("010-1111-1111")
                        .build();

        given(membersRepository.save(any(Members.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(passwordEncoder.encode(requestDto.getPassword()))
                .willReturn("encodedPassword");

        // when
        MembersResponseDto.UpdateMemberResponseDto responseDto =
                membersService.updateMember(
                        1L, memberDetails.getMembers(), requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getName(), responseDto.getName());
        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getContact(), responseDto.getContact());
    }

    @Test
    @Order(8)
    @DisplayName("updateMember Exception : NOT_MATCHED_MEMBER_NAME")
    public void updateMemberNotMatchedMemberName(){
        // given
        Members existingMember = Members.builder()
                .id(1L)
                .name("test User Name")
                .memberName("test User NickName")
                .password("12341234")
                .address("어디시 어딘구 어딘가")
                .build();

        membersRepository.save(existingMember);  // 저장된 멤버 객체 설정

        MembersRequestDto.UpdateMemberRequestDto requestDto =
                MembersRequestDto.UpdateMemberRequestDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("12341234")
                        .memberName("Not matched NickName")
                        .address("updated Address")
                        .email("updated@naver.com")
                        .contact("010-1111-1111")
                        .build();

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersService.updateMember(
                        1L, existingMember, requestDto));
        // then
        assertNotEquals(requestDto.getMemberName(), existingMember.getMemberName());
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(
                MembersExceptionCode.NOT_MATCHED_MEMBER_NAME.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("updateMember Exception : NOT_MATCHED_PASSWORD")
    public void updateMemberNotMatchedPassword(){
        // given
        Members existingMember = Members.builder()
                .id(1L)
                .name("test User Name")
                .memberName("test User NickName")
                .password("12341234")
                .address("어디시 어딘구 어딘가")
                .build();

        membersRepository.save(existingMember);  // 저장된 멤버 객체 설정

        MembersRequestDto.UpdateMemberRequestDto requestDto =
                MembersRequestDto.UpdateMemberRequestDto.builder()
                        .name("updated Name")
                        .password("12341234")
                        .passwordConfirm("Not Matched Password")
                        .memberName("test User NickName")
                        .address("updated Address")
                        .email("updated@naver.com")
                        .contact("010-1111-1111")
                        .build();

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersService.updateMember(
                        1L, existingMember, requestDto));

        // then
        assertEquals(requestDto.getMemberName(), existingMember.getMemberName());
        assertNotEquals(requestDto.getPassword(), requestDto.getPasswordConfirm());
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(
                MembersExceptionCode.NOT_MATCHED_PASSWORD.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("updateMemberRoleSuccess")
    public void updateMemberRoleSuccess(){
        // given
        Members member = Members.builder()
                .id(1L)
                .name("test name")
                .role(MemberRole.HR)
                .build();

        MultipartFile file
                = new MockMultipartFile(
                        "file", "document.pdf",
                "application/pdf", "PDF content".getBytes());
        String documentPath = "path/to/document.pdf";

        when(filesService.createFileForMemberRole(
                any(MultipartFile.class), any(Members.class)))
                .thenReturn(documentPath);

        // when
        MembersResponseDto.UpdateMemberRoleResponseDto responseDto
                = membersService.updateMemberRole(1L, member, file);

        // then
        assertNotNull(responseDto);
        assertEquals(documentPath, responseDto.getFileName());

        verify(membersRepository).save(member);
        verify(filesService).createFileForMemberRole(file, member);
    }

    @Test
    @Order(11)
    @DisplayName("updateMemberProfileImageUrl Success")
    public void updateMemberProfileImageUrlSuccess(){
        // given
        Members member = Members.builder()
                .id(1L)
                .name("test name")
                .role(MemberRole.HR)
                .build();

        MultipartFile image
                = new MockMultipartFile(
                "profileImage", "profile.png",
                "application/png", "PNG content".getBytes());

        // when
        MembersResponseDto.UpdateMemberProfileImageUrlResponseDto responseDto
                = membersService.updateMemberProfileImageUrl(1L, member, image);

        // then
        assertNotNull(responseDto);

        verify(membersRepository).save(member);
        verify(filesService).createImage(image);
    }

    @Test
    @Order(12)
    @DisplayName("deleteMemberProfileImage Success")
    public void deleteMemberProfileImageSuccess() {
        // given
        Members member = Members.builder()
                .id(1L)
                .profileImageUrl("http://example.com/image.jpg")
                .build();

        // when
        MembersResponseDto.DeleteMemberProfileImageResponseDto responseDto
                = membersService.deleteMemberProfileImage(1L, member);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.getFromMemberName(), member.getMemberName());
        assertNull(member.getProfileImageUrl());
    }

    @Test
    @DisplayName("deleteMemberProfileImage Exception : NOT_BLANK_IMAGE_FILE")
    public void deleteMemberProfileImageExceptionTest() {
        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                () -> membersService.deleteMemberProfileImage(
                        memberDetails.getMembers().getId(), memberDetails.getMembers()),
                MembersExceptionCode.NOT_BLANK_IMAGE_FILE.getMessage());

        //then
        assertEquals(
                MembersExceptionCode.NOT_BLANK_IMAGE_FILE.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("deleteMember Success")
    public void deleteMemberSuccess(){
        // when
        membersService.deleteMember(1L, memberDetails.getMembers());
        // then
        verify(membersRepository).deleteById(1L);
    }

    @Test
    @Order(14)
    @DisplayName("validateMember Success")
    public void validateMemberSuccess(){
        // given
        Members member = Members.builder()
                .id(2L)
                .memberName("test Username")
                .name("test id")
                .build();

        when(membersRepository.findById(1L)).thenReturn(Optional.of(memberDetails.getMembers()));

        // when
        membersService.validateMember(1L, member.getId());

        // then
        assertNotEquals(member.getId(), memberDetails.getMembers().getId());
        assertNotEquals(member, memberDetails.getMembers());
    }

    @Test
    @Order(15)
    @DisplayName("validateMember Exception : MATCHED_LOGIN_MEMBER")
    public void validateMemberMatchedLoginMember(){
        // given
        Members member = Members.builder()
                .id(1L)
                .name("test User Name")
                .password("12341234")
                .memberName("test User NickName")
                .contact("010-2222-1212")
                .profileImageUrl(" ")
                .role(MemberRole.HR)
                .build();

        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersService.validateMember(
                        member.getId(), memberDetails.getMembers().getId()));

        // then
        assertEquals(member.getId(), memberDetails.getMembers().getId());
        assertEquals(member.getMemberName(), memberDetails.getMembers().getMemberName());
        assertEquals(
                MembersExceptionCode.MATCHED_LOGIN_MEMBER.getMessage(),
                e.getMessage());
    }

    @Test
    @Order(15)
    @DisplayName("validateMember Exception : NOT_FOUND_MEMBER")
    public void validateMemberNotFoundMember(){
        // when
        MembersCustomException e = assertThrows(MembersCustomException.class,
                ()-> membersService.validateMember(
                        memberDetails.getMembers().getId(), 2L));

        // then
        assertNotEquals(2L, memberDetails.getMembers().getId());
        assertEquals(
                MembersExceptionCode.NOT_FOUND_MEMBER.getMessage(),
                e.getMessage());
    }*/
}
