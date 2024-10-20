package com.example.backoffice.domain.notification;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeImplV1;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.global.security.MemberDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class NotificationServiceFacadeTest {

    @InjectMocks
    private NotificationsServiceFacadeImplV1 notificationsServiceFacade;

    @Mock
    private NotificationsServiceV1 notificationsService;

    @Mock
    private MembersServiceFacadeV1 membersServiceFacade;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private MemberDetailsImpl memberDetails;

    private Members memberOne;

    private Members memberTwo;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

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
    @Order(0)
    @DisplayName("sendNotificationForUser Success")
    public void sendNotificationForUserSuccess(){
        // given
        NotificationData notificationData = NotificationData.builder()
                .fromMember(memberDetails.getMembers())
                .toMember(memberOne)
                .message("Test notification message")
                .build();
        String toMemberName = notificationData.getToMember().getMemberName();
        NotificationType notificationType = NotificationType.MEMBER;

        Notifications notification = Notifications.builder()
                .notificationType(notificationType)
                .toMemberName(notificationData.getToMember().getMemberName())
                .fromMemberDepartment(notificationData.getFromMember().getDepartment())
                .fromMemberName(notificationData.getFromMember().getMemberName())
                .message(notificationData.getMessage())
                .isRead(false)
                .build();

        doAnswer(invocation -> {
            String user = invocation.getArgument(0);
            String destination = invocation.getArgument(1);
            Notifications sentNotification = invocation.getArgument(2);
            System.out.println("Sending notification to user: " + user + " at destination: " + destination);
            return null;
        }).when(simpMessagingTemplate).convertAndSendToUser(anyString(), anyString(), any(Notifications.class));

        // when
        // notificationsServiceFacade.sendNotificationForUser(toMemberName, notification);

        // then
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Notifications> notificationCaptor = ArgumentCaptor.forClass(Notifications.class);

        verify(simpMessagingTemplate).convertAndSendToUser(
                userCaptor.capture(),
                destinationCaptor.capture(),
                notificationCaptor.capture()
        );

        assertEquals(toMemberName, userCaptor.getValue());
        assertEquals("/queue/notifications", destinationCaptor.getValue());
        assertEquals(notification, notificationCaptor.getValue());
    }

    /*@Test
    @Order(1)
    @DisplayName("createOne Success")
    public void createOneSuccess() {
        // given
        NotificationData notificationData = NotificationData.builder()
                .fromMember(memberDetails.getMembers())
                .toMember(memberOne)
                .message("Test notification message")
                .build();
        NotificationType notificationType = NotificationType.MEMBER;

        doAnswer(invocation -> {
            Notifications savedNotification = invocation.getArgument(0);
            System.out.println("Notification saved: " + savedNotification.getMessage());
            return savedNotification;
        }).when(notificationsService).save(any(Notifications.class));

        // when
        notificationsServiceFacade.createOne(notificationData, notificationType);

        // then
        // Verify that the save method was called
        // verify(notificationsService, times(1)).save(any(Notifications.class));

        // Verify that the notification was sent
        verify(simpMessagingTemplate, times(1)).convertAndSendToUser(
                eq(notificationData.getToMember().getMemberName()),
                eq("/queue/notifications"),
                any(Notifications.class));

        // You can also verify that the notification was created correctly by capturing it
        ArgumentCaptor<Notifications> notificationCaptor = ArgumentCaptor.forClass(Notifications.class);
        verify(notificationsService).save(notificationCaptor.capture());
        Notifications savedNotification = notificationCaptor.getValue();

        // Additional assertions to confirm notification was created correctly
        assertEquals(notificationData.getMessage(), savedNotification.getMessage());
        assertEquals(notificationData.getFromMember().getMemberName(), savedNotification.getFromMemberName());
        assertEquals(notificationData.getToMember().getMemberName(), savedNotification.getToMemberName());
    }*/
}
