# BackOffice

1. **한 줄 설명**
    
    한 곳에서 회사의 업무를 효율적으로 관리를 할 수 있게 도와주는 서비스입니다.
    
2. **상세 내용**
    - 모든 멤버 공통 사항
        1. 자신의 정보를 수정할 수 있습니다. 단, 급여, 직위, 직책과 같은 정보는 수정할 수 없습니다.
        2. 전체 게시판을 조회, 자신이 작성한 부서 게시판을 조회, 생성, 수정, 삭제할 수 있습니다.
        3. 자신의 부서에 따른 일정 또는 개인 일정을 조회, 생성, 수정, 삭제가 가능합니다.
        4. 부서 게시글에 잠금 표시가 활성화되면 다른 부서의 인원은 조회할 수 없습니다.
        5. 개인 즐겨 찾기를 활용해서 빠른 접근이 가능합니다.
        6. 출/퇴근 요청을 할 수 있습니다.
    
    - 부서 관리자 공통 사항
        1. 부서의 게시판을 제어할 수 있다.
        2. 부서 일정을 제어할 수 있다.
        3. 특정 인원에게 알림을 보낼 수 있다.
        
    - 인사 부장
        1. 특정 멤버의 급여, 직위, 직책을 수정할 수 있습니다. 단, 메인 관리자는 수정할 수 없습니다.
        2. 모든 멤버의 휴가를 관리할 수 있습니다.
        3. 모든 멤버의 출/퇴근을 관리할 수 있습니다.
        
    - 감사 부장
        1. 특정 감사 관리를 위한 서비스를 접속 할 필요 없이, 해당 사이트에서 감사 관리의 일부를 확인할 수 있습니다.
        2. 비정상적인 로그는 바로 알림 기능을 통해 전달 받을 수 있습니다.
        
    - IT 부장
        1. 시스템의 오류가 생기면 알림 기능을 통해 전달 받을 수 있습니다.
    
3. **기대 효과**
    1. 권한에 따른 효율적인 임무 분담이 가능합니다.
    2. 회사의 반복적인 업무를 일정 부분을 스케줄러가 처리하기에 편하게 이용할 수 있습니다.
    3. 회사 멤버의 부적절한 행동을 특정 서비스를 통해 확인을 하는 것이 아닌, 자체 서비스에서 확인을 할 수 있습니다.
    4. 자체적인 알림 서비스를 통해 중요한 일을 전달 할 수 있습니다.
    5. AWS를 통해 안전한 인프라 구성과 자체적인 알림 서비스, 캐싱 데이터 관리, 감사 관리 데이터베이스를 사용하기에 안정성이 올라갑니다.
    
