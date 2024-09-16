package com.example.backoffice.domain.board;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.board.service.BoardsServiceImplV1;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.file.service.FilesServiceImplV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.security.MemberDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class BoardsServiceTest {

    @InjectMocks
    private BoardsServiceImplV1 boardsService;

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private FilesServiceImplV1 filesService;

    @Mock
    private SecurityContext securityContext;

    private MemberDetailsImpl memberDetails;

    private Members memberOne;

    private Members memberTwo;

    @BeforeEach
    public void setUp(){
        memberDetails = new MemberDetailsImpl(
                Members.builder()
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
                        .build()
        );

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                memberDetails, memberDetails.getPassword(),
                memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

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
    }

    @Test
    @Order(1)
    @DisplayName("createOne Success")
    public void createOneSuccess() {
        // given
        BoardsRequestDto.CreateOneDto requestDto = BoardsRequestDto.CreateOneDto.builder()
                .title("test Board title")
                .content("test Board content")
                .build();

        Boards board = Boards.builder()
                .member(memberDetails.getMembers())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        List<MultipartFile> multipartFileList = getMultipartFileList();

        // Static method mocking with mockStatic
        try (MockedStatic<BoardsConverter> mockedStatic = mockStatic(BoardsConverter.class)) {
            mockedStatic.when(() -> BoardsConverter.toEntity(requestDto, memberDetails.getMembers()))
                    .thenReturn(board);
            mockedStatic.when(() -> BoardsConverter.toCreateOneDto(board, List.of("fileName1", "fileName2", "fileName3")))
                    .thenReturn(BoardsResponseDto.CreateOneDto.builder()
                            .writer(memberDetails.getMembers().getMemberName())
                            .title(board.getTitle())
                            .content(board.getContent())
                            .createdAt(board.getCreatedAt())
                            .fileList(List.of("fileName1", "fileName2", "fileName3"))
                            .build());

            when(boardsRepository.save(board)).thenReturn(board);

            when(filesService.createOneForBoard(multipartFileList.get(0), board))
                    .thenReturn("fileName1");
            when(filesService.createOneForBoard(multipartFileList.get(1), board))
                    .thenReturn("fileName2");
            when(filesService.createOneForBoard(multipartFileList.get(2), board))
                    .thenReturn("fileName3");
            
            // when
            BoardsResponseDto.CreateOneDto responseDto = boardsService.createOne(
                    memberDetails.getMembers(), requestDto, multipartFileList);

            // then
            verify(boardsRepository).save(board);
            assertEquals(requestDto.getTitle(), responseDto.getTitle());
            assertEquals(requestDto.getContent(), responseDto.getContent());
            assertEquals(memberDetails.getMembers().getMemberName(), responseDto.getWriter());
        }
    }

    @Test
    @Order(2)
    @DisplayName("readAll Success")
    public void readAllSuccess(){
        // given
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        List<Boards> boardList = getBoardList();
        Page<Boards> boardListPage
                = new PageImpl<>(boardList, pageable, boardList.size());
        when(boardsRepository.findAll(pageable)).thenReturn(boardListPage);

        // when
        Page<BoardsResponseDto.ReadAllDto> responseList = boardsService.readAll(pageable);

        // then
        assertEquals(responseList.getContent().size(), boardList.size());
        assertEquals(responseList.getContent().get(0).getTitle(), boardList.get(0).getTitle());
        assertEquals(responseList.getContent().get(1).getTitle(), boardList.get(1).getTitle());
        assertEquals(responseList.getContent().get(2).getTitle(), boardList.get(2).getTitle());
        assertEquals(responseList.getContent().get(0).getContent(), boardList.get(0).getContent());

        verify(boardsRepository, times(1)).findAll(pageable);
    }

    @Test
    @Order(3)
    @DisplayName("updateOne Success")
    public void updateOneSuccess() {
        // given
        Boards boardOne = spy(getBoardList().get(0));
        Members member = memberDetails.getMembers();

        BoardsRequestDto.UpdateOneDto requestDto = BoardsRequestDto.UpdateOneDto.builder()
                .title("update title!")
                .content("update content!")
                .build();

        List<MultipartFile> updateMultipartFileList = List.of(
                new MockMultipartFile("file1", "file1.txt", "text/plain", "some content".getBytes()),
                new MockMultipartFile("file2", "file2.txt", "text/plain", "some more content".getBytes())
        );

        when(boardsRepository.findById(boardOne.getId()))
                .thenReturn(Optional.of(boardOne));

        List<Files> existingFileList = new ArrayList<>();
        existingFileList.add(Files.builder().url("url1").board(boardOne).member(member).build());
        existingFileList.add(Files.builder().url("url2").board(boardOne).member(member).build());
        when(boardOne.getFileList()).thenReturn(existingFileList);

        when(filesService.createOneForBoard(any(MultipartFile.class), eq(boardOne)))
                .thenReturn("newFileUrl1", "newFileUrl2");

        // when
        BoardsResponseDto.UpdateOneDto responseDto = boardsService.updateOne(
                boardOne.getId(), member, requestDto, updateMultipartFileList);

        // then
        verify(boardOne, times(1)).update(requestDto.getTitle(), requestDto.getContent());
        verify(filesService, times(1)).delete(eq(boardOne.getId()), anyList());
        verify(filesService, times(2)).createOneForBoard(any(MultipartFile.class), eq(boardOne));
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContent(), responseDto.getContent());
    }

    @Test
    @Order(4)
    @DisplayName("updateOne Exception : NOT_FOUND_BOARDS")
    public void updateOneNotFoundBoard(){
        // given
        BoardsRequestDto.UpdateOneDto requestDto =
                BoardsRequestDto.UpdateOneDto.builder()
                        .title("update title!")
                        .content("update content!")
                        .build();

        Boards notFoundBoard = getBoardList().get(0);
        when(boardsRepository.findById(notFoundBoard.getId()))
                .thenReturn(Optional.empty());

        // when
        BoardsCustomException e = assertThrows(BoardsCustomException.class,
                ()-> boardsService.updateOne(
                        notFoundBoard.getId(), memberDetails.getMembers(),
                        requestDto, getMultipartFileList()));

        // then
        assertEquals(e.getMessage(), BoardsExceptionCode.NOT_FOUND_BOARD.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("deleteOne Success")
    public void deleteOneSuccess(){
        // given
        Boards deletedBoard = getBoardList().get(0);
        when(boardsRepository.findById(deletedBoard.getId()))
                .thenReturn(Optional.of(deletedBoard));
        // when
        boardsService.deleteOne(deletedBoard.getId(), memberDetails.getMembers());
        // then
        assertTimeout(Duration.ofMillis(500), () -> {
            boardsService.deleteOne(deletedBoard.getId(), memberDetails.getMembers());
        });
    }

    @Test
    @Order(6)
    @DisplayName("deleteOne Exception : NOT_FOUND_BOARD")
    public void deleteOneNotFoundBoard(){
        // given
        Boards notFoundBoard = getBoardList().get(0);
        // 실체는 존재하지만 해당 repository에 저장되어 있지 않음
        when(boardsRepository.findById(notFoundBoard.getId()))
                .thenReturn(Optional.empty());

        // when
        BoardsCustomException e = assertThrows(BoardsCustomException.class,
                () -> boardsService.deleteOne(notFoundBoard.getId(), memberDetails.getMembers()));

        // then
        assertEquals(e.getMessage(), BoardsExceptionCode.NOT_FOUND_BOARD.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("deleteOne Exception : NOT_MATCHED_MEMBER")
    public void deleteOneNotMatchedMember(){
        // given
        Boards notMatchedMemberBoard = getBoardList().get(2);
        when(boardsRepository.findById(notMatchedMemberBoard.getId()))
                .thenReturn(Optional.of(notMatchedMemberBoard));

        // when
        BoardsCustomException e = assertThrows(BoardsCustomException.class,
                () -> boardsService.deleteOne(notMatchedMemberBoard.getId(), memberDetails.getMembers()));

        // then
        assertEquals(e.getMessage(), BoardsExceptionCode.NOT_MATCHED_MEMBER.getMessage());
    }
    /*
    void deleteOne(Long boardId, Members member);
    Boards findById(Long boardId);
    */

    private List<MultipartFile> getMultipartFileList(){
        return List.of(
                new MockMultipartFile(
                    "image", "image1.png",
                    "application/png", "PNG content".getBytes()),
                new MockMultipartFile(
                        "file", "document1.pdf",
                        "application/pdf", "PDF content".getBytes()),
                new MockMultipartFile(
                        "file", "document2.pdf",
                        "application/pdf", "PDF content".getBytes())
        );
    }

    private List<Boards> getBoardList(){
        Boards boardOne = Boards.builder()
                .id(1L)
                .title("title1")
                .content("content")
                .member(memberDetails.getMembers())
                .build();

        List<Comments> boardOneCommentList = List.of(
                Comments.builder().board(boardOne).member(memberOne).content("멋저요!").build(),
                Comments.builder().board(boardOne).member(memberTwo).content("정말 대단해요!").build()
        );

        return List.of(
                boardOne,
                Boards.builder()
                        .id(2L)
                        .title("title2")
                        .content("content2")
                        .member(memberDetails.getMembers())
                        .build(),
                Boards.builder()
                        .id(3L)
                        .title("title3")
                        .content("content3")
                        .member(memberOne)
                        .build());
    }
}
