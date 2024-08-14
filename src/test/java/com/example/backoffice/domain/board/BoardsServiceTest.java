package com.example.backoffice.domain.board;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class BoardsServiceTest {

    /*private MemberDetailsImpl memberDetails;

    @Mock
    private FilesService filesService;

    @InjectMocks
    private BoardsServiceImpl boardsService;

    @Mock
    private MembersRepository membersRepository;

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private RedisProvider redisProvider;

    private Members notLoginMember;

    @BeforeEach
    public void setUp(){
        memberDetails = new MemberDetailsImpl(
                Members.builder()
                        .id(1L)
                        .memberName("loginMemberName")
                        .name("loginMemberId")
                        .password("12341234")
                        .email("test@naver.com")
                        .address("어디시 어딘구")
                        .contact("010-0000-0000")
                        .profileImageUrl(" ")
                        .introduction("test!")
                        .role(MemberRole.USER)
                        .loveCount(0L)
                        .build()
        );

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                        memberDetails, memberDetails.getPassword(), memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        notLoginMember = Members.builder()
                .id(2L)
                .memberName("notloginMemberName")
                .name("notloginMemberId")
                .password("112341234")
                .email("ttest@naver.com")
                .address("어디시 어딘구 어딘동")
                .contact("010-0000-0001")
                .profileImageUrl(" ")
                .introduction("test!")
                .role(MemberRole.USER)
                .loveCount(0L)
                .build();

        membersRepository.save(memberDetails.getMembers());
        membersRepository.save(notLoginMember);
    }

    @Test
    @Order(1)
    @DisplayName("createBoard Success")
    public void createBoard(){
        // given
        BoardsRequestDto.CreateBoardRequestDto requestDto
                = BoardsRequestDto.CreateBoardRequestDto.builder()
                .title("board1 title")
                .content("board1 content")
                .build();

        MultipartFile image
                = new MockMultipartFile(
                "images", "images.png",
                "application/png", "PNG content".getBytes());

        List<MultipartFile> images = new ArrayList<>();
        images.add(image);

        Members member = memberDetails.getMembers();
        when(filesService.createFileForBoard(any(MultipartFile.class), any(Boards.class))).thenReturn("URL_to_image.png");
        when(boardsRepository.save(any(Boards.class))).then(returnsFirstArg());

        // when
        BoardsResponseDto.CreateBoardResponseDto responseDto = boardsService.createBoard(member, requestDto, images);

        // then
        assertEquals(member.getMemberName(), responseDto.getWriter());
        assertEquals(requestDto.getContent(), responseDto.getContent());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        verify(filesService, times(images.size()))
                .createFileForBoard(any(MultipartFile.class), any(Boards.class));
        verify(boardsRepository).save(any(Boards.class));
        assertTrue(responseDto.getFileList().get(0).contains("URL_to_image.png"));
    }

    @Test
    @Order(2)
    @DisplayName("readOne Success")
    public void readOneSuccess(){
        // given
        Boards board = Boards.builder()
                .id(1L)
                .member(memberDetails.getMembers())
                .title("board title")
                .content("board content")
                .likeCount(1L)
                .unLikeCount(10L)
                .fileList(new ArrayList<>())
                .viewCount(0L)
                .commentList(new ArrayList<>(new ArrayList<>()))
                .reactionList(new ArrayList<>())
                .build();

        Comments comment1 = Comments.builder()
                .id(1L)
                .parent(null)
                .member(notLoginMember)
                .board(board)
                .replyList(new ArrayList<>())
                .content("Comment content")
                .likeCount(0L)
                .unLikeCount(0L)
                .build();

        Comments reply1 = Comments.builder()
                .parent(comment1)
                .member(memberDetails.getMembers())
                .content("Reply Content")
                .board(board)
                .likeCount(0L)
                .unLikeCount(0L)
                .build();

        Comments reply2 = Comments.builder()
                .board(board)
                .parent(comment1)
                .member(memberDetails.getMembers())
                .content("Reply Content2")
                .likeCount(0L)
                .unLikeCount(2L)
                .build();

        comment1.updateParent(comment1);
        comment1.addReply(reply1);
        comment1.addReply(reply2);
        board.addComment(comment1);

        String redisKey = "boardId : "+board.getId()+ ", viewMemberName : "+
                memberDetails.getMembers().getMemberName();

        when(boardsRepository.findById(1L))
                .thenReturn(Optional.of(board));
        when(redisProvider.getViewCount(redisKey)).thenReturn(0L);
        when(redisProvider.incrementViewCount(anyString())).thenReturn(1L);

        // when
        BoardsResponseDto.ReadBoardResponseDto responseDto
                = boardsService.readOne(board.getId());

        // then
        assertEquals(board.getContent(), responseDto.getContent());
        assertEquals(board.getTitle(), responseDto.getTitle());
        assertEquals(board.getCommentList().get(0).getId(),
                responseDto.getCommentList().get(0).getCommentId());
        assertEquals(board.getCommentList().get(0).getContent(),
                responseDto.getCommentList().get(0).getCommentContent());
        assertEquals(board.getCommentList().get(0).getReplyList().get(0).getContent(),
                comment1.getReplyList().get(0).getContent());
        assertEquals(board.getCommentList().get(0).getReplyList().get(1).getContent(),
                comment1.getReplyList().get(1).getContent());
        assertEquals(1, board.getViewCount());
        verify(redisProvider).getViewCount(anyString());
        verify(redisProvider).incrementViewCount(anyString());
    }

    @Test
    @Order(3)
    @DisplayName("readBoard Success")
    public void readBoardSuccess(){
        // given
        Boards board1 = Boards.builder()
                .member(memberDetails.getMembers())
                .title("board1 title")
                .content("board1 content")
                .likeCount(0L)
                .unLikeCount(0L)
                .viewCount(0L)
                .build();

        when(boardsRepository.save(board1)).thenReturn(board1);
        // when
        // then
    }*/
}