4. **서비스 아키텍처**
    
    ![서비스아키텍쳐](https://github.com/user-attachments/assets/e762c30c-15f8-4118-a9c7-de53bc5b49c6)

    
5. **UI/UX**
    - 로그인/회원가입

        ![로그인v1000](https://github.com/user-attachments/assets/122f9268-94f3-49c6-88a8-f92bc249e9bc)

        ![회원가입v1000](https://github.com/user-attachments/assets/6edcd03f-bdcb-4823-b4d8-bfa1ba825833)

    - 메인 페이지

        ![메인 페이지v1](https://github.com/user-attachments/assets/01cf05dc-acd8-4011-b6d3-c68238b80537)
        
    - 게시글
        - 전체

            ![전체게시글v1000](https://github.com/user-attachments/assets/ff1ac432-7ecb-4969-ad32-af288a40c824)

            
        - 부서
            
            ![부서게시글작성v1000](https://github.com/user-attachments/assets/143093e6-ad2b-4d6c-b62e-68404637a2ac)

            ![세일즈게시글하나조회v1000](https://github.com/user-attachments/assets/635ff963-ee62-4d79-9a46-251041ab1fc0)
           
            ![댓글 작성v1000](https://github.com/user-attachments/assets/db78c40d-da81-4ddb-a731-878bbc151182)
            
            ![답글작성v1000](https://github.com/user-attachments/assets/3bcc6093-4580-4b9a-a1ca-12013f5acdd7)

    - 일정
        - 부서
          
            ![일정페이지v1000](https://github.com/user-attachments/assets/7c5dbcba-1c23-4b16-8130-18ec26ebb221)

            ![부서일정표v1000](https://github.com/user-attachments/assets/1d61e431-1dac-42d1-833f-34790fdaa409)

            
        - 개인
            
            ![개인일정v1000](https://github.com/user-attachments/assets/a196d666-7c7b-4d9a-b6f8-63e8e1b9cbef)

           ![개인일정선택v1000](https://github.com/user-attachments/assets/a91faf67-428b-4813-a54e-66edda25cad3)

            ![휴가신청v1000](https://github.com/user-attachments/assets/4730e041-3a4b-46b1-a50c-e2152b63b50a)

            
    - 개인 근태 기록

      ![개인근태기록페이지v1000_1](https://github.com/user-attachments/assets/5419cb48-d5ef-44db-bf3d-37c5bf275e43)

        
    - 알림
        - 개인 알림 모달
            
            ![개인알림페이지v1000](https://github.com/user-attachments/assets/62f5e600-44f3-4be4-864c-829138a94ab0)

            
    - 관리자 페이지
        - 휴가
            
            ![휴가관리v1000](https://github.com/user-attachments/assets/6810f6e8-4054-4e6f-a043-e758f6634e82)

        - 근태
            
            ![근태관리v1000](https://github.com/user-attachments/assets/308d1c87-ab6c-4c8f-b631-d989210bc685)

            ![당일근태관리v1](https://github.com/user-attachments/assets/16975d4d-9ef7-4493-b538-600f8ea7df65)
            
        - 알림
            
            ![1942](https://github.com/user-attachments/assets/0df69c76-97f0-4662-9873-8daf53b42055)
          
            ![1942v2](https://github.com/user-attachments/assets/8f5736ba-c8eb-403b-8469-12d768340f94)

            ![1942v3](https://github.com/user-attachments/assets/3680f242-e1d2-4aa4-a471-abaed5b90d2f)
            
        - 인사
            
            ![인사관리v1000](https://github.com/user-attachments/assets/19909905-7ede-4b38-a4d2-d0cae69afc4a)
            
        - 감사
            
            ![감사관리v1000](https://github.com/user-attachments/assets/2f71fe94-b575-48d0-b055-9647e2afc0e0)
 
5. **Technical Decision**
   <details>
       <summary>프론트</summary>
       
   - 문제 상황     
      - PostMan 또는 다른 방식을 통해 API 요청을 보여주는 개발 방식 보다는 해당 서비스가 직접적으로 이루어지는 과정을 직접 보여주고자 하는데, 적절하게 사용할 수 있는 프론트 기술을 알지 못하는 문제에 직면함.
            
   - 도입 이유
      - 특정 API의 요청/응답을 보여주는 것이 아닌, 직접적으로 요청/응답이 가는 형태를 단 하나의 화면으로 보여줄 수 있게 됨.
      - 백엔드 개발자라도 프론트의 데이터 요청/응답 흐름을 알고 있어야 한다고 생각함.
      - 프레임 워크 결정
         - ReactJs
            - 직접 다루어 본 적이 존재해, 비교적 낮은 러닝 커브
            - 수많은 라이브러리들이 존재하여 유지 보수가 용이
         - VueJs
            - Vuex, Pinia를 활용한 간단한 상태 관리
            - 이전 프로젝트에서 사용해본 경험 존재
            - 상대적으로 높은 러닝 커브
            - 라이브러리가 상대적으로 적어 유지 보수가 어려움
         - 결론
            - 아래와 같은 이유로 ReactJs를 통한 프론트를 구성하고자 함.
            - 대량의 데이터 관리
                - 대량의 데이터 처리를 필터링, 테이블, 페이지을 통해 구성 → SPA(Single Page Application) 구조를 통하여 다량의 데이터를 빠르게 렌더링이 가능함.
            - 보안
                - JWT 기반 인증 적용 → Spring Boot 백엔드와 연동하여 토큰 기반 인증을 함.
                - CSRF, XSS 방어 → React는 기본적으로 클라이언트 렌더링을 사용하여 서버 측 렌더링보다 CSRF 위험이 적음
            - 확장성 & 유지보수
                - 컴포넌트 기반 개발로 재사용성이 높음 → 유지보수 시 특정 컴포넌트만 수정하여 빠른 대응 가능
                - AWS S3 + CloudFront 배포와 호환성 높음 → 정적 사이트로 빌드하여 빠르게 배포 가능
         - 구현 방식
            - 대량의 데이터 관리
                - 필터를 재사용 컴포넌트를 만들어 사용하고 각각의 특징이 두드러지는 테이블과 페이지는 따로 만들어 대량 데이터를 관리를 밑과 같은 코드로 관리합니다.
                    - FilterDropDown
                        - 다양한 필터 타입을 지원하여 유연한 데이터 검색 가능하게 합니다.
                            - checkbox, input, selct 타입을 유연하게 사용할 수 있게 합니다.
                        - 사용자가 원하는 조건을 쉽게 선택할 수 있도록 UI를 제공하며, useState를 활용해 상태를 관리함.
                    - 테이블 스타일은 한 곳에서 관리하도록 집중하고 일관된 디자인을 할 수 있도록 합니다.
                    - PaginationFooter
                        - 여러 페이지에서 동일한 페이징 UI를 사용할 수 있도록 재사용 가능한 컴포넌트로 구성하여 유지보수성을 향상시켰습니다.
                        - 현재 페이지를 중심으로 최대 5개의 페이지 버튼(이전 2개, 이후 2개)을 노출하는 방식으로 구현하여 가독성을 높힙니다.
                    
            - 보안
                - 프로젝트에서 JWT 기반 사용자 인증 적용을 아래와 같은 모듈로 수행합니다.
                    - AuthProvider
                        - 사용자의 인증 상태를 관리하는 역할을 합니다.
                        - checkAuth()를 실행하여 서버에서 인증 상태를 확인한 후, 사용자 정보를 useState로 관리
                        - 사용자 정보를 **LocalStorage**에 저장하여 페이지 새로고침 시에도 유지
                        - 다양한 필드들(isAuthenticated, id, name…)을 Context로 제공하여 인증된 사용자의 정보를 쉽게 활용 가능
                            - isAuthenticated = ture : localStorage에 특정 정보 저장 및 Context Provider를 통해 다른 컴포넌트도 해당 정보를 유지
                            - isAuthenticated = false : 403에러 반환
                    - AxoisUtils
                        - Axios 인스턴스를 생성하여, 공통적인 HTTP 요청 설정을 적용하는 역할을 합니다.
                        - 인증이 필요 여부에 따른 api 요청을 구분합니다.
                            - apiBaseUrl : env파일에 특정 api 경로를 지정하고 개발, 배포 환경에 따른 요청 경로를 보냅니다.
                            - axoisUnAuthenticated : signup, login에 대한 api 요청은 jwt token의 요청이 불필요한 요청을 처리합니다.
                            - axoisAuthenticated : jwt token 요청을 무조건 처리한 API 요청을 보냅니다.
                            - interceptors : 요청 시, jwt token 토큰을 추가하고 이에 맞는 응답 처리합니다.
                    - AuthService
                        - 회원가입, 로그인, 로그아웃, 인증 확인 등의 API 요청을 담당하는 서비스 모듈입니다.
                        - chekAuth : 요청을 통해 현재 사용자 인증 상태 확인합니다.
                        - getAccessToken : refreshToken의 만료 시에, 요청을 통해 새 액세스 토큰 발급합니다.
        - 기대 효과
            - 빠른 데이터 검색 & 렌더링 최적화로 사용자 경험이 향상됩니다.
            - 안전한 사용자 인증 & 데이터 보호할 수 있습니다.
   </details>

    <details>
        <summary>보안을 강화한 인프라</summary>
        
   - JWT Token 보안 강화
      - 문제 상황
           기존의 Spring boot의 Security를 사용해, Session을 통한 제어를 하는 방식은 서버에 많은 부담을 주며, 직원이 많은 환경에서는 서버를 여러 대 둬야 하는데, 같은 세션을 공유하는 저장소를 확장 해야하는 유지 보수 비용이 불필요하게 발생하게 됩니다.   
      - 도입 이유 
        - 기업의 내부 정보의 안정성 증가 
        - Jwt Token
            - 상대적으로 낮은 유지 보수 비용
            - 높은 개발 친화성
            - 확장성
        - 구현 방식
            - JWT Token은 Access Token은 만료 기간은 1시간, RefreshToken은 만료 기간이 1일이며, RefreshToken은 Redis를 통한 저장을 하고 있습니다.
            - Jwt Token이 필요 없는 API 요청 : 로그인, 회원 가입, 이름 중복 체크에 관련된 요청은 요청 헤더에 Jwt Token을 넣지 않은 API 요청을 합니다.
            - 일반적인 API 요청 : 모든 요청은 요청 헤더에 Cookie를 담아서 요청을 보내야 합니다.
            - WebSocket 요청 : Cookie에 저장되어 있는 accessToken을 Authorization : Bearer your_jwt_token과 같은 형식으로 요청 헤더에 담아서 요청합니다.
        - 기대 효과
            - 기존의 세션 방식에서 취약한 XSS, CSRF 공격 방어를 할 수 있습니다.
            - Websocket 환경 사용에 있어서 Jwt Token 인증 방식을 재사용하여 인증을 할 수 있습니다.
        - HTTPS 적용 및 ResponseCookie 활용
            - 문제 상황   
                브라우저에서 http 프로토콜로 서버의 접근을 허용하게 되면 네트워크에서 비밀번호, 토큰을 가로채는 중간자 공격에 취약하다는 문제점이 발생합니다.
            - 도입 이유
                - HTTPS를 도입하여 클라이언트-서버간의 데이터를 암호화하여 보안을 강화하고자 합니다.
                - ALB를 활용하여, EC2 내부는 HTTP 프로토콜로 통신하여 접근하게 하여, 보안의 안정성을 증대 시키고자 합니다.
                - Jwt Token을 HttpOnly, Secure = true, SameSite = “Strict” 환경에만 작동하게 하여, 특정 공격의 취약한 부분을 제거하고자 합니다.
            - 구현 방식
                - AWS ACM을 활용한 TLS 버젼 1.2 인증서를 API 서버와 웹 사이트에 발급하여 데이터를 암호화할 수 있게 합니다.
                - CloudFront와 ALB를 통한 원본 서버를 연결하여 성능을 최적화할 수 있게 합니다.
                - Spring boot에서 ResponseCookie를 활용해서 특정 설정을 통하여 HTTPS 프로토콜 환경에서만 해당 쿠키를 전달 가능하게 변경합니다.
                - 사진 첨부
                    - 정적 웹사이트, API 서버 TLS 발급       
                            ![backoffice_tls](https://github.com/user-attachments/assets/30a03764-2d7f-4646-8c8e-037c4e4a6b76)
                    - CloudFront ALB 연결
                        ![cloudFront_alb_connect](https://github.com/user-attachments/assets/f8795583-81a0-474b-b1d5-dc4dc64f7bf4)
                        ![cloudFront_alb_connect_v2](https://github.com/user-attachments/assets/3c37633e-de32-420b-9d06-d421492b40e7)
            - 기대 효과
                - MITM(Man-in-the-Middle) 공격 방지 및 데이터 보안이 강화됩니다.
                - 클라이언트에서 JWT 토큰 접근 차단 (XSS, CSRF 공격 방어 가능)할 수 있습니다.
            - 해당 문제 상황 추가 정리
                [backoffice_cookie_v1.pdf](https://github.com/user-attachments/files/19104903/backoffice_cookie_v1.pdf)
        - 인프라 보안
            - 문제 상황   
                비정상적인 API 접근, 대용량 파일 업로드를 하는 경우에 그대로 application에 전달 되는 문제점이 발생합니다.
            - 도입 이유
                - 불필요한 트래픽이 발생하여, 비용이 추가적으로 발생하게 됩니다.
                - 대용량 파일 업로드를 하게 되면 S3 스토리지에 대한 과한 비용이 발생 할 수 있음으로, 대용량 파일 업로드를 방지하고자 합니다.
                - WAF을 활용하여, 추가적인 보안을 확장할 수 있습니다. 
            - 구현 방식
                - AWS WAF의 기존의 제한 방식을 추가로 사용 또는 자기 자신이 만든 제한을 추가하여 EC2 원본 서버의 접근을 앞단에서 막게 합니다.
                    - 10MB 이상의 파일 업로드 차단
                    - 특정 국가/지역의 접근을 제한
                    - SQL Injection 및 XSS 공격 탐지 및 차단
                - CloudFront와 S3의 보안 설정 강화
                    - CloudFront에서만 S3 원본 접근 허용 (Origin Access Control)
                    - 파일 업로드 시, 특정 출처(Origin)만 허용
                    - CloudFront의 캐싱 무효화 정책 활성화
                - 사진 첨부
                    - WAF 대용량 파일 업로드 차단 규칙
                        ```json
                        {
                          "Name": "LimitRequestBodySize",
                          "Priority": 3,
                          "Statement": {
                            "SizeConstraintStatement": {
                              "FieldToMatch": {
                                "JsonBody": {
                                  "MatchPattern": {
                                    "All": {}
                                  },
                                  "MatchScope": "ALL",
                                  "OversizeHandling": "MATCH"
                                }
                              },
                              "ComparisonOperator": "GT",
                              "Size": 10485760,
                              "TextTransformations": [
                                {
                                  "Priority": 0,
                                  "Type": "NONE"
                                }
                              ]
                            }
                          },
                          "Action": {
                            "Block": {}
                          },
                          "VisibilityConfig": {
                            "SampledRequestsEnabled": true,
                            "CloudWatchMetricsEnabled": true,
                            "MetricName": "LimitRequestBodySize"
                          }
                        }
                        ```
                    - CloudFront의 WAF 설정을 통한 특정 국가/지역의 접근 제한
                        ![cloudFront_blocked_special_country](https://github.com/user-attachments/assets/5dd6ae56-55c4-4490-9a0a-ec6ec4851617)
            - 기대 효과
                - 서버 부하 방지 및 악성 트래픽 차단
                - 대용량 요청에 대하여 트래픽 제한으로 인프라 비용 절감   
        - 확장 가능성
            - 문제 상황   
                application 레벨이 아닌 앞단 레벨의 문제점을 전부 로깅으로 기록할 수 없다는 문제점이 발생합니다.
            - 도입 이유
                - 로깅 검사를 application에서 로깅을 저장하는 것과 WAF에서도 로깅을 기록하여, 장애 감지 및 분석을 추가적으로 하여, 에러 방지에 힘쓰고자 합니다.
                - AWS 인프라를 사용하고 있음으로 WAF뿐만 아니라, CloudWatch의 추가 확장을 고려할 수 있습니다.
            - 차후 구현 방식
                - AWS CloudWatch Logs 및 Metrics 적용을 통한 CloudFront, EC2, ALB 상태 모니터링을 효율적으로 할 수 있습니다.
                - ELK Stack을 통한 API 요청 로그 시각화 및 분석을 할 수 있습니다.
            - 기대 효과
                - 실시간 장애 감지 및 빠른 대응 가능
            | 카테고리 | 문제 상황 | 도입 이유 | 구현 방식 | 기대 효과 |
            | --- | --- | --- | --- | --- |
            | **JWT 인증** | 세션 기반 인증은 서버 부담 증가 | 유지보수 비용 절감, 확장성 고려 | AccessToken + RefreshToken 구조, WebSocket에서도 사용 | XSS, CSRF 방어 및 인증 통합 |
            | **HTTPS + Secure Cookie** | HTTP 프로토콜 사용 시 보안 취약 | MITM 공격 방어, JWT 토큰 보호 | AWS ACM TLS 적용, HttpOnly Secure Cookie 설정 | 데이터 암호화 및 클라이언트 접근 차단 |
            | **인프라 보안 (WAF + CloudFront + S3)** | 비정상적인 API 접근 및 대용량 파일 업로드 문제 | 불필요한 트래픽 차단 및 비용 절감 | WAF 차단 규칙 적용, CloudFront + S3 OAC 활용 | 서버 부하 방지, 악성 트래픽 차단 |
            | **인프라 기반 확장 가능성 (CloudWatch + ELK)** | 로깅 부족으로 문제 분석 어려움 | 장애 감지 및 분석 강화 | AWS CloudWatch, WAF 로그 분석, ELK Stack 연계 | 실시간 모니터링 및 최적화 가능 |
    </details>

   <details>
       <summary>반복되는 작업 공통화</summary>
        - 문제 상황
            현재 인사 담당자가  매일 출근할 때마다 개별적으로 근태 기록을 생성하고, 휴가가 끝난 멤버의 상태를 직접 변경해야 하는 번거로움이 있음. 또한, 연말이 되면 수작업으로 연간 데이터를 정리해야 하는 문제가 발생. 이를 자동화하여 인사 담당자의 업무 부담을 줄이고, 실수 없이 일관된 처리를 수행할 방법을 모색
        - 도입 이유 
            1. 관리자의 업무를 자동화하여, 일의 부담감을 덜고자 함. 
            2. 모든 직원은 실수로 발생할 수 있는 데이터를 일관된 업무 처리로 오류를 최소화함.
            3. 신규 멤버는 일관된 프로세스를 통해 적응 속도를 향상시키고 조직 전체의 운영 효율성을 높일 수 있음.
        - 구현 방식
            - DailyScheduler, MonthlyScheduler, YearlyScheduler를 나누어 실행하게 함.
            1. DailyScheduler : 당일에 처리해야 할 업무를 자동화
            2. MonthlyScheduler: 달 단위에 처리해야 할 업무를 자동화
            3. YearlyScheduler : 연 단위에 처리해야 할 업무를 자동화
        - 기대 효과
            1. 업무가 치중되어 있는 관리자의 업무가 자동화되어 전략적 업무에 집중할 수 있게 됨.
            2. 모든 멤버가 일괄적인 업무 처리에 따라 빠른 업무 적응 속도가 기대됨.
   </details>

   <details>
       <summary>DB 성능 최적화</summary>
       1. 다양한 DB
            1. 문제 상황
                - 기존 인프라는 MySQL만 사용하고 있었으나, 특정 엔티티의 데이터가 수십만 개 이상이 쌓이면서 밑과 같은 문제가 발생함.
                1. MySQL에서 특정 엔티티 대상으로 복잡한 조회에 대해서 성능이 급격히 저하됨.
                2. RefreshToken은 자주 변경되어지는 데이터임으로 MySQL과 맞지 않음.
                3. 바뀌지 않는 데이터가 Server에 똑같은 SELECT 조회문을 날려 불필요한 API 요청을 줄 일 필요가 있다고 생각함.
            2. 도입 이유
                1. MongoDB : 인덱싱 성능이 우수하고, JSON 기반 문서형 저장소가 복잡한 쿼리에 적합
                2. Redis : TTL 설정 가능, 메모리 기반이므로 빠른 조회 및 삭제 가능
                3. localStorage & Redis : 서버 부하를 줄이기 위해 Redis와 localStorage를 함께 사용
            3. 구현 방식
                1. MongoDB
                    - MySQL의 경우 인덱스 최적화를 하더라도 JOIN 연산이 많아지면 성능이 떨어짐.
                    - MongoDB는 JSON 기반 문서형 DB로, 대량 데이터를 효율적으로 저장하고 조회할 수 있음.
                    - 조회 최적화에 구현 방식을 같이 확인 가능.
                2. Redis
                    - Jwt Token을 통한 RefreshToken을 구현함.
                    - 해당 RefreshToken은 Access Token의 상태에 따라 처리 방식이 변경
                        - RefreshToken ACCESS
                            - AccessToken ACCESS : 해당 프로세스 진행
                            - AccessToken EXPIRED : AccessToken만 재발급, 프로세스 진행
                            - AccessToken FAIL : 403 에러
                        - RefreshToken EXPIRED
                            - AccessToken ACCESS : RefrehsToken만 재발급, 프로세스 진행
                            - AccessToken EXPIRED : AccessToken, RefreshToken 재발급, 프로세스 진행
                            - AccessToken FAIL : 403 에러
                        - FAIL → 403 에러
                    - 위와 같은 검증을 하고, RefreshToken은 RefreshTokenRepository에 저장
                    - 이미지 첨부
                3. CacheData → Client & Redis
                    1. Client의 localStorage에 저장하는 방식
                        1. 네트워크 요청 없이 즉시 사용 가능
                        2. 보안 문제로 인해 중요한 데이터 저장 불가
                    2. Redis를 활용하여 저장하는 방식
                        1. 빠른 조회 및 검증 가능
                        2. 네트워크 요청이 필요하여 localStorage보다는 상대적으로 느림
                    - 따라서, 덜 중요한 정보는 localStorage에 저장하고, 중요한 정보는 Redis에 저장하는 방식으로 혼합하여 사용
                    - 이미지 첨부 :
                        - localStorage :
                        - 휴가 정정 기간(데이터베이스 2) :
                        - 예정된 근태 기록(데이터베이스 3) :
            4. 기대 효과
                1. 백 만개의 데이터가 존재할 때, MongoDB에서 조회 속도를 약 6~8배 성능을 향상시킬 수 있음. 
                2. RefreshToken을 Redis를 통한 관리를 하여, 간단하게 TTL 설정하고 XSS, CSRF, MITM과 같은 공격을 방지할 수 있어 안전하게 구성할 수 있음.
                3.  Redis를 캐시로 활용하여 서버 부하를 줄이고 빠른 응답 속도 제공
        2. 조회 최적화
            1. 문제 상황
                - MySQL : 많은 필터링으로 인하여 쿼리의 복잡도가 올라가고 다른 엔티티와의 연관관계가 복잡해짐으로써 CRUD의 성능에 영향을 미침.
                - MongoDB : 해당 데이터베이스로 변경했음에도, 조회 속도 개선이 되지 않음.
            2. 도입 이유
                1. Indexing : 특정 필드를 하이라이팅하여, 더 빠른 조회 속도를 부여할 수 있게 함.
                2. Page : 모든 데이터를 반환하는 것이 아닌, 사용자가 요구한 데이터만을 적절히 반환하여, 속도를 높이고자 함.
                3. 필요한 필드만 SELECT : 모든 엔티티에 대한 조회를 하는 것이 아닌, 반환이 필요한 부분만 SELECT하여, 대용량의 데이터를 효율적으로 조회할 수 있게 함.
            3. 구현 방식
                1. Indexing : 
                    - 특정 엔티티 두 개만 선별해서 작성함. 밑의 ‘DB 성능 최적화’에 상세히 기록함.
                    1. Attendances
                        - Index status
                        - attendancesStatus : 모든 구성원들이 출결 상태에 대한 상태 업데이트가 많이 될 것이라 판단하여, index를 하지 않음
                        - checkInTime, checkOutTime : 위와 같은 이유로 예상됨.
                        - memberId : 멤버 아이디는 외래키로 개인 출결, 관리자 페이지의 조회가 많이 일어나면서, 해당 memberId는 생성/수정/삭제의 연산이 일어날 일이 드물다.
                        - createdAt : createdAt 필드는 해당 출결 날짜에 해당하며, 스케줄러에 의한 Attendances ‘생성’은 매일 일어나고 ‘수정/삭제’는 거의 없지만, 멤버들이 출결 상태/관리자 근태 관리 페이지 조회가 자주 일어나는 것을 감안하면, INDEX 처리가 적절하다고 생각
                    2. Notifcations 
                        - toMemberName, fromMemberName : 누가, 누구한테 전달 받았을 지에 대한 정보를 줘야함.
                        - isRead
                            - isRead는 isRead = false에 해당하는 알림 조회가 많음
                            - isRead는 상태 변경이 많지만, 알림 1개당 단 한 번의 상태 변경만 이루어짐.
                                - 초기 상태는 isRead = false → isRead = true 상태로의 변경만 하기 때문
                            - Notifications 특성 상, 알림을 조회가 많이 이루어지기에 읽기 최적화가 더 중요함.
                                - MongoDB 특성상 오버헤드가 일어날 수도 있음.
                2. 페이징 조회
                    - 전체 데이터를 가지고 오는 것이 아닌 개발자의 요청에 따른 일부 데이터만 가지고 옴.
                    - 일부 데이터 요청에 LIMIT, OFFSET, ORDER BY등을 활용하여 조회함.
                    - 장점
                        - **조회 성능 향상**: 전체 데이터 조회보다 빠름.
                        - **메모리 사용량 감소**: 불필요한 데이터 로딩 방지.
                        - **네트워크 부하 감소**: 클라이언트로 전송하는 데이터가 줄어듦.
            4. 기대 효과
                1. 대용량의 데이터를 조회하는 속도가 개선됨.
                2. 서버에서도 DB에게 필요한 데이터의 필드만 받아서 사용하기에 부하가 줄어듦.
            5. DB 성능 최적화 (성능 테스트 환경에 따른 결과값)
   </details>
