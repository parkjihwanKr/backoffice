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
    
4. 서비스 아키텍처
    
    ![최종 인프라스트럭처.png](%EC%B5%9C%EC%A2%85_%EC%9D%B8%ED%94%84%EB%9D%BC%EC%8A%A4%ED%8A%B8%EB%9F%AD%EC%B2%98.png)
    
5. **UI/UX**
    - 로그인/회원가입
        
        ![로그인v1000.png](%EB%A1%9C%EA%B7%B8%EC%9D%B8v1000.png)
        
        ![회원가입v1000.png](%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85v1000.png)
        
    - 메인 페이지
        
        ![메인 페이지v1.png](%EB%A9%94%EC%9D%B8_%ED%8E%98%EC%9D%B4%EC%A7%80v1.png)
        
    - 게시글
        - 전체
            
            ![게시판페이지v1000.png](%EA%B2%8C%EC%8B%9C%ED%8C%90%ED%8E%98%EC%9D%B4%EC%A7%80v1000.png)
            
            ![전체게시글v1000.png](%EC%A0%84%EC%B2%B4%EA%B2%8C%EC%8B%9C%EA%B8%80v1000.png)
            
        - 부서
            
            ![게시글부서선택v1000.png](%EA%B2%8C%EC%8B%9C%EA%B8%80%EB%B6%80%EC%84%9C%EC%84%A0%ED%83%9Dv1000.png)
            
            ![부서게시글작성v1000.png](%EB%B6%80%EC%84%9C%EA%B2%8C%EC%8B%9C%EA%B8%80%EC%9E%91%EC%84%B1v1000.png)
            
            ![세일즈게시글특정조건v1000.png](%EC%84%B8%EC%9D%BC%EC%A6%88%EA%B2%8C%EC%8B%9C%EA%B8%80%ED%8A%B9%EC%A0%95%EC%A1%B0%EA%B1%B4v1000.png)
            
            ![세일즈게시글하나조회v1000.png](%EC%84%B8%EC%9D%BC%EC%A6%88%EA%B2%8C%EC%8B%9C%EA%B8%80%ED%95%98%EB%82%98%EC%A1%B0%ED%9A%8Cv1000.png)
            
            ![답글작성v1000.png](%EB%8B%B5%EA%B8%80%EC%9E%91%EC%84%B1v1000.png)
            
    - 일정
        - 부서
            
            ![일정페이지v1000.png](%EC%9D%BC%EC%A0%95%ED%8E%98%EC%9D%B4%EC%A7%80v1000.png)
            
            ![일정부서선택v1000.png](%EC%9D%BC%EC%A0%95%EB%B6%80%EC%84%9C%EC%84%A0%ED%83%9Dv1000.png)
            
            ![부서일정표v1000.png](%EB%B6%80%EC%84%9C%EC%9D%BC%EC%A0%95%ED%91%9Cv1000.png)
            
        - 개인
            
            ![개인일정v1000.png](%EA%B0%9C%EC%9D%B8%EC%9D%BC%EC%A0%95v1000.png)
            
            ![개인일정선택v1000.png](%EA%B0%9C%EC%9D%B8%EC%9D%BC%EC%A0%95%EC%84%A0%ED%83%9Dv1000.png)
            
            ![휴가신청v1000.png](%ED%9C%B4%EA%B0%80%EC%8B%A0%EC%B2%ADv1000.png)
            
    - 개인 근태 기록
        
        ![개인근태기록페이지v1000_1.png](%EA%B0%9C%EC%9D%B8%EA%B7%BC%ED%83%9C%EA%B8%B0%EB%A1%9D%ED%8E%98%EC%9D%B4%EC%A7%80v1000_1.png)
        
    - 알림
        - 개인 알림 모달
            
            ![개인알림페이지v1000.png](%EA%B0%9C%EC%9D%B8%EC%95%8C%EB%A6%BC%ED%8E%98%EC%9D%B4%EC%A7%80v1000.png)
            
    - 관리자 페이지
        - 휴가
            
            ![휴가관리v1000.png](%ED%9C%B4%EA%B0%80%EA%B4%80%EB%A6%ACv1000.png)
            
        - 근태
            
            ![근태관리v1000.png](%EA%B7%BC%ED%83%9C%EA%B4%80%EB%A6%ACv1000.png)
            
            ![당일근태관리v1.png](%EB%8B%B9%EC%9D%BC%EA%B7%BC%ED%83%9C%EA%B4%80%EB%A6%ACv1.png)
            
        - 알림
            
            ![1942.png](1942.png)
            
            ![1942v2.png](1942v2.png)
            
            ![1942v3.png](1942v3.png)
            
        - 인사
            
            ![인사관리v1.png](%EC%9D%B8%EC%82%AC%EA%B4%80%EB%A6%ACv1.png)
            
        - 감사
            
            ![감사관리v1.png](%EA%B0%90%EC%82%AC%EA%B4%80%EB%A6%ACv1.png)
            
    
6. **Directory 구조**
    - Directory 구조
        
        ```
        └─src
            ├─main
            │  ├─java
            │  │  └─com
            │  │      └─example
            │  │          └─backoffice
            │  │              ├─domain
            │  │              │  ├─answer
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─attendance
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─board
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─facade
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─comment
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─evaluation
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─facade
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─event
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─facade
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─favorite
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─file
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─mainPage
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  └─service
            │  │              │  ├─member
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─facade
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─memberAnswer
            │  │              │  │  ├─converter
            │  │              │  │  ├─entity
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─memberEvaluation
            │  │              │  │  ├─converter
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─notification
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─facade
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─question
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  ├─reaction
            │  │              │  │  ├─controller
            │  │              │  │  ├─converter
            │  │              │  │  ├─dto
            │  │              │  │  ├─entity
            │  │              │  │  ├─exception
            │  │              │  │  ├─repository
            │  │              │  │  └─service
            │  │              │  └─vacation
            │  │              │      ├─controller
            │  │              │      ├─converter
            │  │              │      ├─dto
            │  │              │      ├─entity
            │  │              │      ├─exception
            │  │              │      ├─facade
            │  │              │      ├─repository
            │  │              │      └─service
            │  │              └─global
            │  │                  ├─aop
            │  │                  ├─audit
            │  │                  │  ├─controller
            │  │                  │  ├─converter
            │  │                  │  ├─dto
            │  │                  │  ├─entity
            │  │                  │  ├─repository
            │  │                  │  └─service
            │  │                  ├─awss3
            │  │                  ├─common
            │  │                  ├─config
            │  │                  ├─converter
            │  │                  ├─date
            │  │                  ├─dto
            │  │                  ├─exception
            │  │                  ├─initializer
            │  │                  ├─jwt
            │  │                  │  ├─controller
            │  │                  │  ├─dto
            │  │                  │  ├─interceptor
            │  │                  │  └─service
            │  │                  ├─loadBalancer
            │  │                  ├─redis
            │  │                  ├─scheduler
            │  │                  └─security
            │  └─resources
            │      └─static
        
        ```
        
    
7. **Technical Decision**
    - 프론트
        1. 문제 상황
            
            PostMan 또는 다른 방식을 통해 API 요청을 보여주는 개발 방식 보다는 해당 서비스가 직접적으로 이루어지는 과정을 직접 보여주고자 하는데, 적절하게 사용할 수 있는 프론트 기술을 알지 못하는 문제에 직면함.
            
        2. 도입 이유
            1. 특정 API의 요청/응답을 보여주는 것이 아닌, 직접적으로 요청/응답이 가는 형태를 단 하나의 화면으로 보여줄 수 있게 됨.
            2. 백엔드 개발자라도 프론트의 데이터 요청/응답 흐름을 알고 있어야 한다고 생각함.
        3. 프레임 워크 결정
            1. ReactJs
                - 직접 다루어 본 적이 존재해, 비교적 낮은 러닝 커브
                - 수많은 라이브러리들이 존재하여 유지 보수가 용이
            2. VueJs
                - Vuex, Pinia를 활용한 간단한 상태 관리
                - 이전 프로젝트에서 사용해본 경험 존재
                - 상대적으로 높은 러닝 커브
                - 라이브러리가 상대적으로 적어 유지 보수가 어려움
            3. 결론
                - 아래와 같은 이유로 ReactJs를 통한 프론트를 구성하고자 함.
                - 대량의 데이터 관리
                    - 대량의 데이터 처리를 필터링, 테이블, 페이지을 통해 구성 → SPA(Single Page Application) 구조를 통하여 다량의 데이터를 빠르게 렌더링이 가능함.
                - 보안
                    - JWT 기반 인증 적용 → Spring Boot 백엔드와 연동하여 토큰 기반 인증을 함.
                    - CSRF, XSS 방어 → React는 기본적으로 클라이언트 렌더링을 사용하여 서버 측 렌더링보다 CSRF 위험이 적음
                - 확장성 & 유지보수
                    - 컴포넌트 기반 개발로 재사용성이 높음 → 유지보수 시 특정 컴포넌트만 수정하여 빠른 대응 가능
                    - AWS S3 + CloudFront 배포와 호환성 높음 → 정적 사이트로 빌드하여 빠르게 배포 가능
        4. 구현 방식
            1. 대량의 데이터 관리
                - 필터를 재사용 컴포넌트를 만들어 사용하고 각각의 특징이 두드러지는 테이블과 페이지는 따로 만들어 대량 데이터를 관리를 밑과 같은 코드로 관리합니다.
                    - FilterDropDown
                        - 다양한 필터 타입을 지원하여 유연한 데이터 검색 가능하게 합니다.
                            - checkbox, input, selct 타입을 유연하게 사용할 수 있게 합니다.
                        - 사용자가 원하는 조건을 쉽게 선택할 수 있도록 UI를 제공하며, useState를 활용해 상태를 관리함.
                        - FilterDropDown
                            
                            ```jsx
                            import React from 'react';
                            import './FilterDropDown.css';
                            import CloseImageButton from "../ui/image/CloseImageButton";
                            
                            const FilterDropDown = ({
                                                        showFilters,
                                                        filters,
                                                        setFilters,
                                                        filterOptions,
                                                        onSubmit,
                                                        onReset,
                                                        setShowFilters,
                                                        showResetButton = true,
                                                    }) => {
                                if (!showFilters) return null;
                            
                                return (
                                    <div className="custom-filters-dropdown">
                                        <h3 className="custom-filters-dropdown-header">필터 적용</h3>
                                        <CloseImageButton handleClose={() => setShowFilters(!showFilters)} />
                                        {filterOptions.map((filter) => {
                                            if (filter.type === 'select') {
                                                return (
                                                    <div key={filter.name}
                                                         className={`custom-filter-item ${filter.type === 'checkbox' ? 'checkbox-item' : ''}`}>
                                                        <label htmlFor={filter.name}>{filter.label}:</label>
                                                        <select
                                                            id={filter.name}
                                                            value={filters[filter.name] || ''}
                                                            onChange={(e) =>
                                                                setFilters({
                                                                    ...filters,
                                                                    [filter.name]: e.target.value || null,
                                                                })
                                                            }
                                                        >
                                                            <option value="">전체</option>
                                                            {filter.options.map((option) => (
                                                                <option key={option.value} value={option.value}>
                                                                    {option.label}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    </div>
                                                );
                                            } else if (filter.type === 'checkbox') {
                                                return (
                                                    <div key={filter.name} className="custom-filter-item my-checkbox-item">
                                                        <label htmlFor={filter.name}>{filter.label}</label>
                                                        <input
                                                            type="checkbox"
                                                            id={filter.name}
                                                            checked={!!filters[filter.name]}
                                                            onChange={(e) =>
                                                                setFilters({
                                                                    ...filters,
                                                                    [filter.name]: e.target.checked || null,
                                                                })
                                                            }
                                                        />
                                                    </div>
                                                );
                                            } else if (filter.type === 'input') {
                                                return (
                                                    <div key={filter.name} className="custom-filter-item">
                                                        <label htmlFor={filter.name}>{filter.label}:</label>
                                                        <input
                                                            id={filter.name}
                                                            type={filter.inputType}
                                                            placeholder={filter.placeholder}
                                                            value={filters[filter.name] || ''}
                                                            onChange={(e) =>
                                                                setFilters({
                                                                    ...filters,
                                                                    [filter.name]: e.target.value || null,
                                                                })
                                                            }
                                                        />
                                                    </div>
                                                );
                                            }
                                            return null;
                                        })}
                            
                                        <div className="custom-filter-actions">
                                            <button onClick={onSubmit}>조회</button>
                                            {showResetButton && <button onClick={onReset}>초기화</button>}
                                        </div>
                                    </div>
                                );
                            };
                            
                            export default FilterDropDown;
                            
                            ```
                            
                    - 테이블 스타일은 한 곳에서 관리하도록 집중하고 일관된 디자인을 할 수 있도록 합니다.
                        - Table.css
                            
                            ```jsx
                            .custom-table {
                                width : 90%;
                                margin: 0 auto;
                                border-collapse: separate; /* separate로 설정해야 둥근 테두리가 적용됨 */
                                border-spacing: 0; /* 셀 간의 간격을 없앰 */
                                overflow: hidden; /* 둥근 테두리 바깥 영역이 잘리지 않도록 설정 */
                                border-radius: 12px; /* 원하는 만큼 둥글게 설정 */
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 테두리 강조 효과 */
                                max-width: 1200px;
                            }
                            
                            .custom-table th {
                                padding: 10px;
                                font-size: 15px !important;
                                border: 1px solid #ddd;
                                text-align: center;
                                font-weight: bold; /* 헤더 폰트 두껍게 */
                                background-color: #f5f5f5; /* 헤더 배경색 */
                                word-wrap: break-word; /* 내용이 넘칠 경우 줄바꿈 */
                                white-space: normal; /* 줄바꿈 가능 */
                            }
                            
                            .custom-table th, td {
                                word-wrap: break-word;
                                font-weight: bold;
                                max-width: 100px !important;
                                min-width: 50px !important;
                            }
                            
                            .custom-table td {
                                font-size: 13px !important;
                            }
                            
                            .custom-table td img {
                                cursor: pointer;
                            }
                            
                            .custom-table tbody tr:last-child td:first-child {
                                border-bottom-left-radius: 12px;
                            }
                            
                            /* 나머지 스타일 */
                            .custom-table th, .custom-table td {
                                padding: 10px;
                                border: 1px solid #ddd;
                                text-align: center;
                                vertical-align: middle;
                            }
                            
                            @media(max-width: 768px) {
                                .custom-table th, .custom-table td {
                                    min-width: 25px !important;
                                    max-width: 50px !important;
                                    font-size: 10px; /* 폰트 크기를 줄여 모바일에서 가독성 확보 */
                                    padding: 10px; /* 모바일에서 패딩 간격 축소 */
                                }
                            
                                .custom-table {
                                    font-size: 12px;
                                }
                            }
                            ```
                            
                    - PaginationFooter
                        - 여러 페이지에서 동일한 페이징 UI를 사용할 수 있도록 재사용 가능한 컴포넌트로 구성하여 유지보수성을 향상시켰습니다.
                        - 현재 페이지를 중심으로 최대 5개의 페이지 버튼(이전 2개, 이후 2개)을 노출하는 방식으로 구현하여 가독성을 높힙니다.
                        - PaginationFooter
                            
                            ```jsx
                            import React from "react";
                            import "./PaginationFooter.css";
                            
                            const PaginationFooter = ({ currentPage, totalPages, onPageChange }) => {
                            
                                // 현재 페이지를 기준으로 시작 페이지와 끝 페이지 설정
                                const startPage = Math.max(0, currentPage - 2); // 최소 0 페이지
                                const endPage = Math.min(totalPages - 1, currentPage + 2); // 최대 totalPages - 1 페이지
                            
                                // startPage와 endPage를 기준으로 페이지 배열 생성
                                const pageNumbers = [];
                                for (let i = startPage; i <= endPage; i++) {
                                    pageNumbers.push(i);
                                }
                            
                                return (
                                    <div className="custom-pagination">
                                        <nav>
                                            <ul className="custom-pagination-ul">
                                                <li className={`custom-page-item ${currentPage === 0 ? 'disabled' : ''}`}>
                                                    <button className="page-link" onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 0}>
                                                        이전
                                                    </button>
                                                </li>
                                                {pageNumbers.map((pageNumber) => (
                                                    <li key={pageNumber} className={`custom-page-item ${pageNumber === currentPage ? 'active' : ''}`}>
                                                        <button
                                                            onClick={() => onPageChange(pageNumber)}
                                                            className="page-link"
                                                        >
                                                            {pageNumber + 1}
                                                        </button>
                                                    </li>
                                                ))}
                                                <li className={`custom-page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}`}>
                                                    <button className="page-link" onClick={() => onPageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                                                        다음
                                                    </button>
                                                </li>
                                            </ul>
                                        </nav>
                                    </div>
                                );
                            };
                            
                            export default PaginationFooter;
                            
                            ```
                            
            2. 보안
                - 프로젝트에서 JWT 기반 사용자 인증 적용을 아래와 같은 모듈로 수행합니다.
                    - AuthProvider
                        - 사용자의 인증 상태를 관리하는 역할을 합니다.
                        - checkAuth()를 실행하여 서버에서 인증 상태를 확인한 후, 사용자 정보를 useState로 관리
                        - 사용자 정보를 **LocalStorage**에 저장하여 페이지 새로고침 시에도 유지
                        - 다양한 필드들(isAuthenticated, id, name…)을 Context로 제공하여 인증된 사용자의 정보를 쉽게 활용 가능
                            - isAuthenticated = ture : localStorage에 특정 정보 저장 및 Context Provider를 통해 다른 컴포넌트도 해당 정보를 유지
                            - isAuthenticated = false : 403에러 반환
                        - AuthProvider
                            
                            ```jsx
                            import React, {createContext, useContext, useEffect, useState} from 'react';
                            import {checkAuth} from "../services/AuthService";
                            
                            const AuthContext = createContext();
                            
                            export const AuthProvider = ({ children }) => {
                                const [isAuthenticated, setIsAuthenticated] = useState(false);
                                const [id, setId] = useState('');
                                const [name, setName] = useState('');
                                const [department, setDepartment] = useState('');
                                const [position, setPosition] = useState('');
                                const [profileImageUrl, setProfileImageUrl] = useState('') ;
                                const [loading, setLoading] = useState(true);
                            
                                useEffect(() => {
                                    const initializeAuth = async () => {
                                        try {
                                            const response = await checkAuth(); // 서버 인증 확인
                            
                                            if (response) {
                                                const { id, name, department, position, profileImageUrl } = response;
                            
                                                setIsAuthenticated(true);
                                                setId(id);
                                                setName(name);
                                                setDepartment(department);
                                                setPosition(position);
                                                setProfileImageUrl(profileImageUrl);
                            
                                                // LocalStorage에 저장
                                                localStorage.setItem('isAuthenticated', JSON.stringify(true));
                                                localStorage.setItem('id', id);
                                                localStorage.setItem('name', name);
                                                localStorage.setItem('department', department);
                                                localStorage.setItem('position', position);
                                                localStorage.setItem('profileImageUrl', profileImageUrl);
                                            }
                                        } catch (error) {
                                            console.error("인증 실패 : ", error);
                            
                                            if (error.response && error.response.status === 403) {
                                                const storedAuth = JSON.parse(localStorage.getItem('isAuthenticated'));
                                                if (storedAuth) {
                                                    setIsAuthenticated(storedAuth);
                                                }
                                            } else {
                                                setIsAuthenticated(false);
                                            }
                                        } finally {
                                            setLoading(false); // 로딩 완료
                                        }
                                    };
                            
                                    // LocalStorage의 값이 있으면 초기 상태 설정
                                    const storedAuth = JSON.parse(localStorage.getItem('isAuthenticated'));
                                    if (storedAuth) {
                                        setIsAuthenticated(storedAuth);
                                    }
                            
                                    initializeAuth(); // 한 번만 실행
                                }, []); // 의존성 배열에서 isAuthenticated를 제거
                            
                                if (loading) {
                                    return <div>Loading...</div>;
                                }
                            
                                return (
                                    <AuthContext.Provider value={{ isAuthenticated, id, name, department, position, profileImageUrl, setProfileImageUrl }}>
                                        {children}
                                    </AuthContext.Provider>
                                );
                            };
                            
                            export const useAuth = () => useContext(AuthContext);
                            
                            ```
                            
                    - AxoisUtils
                        - Axios 인스턴스를 생성하여, 공통적인 HTTP 요청 설정을 적용하는 역할을 합니다.
                        - 인증이 필요 여부에 따른 api 요청을 구분합니다.
                            - apiBaseUrl : env파일에 특정 api 경로를 지정하고 개발, 배포 환경에 따른 요청 경로를 보냅니다.
                            - axoisUnAuthenticated : signup, login에 대한 api 요청은 jwt token의 요청이 불필요한 요청을 처리합니다.
                            - axoisAuthenticated : jwt token 요청을 무조건 처리한 API 요청을 보냅니다.
                            - interceptors : 요청 시, jwt token 토큰을 추가하고 이에 맞는 응답 처리합니다.
                        - AxiosUtils
                            
                            ```jsx
                            import axios from 'axios';
                            
                            // 공통 baseURL 설정
                            const apiBaseUrl = process.env.REACT_APP_API_BASE_URL;
                            
                            // axios
                            export const axiosUnAuthenticated = axios.create({
                                baseURL: apiBaseUrl,
                                headers : {
                                    'Content-Type' : 'application/json',
                                },
                            })
                            
                            // axios 인스턴스 생성
                            export const axiosInstance = axios.create({
                                baseURL: apiBaseUrl,  // 공통 base URL
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                withCredentials: true  // 쿠키 전송을 위해 credentials 포함
                            });
                            
                            // 요청 인터셉터: 각 요청마다 Authorization 헤더에 accessToken을 자동으로 추가
                            axiosInstance.interceptors.request.use(config => {
                                return config;
                            }, error => {
                                return Promise.reject(error);
                            });
                            
                            // 응답 인터셉터: 403 처리
                            axiosInstance.interceptors.response.use(response => {
                                return response;
                            }, error => {
                                if (error.response) {
                                    if (error.response.status === 403) {
                                        console.warn("403 Forbidden: 인증되지 않은 요청입니다.");
                                    }
                                }
                                return Promise.reject(error);
                            });
                            
                            ```
                            
                    - AuthService
                        - 회원가입, 로그인, 로그아웃, 인증 확인 등의 API 요청을 담당하는 서비스 모듈입니다.
                        - chekAuth : 요청을 통해 현재 사용자 인증 상태 확인합니다.
                        - getAccessToken : refreshToken의 만료 시에, 요청을 통해 새 액세스 토큰 발급합니다.
                        - AuthService
                            
                            ```jsx
                            // services/authService.js
                            import {axiosInstance, axiosUnAuthenticated} from "../../../utils/AxiosUtils";
                            
                            export const signup = async (signupData) => {
                                const response
                                    = await axiosUnAuthenticated.post('/signup', signupData);
                                return response.data;
                            };
                            
                            export const login = async (memberName, password) => {
                                const response
                                    = await axiosUnAuthenticated.post(`/login`, {
                                        memberName : memberName,
                                        password : password}, {
                                    withCredentials: true
                                });
                                console.log("login process response : "+response);
                                return response.data;
                            }
                            
                            export const checkAuth = async () => {
                                const response = await axiosInstance.get(`/check-auth`);
                                return response.data.data;
                            }
                            
                            export const checkUsernameAvailability = async (memberName) => {
                                const response
                                    = await axiosUnAuthenticated.get(`/check-available-memberName`,{
                                        params : {
                                            memberName
                                        }
                                    });
                                return response.data;
                            }
                            
                            export const logout = async () => {
                                const response = await axiosInstance.post(`/logout`);
                                return response.data;
                            };
                            
                            export const getAccessToken = async () => {
                                const response = await axiosInstance.get(`/access-token`);
                                return response.data.data;
                            }
                            
                            ```
                            
        5. 기대 효과
            - 빠른 데이터 검색 & 렌더링 최적화로 사용자 경험이 향상됩니다.
            - 안전한 사용자 인증 & 데이터 보호할 수 있습니다.
            
    - 보안을 강화한 인프라
        1. JWT Token 보안 강화
            1. 문제 상황
                
                기존의 Spring boot의 Security를 사용해, Session을 통한 제어를 하는 방식은 서버에 많은 부담을 주며, 직원이 많은 환경에서는 서버를 여러 대 둬야 하는데, 같은 세션을 공유하는 저장소를 확장 해야하는 유지 보수 비용이 불필요하게 발생하게 됩니다.
                
            2. 도입 이유 
                1. 기업의 내부 정보의 안정성 증가 
                2. Jwt Token
                    - 상대적으로 낮은 유지 보수 비용
                    - 높은 개발 친화성
                    - 확장성
            3. 구현 방식
                - JWT Token은 Access Token은 만료 기간은 1시간, RefreshToken은 만료 기간이 1일이며, RefreshToken은 Redis를 통한 저장을 하고 있습니다.
                1. Jwt Token이 필요 없는 API 요청 : 로그인, 회원 가입, 이름 중복 체크에 관련된 요청은 요청 헤더에 Jwt Token을 넣지 않은 API 요청을 합니다.
                2. 일반적인 API 요청 : 모든 요청은 요청 헤더에 Cookie를 담아서 요청을 보내야 합니다.
                3. WebSocket 요청 : Cookie에 저장되어 있는 accessToken을 Authorization : Bearer your_jwt_token과 같은 형식으로 요청 헤더에 담아서 요청합니다.
                4. 코드 첨부
                    - JwtAuthorizationFilter
                        
                        ```java
                        package com.example.backoffice.global.jwt;
                        
                        import com.example.backoffice.domain.member.entity.MemberRole;
                        import com.example.backoffice.global.exception.GlobalExceptionCode;
                        import com.example.backoffice.global.exception.JwtCustomException;
                        import com.example.backoffice.global.redis.RefreshTokenRepository;
                        import com.example.backoffice.global.security.MemberDetailsImpl;
                        import jakarta.servlet.FilterChain;
                        import jakarta.servlet.ServletException;
                        import jakarta.servlet.http.HttpServletRequest;
                        import jakarta.servlet.http.HttpServletResponse;
                        import lombok.RequiredArgsConstructor;
                        import lombok.extern.slf4j.Slf4j;
                        import org.springframework.beans.factory.annotation.Value;
                        import org.springframework.http.ResponseCookie;
                        import org.springframework.security.core.Authentication;
                        import org.springframework.security.core.context.SecurityContext;
                        import org.springframework.security.core.context.SecurityContextHolder;
                        import org.springframework.util.StringUtils;
                        import org.springframework.web.filter.OncePerRequestFilter;
                        
                        import java.io.IOException;
                        import java.io.UnsupportedEncodingException;
                        import java.net.URLEncoder;
                        
                        @Slf4j(topic = "JWT 검증 인가")
                        @RequiredArgsConstructor
                        public class JwtAuthorizationFilter extends OncePerRequestFilter {
                        
                            private final CookieUtil cookieUtil;
                            private final JwtProvider jwtProvider;
                            private final RefreshTokenRepository refreshTokenRepository;
                        
                            // 배포 상태 여부 체크
                            @Value("${cookie.secure}")
                            private boolean isProductionStatus;
                        
                            // 필터를 무시할 api 또는 websocket
                            private boolean isExcludedUrl(String requestUrl) {
                                // 필터링을 건너뛰는 경로를 명시적으로 정의
                                // accessToken 검증 로직 : access-token은 해당 필터와
                                // 다른 로직이 존재함으로 Controller에서 처리
                                return requestUrl.startsWith("/ws")
                                        || requestUrl.equals("/wss")
                                        || requestUrl.equals("/api/v1/signup")
                                        || requestUrl.equals("/api/v1/login")
                                        || requestUrl.equals("/api/v1/check-available-memberName")
                                        || requestUrl.startsWith("/swagger-ui")
                                        || requestUrl.startsWith("/v3/api-docs")
                                        || requestUrl.startsWith("/api/v1/health-check")
                                        || requestUrl.startsWith("/api/v1/access-token");
                            }
                        
                            @Override
                            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                                log.info("doFilterInternal!!");
                                String requestUrl = request.getRequestURI();
                                log.info("Request URL: " + requestUrl);
                        
                                // 특정 경로는 무시하고 진행
                                if (isExcludedUrl(requestUrl)) {
                                    log.info("Excluded URL, skipping filter: {}", requestUrl);
                                    filterChain.doFilter(request, response);
                                    return;
                                }
                        
                                if(!isProductionStatus){
                                    try {
                                        String accessTokenValue
                                                = cookieUtil.getJwtTokenFromCookie(request, true);
                                        JwtStatus jwtStatus = validateToken(accessTokenValue);
                        
                                        switch (jwtStatus) {
                                            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                            case ACCESS -> successValidatedToken(accessTokenValue);
                                            case EXPIRED -> validateRefreshToken(response, accessTokenValue);
                                        }
                                    } catch (JwtCustomException e) {
                                        log.error("JWT Validation Error: {}", e.getMessage());
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                                        return;
                                    }
                                }else{
                                    try {
                                        String accessTokenValue
                                                = cookieUtil.getJwtTokenFromCookie(request, true);
                                        // String accessTokenValue = jwtProvider.getJwtFromHeader(request);
                                        JwtStatus jwtStatus = validateToken(accessTokenValue);
                        
                                        switch (jwtStatus) {
                                            case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                            case ACCESS -> successValidatedToken(accessTokenValue);
                                            case EXPIRED -> validateRefreshTokenInProduction(request, response);
                                        }
                                    } catch (JwtCustomException e) {
                                        log.error("JWT Validation Error: {}", e.getMessage());
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                                        return;
                                    }
                                }
                        
                                filterChain.doFilter(request, response);
                            }
                        
                            private JwtStatus validateToken(String token){
                                if (StringUtils.hasText(token)) {
                                    return jwtProvider.validateToken(token);
                                }
                                throw new JwtCustomException(GlobalExceptionCode.NOT_EXIST_JWT_STATUS);
                            }
                        
                            private void successValidatedToken(String accessToken) {
                                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                                String authName = authentication.getName();
                                String refreshTokenKey = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authName;
                                // RefreshToken : name
                                if (!refreshTokenRepository.existsByKey(refreshTokenKey)) {
                                    throw new JwtCustomException(GlobalExceptionCode.NOT_FOUND_REFRESH_TOKEN);
                                }
                                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                                securityContext.setAuthentication(authentication);
                                SecurityContextHolder.setContext(securityContext);
                            }
                        
                            private void validateRefreshToken(HttpServletResponse response, String accessTokenValue) throws UnsupportedEncodingException {
                                Authentication authentication
                                        = jwtProvider.getAuthentication(accessTokenValue);
                                String refreshTokenKey
                                        = JwtProvider.REFRESH_TOKEN_HEADER + " : " + authentication.getName();
                                String refreshTokenValue
                                        = refreshTokenRepository.getRefreshTokenValue(refreshTokenKey);
                                JwtStatus jwtStatus = validateToken(refreshTokenValue);
                                switch (jwtStatus) {
                                    case ACCESS, EXPIRED -> makeNewAccessToken(refreshTokenValue, response, jwtStatus);
                                    case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                }
                            }
                        
                            // 액세스 토큰은 이미 만료된 상태
                            private void validateRefreshTokenInProduction(
                                    HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
                                String refreshTokenValue
                                        = cookieUtil.getJwtTokenFromCookie(request, false);
                                JwtStatus jwtStatus = validateToken(refreshTokenValue);
                                switch (jwtStatus) {
                                    case ACCESS -> makeNewAccessToken(refreshTokenValue, response, jwtStatus);
                                    case EXPIRED -> makeNewJwtToken(refreshTokenValue, response, jwtStatus);
                                    case FAIL -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                }
                            }
                        
                            private void makeNewAccessToken(String refreshTokenValue, HttpServletResponse response, JwtStatus jwtStatus) throws UnsupportedEncodingException {
                                // Refresh Token에서 인증 정보 추출
                                Authentication authentication = jwtProvider.getAuthentication(refreshTokenValue);
                                String username = authentication.getName();
                                String redisKey = JwtProvider.REFRESH_TOKEN_HEADER+" : "+username;
                                // Redis에 해당 Refresh Token이 존재하는지 검증
                                if (refreshTokenRepository.existsByKey(redisKey)) {
                                    MemberRole role
                                            = ((MemberDetailsImpl) authentication.getPrincipal()).getMembers().getRole();
                        
                                    // 새 Access Token 생성
                                    String newAccessToken = jwtProvider.createToken(username, role).getAccessToken();
                                    String accessToken
                                            = URLEncoder.encode(newAccessToken, "utf-8")
                                            .replaceAll("\\+", "%20");
                        
                                    ResponseCookie accessCookie
                                            = cookieUtil.createCookie(
                                                    JwtProvider.ACCESS_TOKEN_HEADER, accessToken,
                                            jwtProvider.getAccessTokenExpiration());
                                    response.addHeader("Set-Cookie", accessCookie.toString());
                        
                                    String refreshCookie = refreshTokenRepository.getRefreshTokenValue(redisKey);
                                    response.addHeader("Set-Cookie", refreshCookie);
                        
                                    log.info("accessCookie : "+accessCookie + " / refreshCookie : "+refreshCookie);
                                    // 원래 사용자 데이터도 함께 반환
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    String jsonResponse
                                            = String.format(
                                                    "{\"accessToken\":\"%s\", \"username\":\"%s\"}",
                                            newAccessToken, username);
                                    try{
                                        response.getWriter().write(jsonResponse);
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                }
                            }
                        
                            private void makeNewJwtToken(String refreshTokenValue, HttpServletResponse response, JwtStatus jwtStatus) throws UnsupportedEncodingException {
                                // Refresh Token에서 인증 정보 추출
                                Authentication authentication = jwtProvider.getAuthentication(refreshTokenValue);
                                String username = authentication.getName();
                                String redisKey = JwtProvider.REFRESH_TOKEN_HEADER + " : " + username;
                                // Redis에 해당 Refresh Token이 존재하는지 검증
                                if (refreshTokenRepository.existsByKey(redisKey)) {
                                    MemberRole role
                                            = ((MemberDetailsImpl) authentication.getPrincipal()).getMembers().getRole();
                        
                                    // 새 Access Token 생성
                                    String newAccessToken = jwtProvider.createToken(username, role).getAccessToken();
                                    String accessToken = URLEncoder.encode(newAccessToken, "utf-8").replaceAll("\\+", "%20");
                        
                                    // Access Token, RefreshToken을 ResponseCookie에 설정
                                    ResponseCookie accessCookie
                                            = cookieUtil.createCookie(
                                                    JwtProvider.ACCESS_TOKEN_HEADER, accessToken,
                                            jwtProvider.getAccessTokenExpiration());
                                    response.addHeader("Set-Cookie", accessCookie.toString());
                        
                                    ResponseCookie refreshCookie
                                            = cookieUtil.createCookie(
                                                    JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue,
                                            jwtProvider.getRefreshTokenExpiration());
                                    response.addHeader("Set-Cookie", refreshCookie.toString());
                        
                                    refreshTokenRepository.deleteToken(redisKey);
                                    refreshTokenRepository.saveToken(
                                            redisKey, Math.toIntExact(
                                                    jwtProvider.getRefreshTokenExpiration()),
                                            refreshTokenValue);
                        
                                    log.info("accessCookie : "+accessCookie + " / refreshCookie : "+refreshCookie);
                                    // 원래 사용자 데이터도 함께 반환
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    String jsonResponse
                                            = String.format(
                                            "{\"accessToken\":\"%s\", \"username\":\"%s\"}",
                                            newAccessToken, username);
                                    try {
                                        response.getWriter().write(jsonResponse);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
                                }
                            }
                        }
                        ```
                        
                    - JwtAuthenticationFilter
                        
                        ```java
                        package com.example.backoffice.global.jwt;
                        
                        import com.example.backoffice.domain.member.dto.MembersRequestDto;
                        import com.example.backoffice.domain.member.entity.MemberRole;
                        import com.example.backoffice.global.exception.GlobalExceptionCode;
                        import com.example.backoffice.global.exception.JwtCustomException;
                        import com.example.backoffice.global.jwt.dto.TokenDto;
                        import com.example.backoffice.global.redis.RefreshTokenRepository;
                        import com.example.backoffice.global.security.MemberDetailsImpl;
                        import com.fasterxml.jackson.databind.ObjectMapper;
                        import jakarta.servlet.FilterChain;
                        import jakarta.servlet.http.Cookie;
                        import jakarta.servlet.http.HttpServletRequest;
                        import jakarta.servlet.http.HttpServletResponse;
                        import lombok.extern.slf4j.Slf4j;
                        import org.springframework.http.ResponseCookie;
                        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
                        import org.springframework.security.core.Authentication;
                        import org.springframework.security.core.AuthenticationException;
                        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
                        
                        import java.io.IOException;
                        import java.util.Collection;
                        
                        @Slf4j(topic = "로그인 및 JWT 생성")
                        public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
                            private final JwtProvider jwtProvider;
                            private final RefreshTokenRepository tokenRedisProvider;
                            private final CookieUtil cookieUtil;
                            public JwtAuthenticationFilter(
                                    JwtProvider jwtProvider, RefreshTokenRepository tokenRedisProvider,
                                    CookieUtil cookieUtil) {
                                this.jwtProvider = jwtProvider;
                                this.tokenRedisProvider = tokenRedisProvider;
                                this.cookieUtil = cookieUtil;
                                setFilterProcessesUrl("/api/v1/login");
                            }
                        
                            @Override
                            public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
                                // 로그아웃 로직일때 인증절차를 밟지 않고 LogoutFilter에서 끝냄
                                log.info("attemptAuthentication()");
                                try {
                                    MembersRequestDto.LoginDto requestDto
                                            = new ObjectMapper().readValue(request.getInputStream(),
                                            MembersRequestDto.LoginDto.class);
                        
                                    return getAuthenticationManager().authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                    requestDto.getMemberName(),
                                                    requestDto.getPassword(),
                                                    null
                                            )
                                    );
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                    throw new RuntimeException(e.getMessage());
                                }
                            }
                        
                            @Override
                            protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
                                log.info("successfulAuthentication()");
                        
                                String origin = request.getHeader("Origin"); // 요청의 출처
                                String referer = request.getHeader("Referer"); // 요청 발생 경로
                        
                                log.info("Request Origin: " + origin);
                                log.info("Request Referer: " + referer);
                        
                                // 서버에서 수신한 요청 URL
                                String serverRequestURL = request.getRequestURL().toString(); // 전체 URL
                                String serverRequestURI = request.getRequestURI(); // URI 부분
                                log.info("Server received request URL: " + serverRequestURL);
                                log.info("Server received request URI: " + serverRequestURI);
                        
                                String username = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();
                                MemberRole role = ((MemberDetailsImpl) authResult.getPrincipal()).getMembers().getRole();
                        
                                TokenDto tokenDto = jwtProvider.createToken(username, role);
                        
                                // Access Token Cookie settings
                                ResponseCookie accessCookie = cookieUtil.createCookie(
                                        JwtProvider.ACCESS_TOKEN_HEADER, tokenDto.getAccessToken(),
                                        jwtProvider.getAccessTokenExpiration());
                        
                                log.info("Created Access Token Cookie: Name = {}, Value = {}, Max-Age = {}, SameSite = {}",
                                        accessCookie.getName(), accessCookie.getValue(), accessCookie.getMaxAge(), accessCookie.getSameSite());
                                ResponseCookie refreshCookie = null;
                                // Refresh Token Cookie settings
                                String redisKey = JwtProvider.REFRESH_TOKEN_HEADER+" : "+username;
                        
                                boolean existRefreshToken
                                        = tokenRedisProvider.existsByKey(redisKey);
                                if(!existRefreshToken){
                                    refreshCookie
                                            = cookieUtil.createCookie(
                                                    JwtProvider.REFRESH_TOKEN_HEADER, tokenDto.getRefreshToken(),
                                            jwtProvider.getRefreshTokenExpiration());
                        
                                    log.info("Created Refresh Token Cookie: Name = {}, Value = {}, Max-Age = {}, SameSite = {}",
                                            refreshCookie.getName(), refreshCookie.getValue(), refreshCookie.getMaxAge(), refreshCookie.getSameSite());
                        
                                    tokenRedisProvider.saveToken(
                                            refreshCookie.getName()+ " : " + username,
                                            Math.toIntExact(
                                                    jwtProvider.getRefreshTokenExpiration()),
                                            tokenDto.getRefreshToken());
                                }else {
                                    String redisValue
                                            = tokenRedisProvider.getRefreshTokenValue(redisKey);
                                    if (redisValue != null) {  // 가져온 값이 null이 아닐 때만 쿠키 생성
                                        refreshCookie = cookieUtil.createCookie(
                                                JwtProvider.REFRESH_TOKEN_HEADER, redisValue,
                                                jwtProvider.getRefreshTokenExpiration());
                                        log.info("redisValue is not null!, therefore... Created Refresh Token Cookie: Name = {}, Value = {}, Max-Age = {}",
                                                refreshCookie.getName(), refreshCookie.getValue(), refreshCookie.getMaxAge());
                                    }else{
                                        throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
                                    }
                                }
                        
                                // JSON 응답 보내기
                                response.addHeader("Set-Cookie", accessCookie.toString());
                                response.addHeader("Set-Cookie", refreshCookie.toString());
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                        
                                try {
                                    response.getWriter().write("{\"status\":\"success\"}");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        
                            @Override
                            protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
                                response.setStatus(401);
                            }
                        }
                        
                        ```
                        
                    - WebSecurityConfig
                        
                        ```java
                        package com.example.backoffice.global.config;
                        
                        import com.example.backoffice.global.jwt.CookieUtil;
                        import com.example.backoffice.global.jwt.JwtAuthenticationFilter;
                        import com.example.backoffice.global.jwt.JwtAuthorizationFilter;
                        import com.example.backoffice.global.jwt.JwtProvider;
                        import com.example.backoffice.global.redis.RefreshTokenRepository;
                        import com.example.backoffice.global.security.CustomLogoutHandler;
                        import jakarta.servlet.http.HttpServletResponse;
                        import lombok.RequiredArgsConstructor;
                        import org.springframework.beans.factory.annotation.Value;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.security.authentication.AuthenticationManager;
                        import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
                        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
                        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
                        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
                        import org.springframework.security.config.http.SessionCreationPolicy;
                        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
                        import org.springframework.security.crypto.password.PasswordEncoder;
                        import org.springframework.security.web.SecurityFilterChain;
                        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
                        import org.springframework.web.cors.CorsConfiguration;
                        
                        import java.util.Arrays;
                        
                        @Configuration
                        @EnableWebSecurity
                        @RequiredArgsConstructor
                        public class WebSecurityConfig {
                        
                            @Value("${server.port}")
                            private String deploymentPort;
                        
                            private final JwtProvider jwtProvider;
                            private final RefreshTokenRepository tokenRedisProvider;
                            private final AuthenticationConfiguration authenticationConfiguration;
                            private final CustomLogoutHandler customLogoutHandler;
                            private final CookieUtil cookieUtil;
                        
                            @Bean
                            public PasswordEncoder passwordEncoder() {
                                return new BCryptPasswordEncoder();
                            }
                        
                            @Bean
                            public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                                return configuration.getAuthenticationManager();
                            }
                        
                            @Bean
                            public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
                                JwtAuthenticationFilter filter
                                        = new JwtAuthenticationFilter(
                                                jwtProvider, tokenRedisProvider, cookieUtil);
                                filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
                                return filter;
                            }
                        
                            @Bean
                            public JwtAuthorizationFilter jwtAuthorizationFilter() {
                                return new JwtAuthorizationFilter(cookieUtil, jwtProvider, tokenRedisProvider);
                            }
                        
                            @Bean
                            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                                // csrf(front) token 비활성화, 폼 로그인 비활성화,
                                // http에서 제공하는 Authorization : base64 암호화 방식 변경 -> JWT 토큰 인증 방식으로 변경
                                // spring boot에서 제공하는 session 방식 비활성화 -> JWT 토큰 인증 방식으로 변경
                                http.csrf(AbstractHttpConfigurer::disable)
                                        .formLogin(AbstractHttpConfigurer::disable)
                                        .httpBasic(AbstractHttpConfigurer::disable)
                                        .cors(cors -> cors
                                                .configurationSource(request -> {
                                                    CorsConfiguration configuration = new CorsConfiguration();
                        
                                                    // 테스트를 위한 임시로 접근 url을 모두 허용
                                                    configuration.setAllowedOriginPatterns(
                                                            Arrays.asList(
                                                                    "http://localhost:3000", "http://localhost:8080",
                                                                    "http://api.baegobiseu.com",
                                                                    "https://api.baegobiseu.com", "https://baegobiseu.com"));
                                                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
                                                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "refreshToken", "Cache-Control", "Content-Type"));
                                                    configuration.setAllowCredentials(true);
                                                    configuration.setExposedHeaders(Arrays.asList("Authorization","Set-Cookie"));
                        
                                                    return configuration;
                                                })
                                        )
                                        .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers(
                                                        "/websocket", "/ws/**", "/wss/**",
                                                        "/api/v1/login","/api/v1/signup",
                                                        "/swagger-ui/**", "/v3/api-docs/**",
                                                        "/api/v1/check-available-memberName",
                                                        "/api/v1/health-check",
                                                        "/api/v1/access-token",
                                                        "https://baegobiseu.com/auth/login",
                                                        "https://baegobiseu.com/auth/signup").permitAll()
                                                .anyRequest().authenticated()
                                        )
                                        .logout((logout) -> logout
                                                .logoutUrl("/api/v1/logout")
                                                .addLogoutHandler(customLogoutHandler)
                                                .deleteCookies("remember-me")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                    response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                        )
                                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                ;
                                // 필터 순서 조정
                                http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
                                http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
                                return http.build();
                                /*http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
                                return http.build();*/
                            }
                        }
                        
                        ```
                        
                    - WebSocketConfig
                        
                        ```java
                        package com.example.backoffice.global.config;
                        
                        import com.example.backoffice.global.jwt.interceptor.JwtChannelInterceptor;
                        import lombok.RequiredArgsConstructor;
                        import org.springframework.beans.factory.annotation.Value;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.messaging.simp.config.ChannelRegistration;
                        import org.springframework.messaging.simp.config.MessageBrokerRegistry;
                        import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
                        import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
                        import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
                        
                        @Configuration
                        @RequiredArgsConstructor
                        @EnableWebSocketMessageBroker
                        public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
                        
                            @Value("${cookie.secure}")
                            private Boolean isSecure;
                        
                            private final JwtChannelInterceptor jwtChannelInterceptor;
                        
                            @Override
                            public void configureMessageBroker(MessageBrokerRegistry config) {
                                // topic : 전체 알림, queue : 개인 알림
                                config.enableSimpleBroker("/topic", "/queue");
                                config.setApplicationDestinationPrefixes("/app");
                                config.setUserDestinationPrefix("/user");
                            }
                        
                            @Override
                            public void registerStompEndpoints(StompEndpointRegistry registry) {
                                // 브라우저 CROS 이슈
                                // 로컬
                                if(!isSecure){
                                    registry.addEndpoint("/ws")
                                            .setAllowedOrigins(
                                                    "http://localhost:3000", "http://localhost:8080")
                                            // ec2 서버도 추가해야함
                                            .withSockJS();
                                }else{
                                    // 배포
                                    registry.addEndpoint("/wss")
                                            .setAllowedOrigins(
                                                    "https://baegobiseu.com", "https://api.baegobiseu.com")
                                            // ec2 서버도 추가해야함
                                            .withSockJS();
                                }
                            }
                        
                            @Override
                            public void configureClientInboundChannel(ChannelRegistration registration) {
                                registration.interceptors(jwtChannelInterceptor); // JwtChannelInterceptor 등록
                            }
                        }
                        
                        ```
                        
                    - JwtChannelInterceptor
                        
                        ```java
                        package com.example.backoffice.global.jwt.interceptor;
                        
                        import com.example.backoffice.global.exception.GlobalExceptionCode;
                        import com.example.backoffice.global.exception.JwtCustomException;
                        import com.example.backoffice.global.jwt.JwtProvider;
                        import com.example.backoffice.global.jwt.JwtStatus;
                        import lombok.extern.slf4j.Slf4j;
                        import org.springframework.messaging.Message;
                        import org.springframework.messaging.MessageChannel;
                        import org.springframework.messaging.simp.stomp.StompCommand;
                        import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
                        import org.springframework.messaging.support.ChannelInterceptor;
                        import org.springframework.messaging.support.MessageHeaderAccessor;
                        import org.springframework.security.core.Authentication;
                        import org.springframework.security.core.context.SecurityContextHolder;
                        import org.springframework.stereotype.Component;
                        
                        import java.util.Map;
                        import java.util.concurrent.ConcurrentHashMap;
                        
                        @Slf4j(topic = "JwtChannelInterceptor logs")
                        @Component
                        public class JwtChannelInterceptor implements ChannelInterceptor {
                        
                            private final JwtProvider jwtProvider;
                        
                            public JwtChannelInterceptor(JwtProvider jwtProvider) {
                                this.jwtProvider = jwtProvider;
                            }
                        
                            // 세션 유지 맵
                            private static final Map<String, Authentication> sessionAuthenticationMap = new ConcurrentHashMap<>();
                        
                            @Override
                            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                                log.info("JwtChannelInterceptor preSend invoked");
                                StompHeaderAccessor accessor = MessageHeaderAccessor
                                        .getAccessor(message, StompHeaderAccessor.class);
                                String sessionId = accessor.getSessionId();  // 현재 세션 ID 가져오기
                                String destination = accessor.getDestination(); // destination 값 가져오기
                        
                                // destination 검증 로그 추가
                                if (destination != null) {
                                    log.info("Destination: {}", destination);
                                } else {
                                    log.info("No destination found for command: {}", accessor.getCommand());
                                }
                        
                                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                                    // CONNECT 요청 시 JWT 인증 처리
                                    String bearerToken = accessor.getFirstNativeHeader(JwtProvider.AUTHORIZATION_HEADER);
                                    String tokenValue = jwtProvider.removeBearerPrefix(bearerToken);
                        
                                    if (tokenValue != null) {
                                        JwtStatus status = jwtProvider.validateToken(tokenValue);
                                        if (status == JwtStatus.ACCESS) {
                                            Authentication auth = jwtProvider.getAuthentication(tokenValue);
                                            if (auth != null) {
                                                // 인증된 사용자 정보를 StompHeaderAccessor와 SecurityContextHolder에 저장
                                                accessor.setUser(auth);
                                                SecurityContextHolder.getContext().setAuthentication(auth);
                                                log.info("Authenticated user set in SecurityContextHolder and accessor: " + auth.getName());
                        
                                                // 세션 ID와 인증 객체를 맵에 저장해 둠
                                                sessionAuthenticationMap.put(sessionId, auth);
                                            }
                                        }
                                    }
                                    // 세션 맵을 활용해서 해당 인증 객체가 유실되는 거 수정
                                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
                                    // SUBSCRIBE 또는 SEND 요청 시 인증 객체 확인
                                    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
                                    if (existingAuth == null) {
                                        // 세션 ID로 인증 객체를 가져옴
                                        existingAuth = sessionAuthenticationMap.get(sessionId);
                                        if (existingAuth != null) {
                                            SecurityContextHolder.getContext().setAuthentication(existingAuth);
                                            log.info("Restored authentication for session: " + sessionId + ", user: " + existingAuth.getName());
                                        } else {
                                            log.error("No authenticated user found for SUBSCRIBE or SEND command.");
                                            throw new JwtCustomException(GlobalExceptionCode.UNAUTHORIZED);
                                        }
                                    }
                                    log.info("existAuth : {}", existingAuth);
                                    log.info("securityContextHolder.getContext().getAuthentication : {}", SecurityContextHolder.getContext().getAuthentication());
                        
                                    // 추가: destination 검증 로그
                                    if (destination != null) {
                                        log.info("Validating destination for {} command: {}", accessor.getCommand(), destination);
                                    }
                                }
                        
                                log.info("JWT CHANNEL INTERCEPTOR {} MESSAGE : {}", accessor.getCommand(), message);
                                return message;
                            }
                        }
                        
                        ```
                        
                    - RefreshTokenRepository
                        
                        ```java
                        package com.example.backoffice.global.redis;
                        
                        import com.example.backoffice.global.exception.GlobalExceptionCode;
                        import com.example.backoffice.global.exception.JwtCustomException;
                        import com.fasterxml.jackson.core.JsonProcessingException;
                        import com.fasterxml.jackson.databind.ObjectMapper;
                        import org.springframework.beans.factory.annotation.Qualifier;
                        import org.springframework.data.redis.core.RedisTemplate;
                        import org.springframework.stereotype.Component;
                        
                        import java.util.Objects;
                        import java.util.concurrent.TimeUnit;
                        
                        @Component
                        public class RefreshTokenRepository {
                            private final ObjectMapper objectMapper;
                        
                            // database 0 : jwt token -> refreshToken
                            @Qualifier("redisTemplateForToken")
                            private final RedisTemplate<String, Object> redisTemplateForToken;
                        
                            public RefreshTokenRepository(
                                    ObjectMapper objectMapper,
                                    @Qualifier("redisTemplateForToken") RedisTemplate<String, Object> redisTemplateForToken){
                                this.objectMapper = objectMapper;
                                this.redisTemplateForToken = redisTemplateForToken;
                            }
                        
                            public <T> void saveToken(String key, Integer minutes, T value) {
                                String valueString = null;
                                try {
                                    valueString = !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException();
                                }
                        
                                redisTemplateForToken.opsForValue().set(key, valueString);
                                redisTemplateForToken.expire(key, minutes, TimeUnit.MINUTES);
                            }
                        
                            // refreshToken 조회
                            public <T> T getToken(String key, Class<T> valueType) {
                        
                                String value = (String) redisTemplateForToken.opsForValue().get(key);
                                if (Objects.isNull(value)) {
                                    return null;
                                }
                                try {
                                    return objectMapper.readValue(value, valueType);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        
                            public String getRefreshTokenValue(String key) {
                                // Long isExpiredRefreshToken = redisTemplateForToken.getExpire(key);
                                if(redisTemplateForToken.opsForValue().get(key) == null){
                                    throw new JwtCustomException(GlobalExceptionCode.TOKEN_VALUE_IS_NULL);
                                }
                                return redisTemplateForToken.opsForValue().get(key).toString();
                            }
                        
                            // 토큰 삭제
                            public void deleteToken(String key) {
                                redisTemplateForToken.delete(key);
                            }
                        
                            public boolean existsByKey(String key) {
                                return redisTemplateForToken.opsForValue().get(key) != null;
                            }
                        }
                        
                        ```
                        
            4. 기대 효과
                1. 기존의 세션 방식에서 취약한 XSS, CSRF 공격 방어를 할 수 있습니다.
                2. Websocket 환경 사용에 있어서 Jwt Token 인증 방식을 재사용하여 인증을 할 수 있습니다.
                
        2. HTTPS 적용 및 ResponseCookie 활용
            1. 문제 상황
                
                브라우저에서 http 프로토콜로 서버의 접근을 허용하게 되면 네트워크에서 비밀번호, 토큰을 가로채는 중간자 공격에 취약하다는 문제점이 발생합니다.
                
            2. 도입 이유
                1. HTTPS를 도입하여 클라이언트-서버간의 데이터를 암호화하여 보안을 강화하고자 합니다.
                2. ALB를 활용하여, EC2 내부는 HTTP 프로토콜로 통신하여 접근하게 하여, 보안의 안정성을 증대 시키고자 합니다.
                3. Jwt Token을 HttpOnly, Secure = true, SameSite = “Strict” 환경에만 작동하게 하여, 특정 공격의 취약한 부분을 제거하고자 합니다.
            3. 구현 방식
                1. AWS ACM을 활용한 TLS 버젼 1.2 인증서를 API 서버와 웹 사이트에 발급하여 데이터를 암호화할 수 있게 합니다.
                2. CloudFront와 ALB를 통한 원본 서버를 연결하여 성능을 최적화할 수 있게 합니다.
                3. Spring boot에서 ResponseCookie를 활용해서 특정 설정을 통하여 HTTPS 프로토콜 환경에서만 해당 쿠키를 전달 가능하게 변경합니다.
                4. 사진 첨부
                    - 정적 웹사이트, API 서버 TLS 발급
                        
                        ![image.png](image.png)
                        
                    - CloudFront ALB 연결
                        
                        ![image.png](image%201.png)
                        
                        ![image.png](image%202.png)
                        
                    - CookieUtil
                        
                        ```java
                        package com.example.backoffice.global.jwt;
                        
                        import com.example.backoffice.global.exception.GlobalExceptionCode;
                        import com.example.backoffice.global.exception.JwtCustomException;
                        import jakarta.servlet.http.Cookie;
                        import jakarta.servlet.http.HttpServletRequest;
                        import jakarta.servlet.http.HttpServletResponse;
                        import org.springframework.beans.factory.annotation.Value;
                        import org.springframework.http.ResponseCookie;
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        public class CookieUtil {
                        
                            @Value("${cookie.secure}")
                            private boolean isSecure;
                            public static final String SET_COOKIE = "Set-Cookie";
                            public static final String ACCESS_TOKEN_KEY = "accessToken";
                            public static final String REFRESH_TOKEN_KEY = "refreshToken";
                        
                            public ResponseCookie createCookie(
                                    String name, String value, long maxAgeSeconds){
                                if(!isSecure){
                                    // local test success
                                    return ResponseCookie.from(name, value)
                                            .httpOnly(false)        // 로컬 환경에서 httpOnly 또한 false로 변경
                                            .secure(this.isSecure) // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
                                            .path("/") // 쿠키가 적용될 경로
                                            .maxAge(maxAgeSeconds) // 쿠키의 유효 기간 설정 (초 단위)
                                            .sameSite("Lax") // CSRF 보호를 위한 SameSite 설정
                                            .build();
                                }else {
                                    // production
                                    return ResponseCookie.from(name, value)
                                            .httpOnly(true)
                                            .secure(this.isSecure) // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
                                            .path("/") // 쿠키가 적용될 경로
                                            .maxAge(maxAgeSeconds) // 쿠키의 유효 기간 설정 (초 단위)
                                            .domain(".baegobiseu.com")
                                            .sameSite("Strict")// CSRF 보호를 위한 SameSite 설정
                                            .build();
                                }
                            }
                        
                            public void deleteCookie(HttpServletResponse response, String cookieName) {
                                if(!isSecure){
                                    // 로컬
                                    Cookie cookie = new Cookie(cookieName, null);
                                    cookie.setHttpOnly(false);
                                    cookie.setSecure(this.isSecure); // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
                                    cookie.setPath("/");
                                    cookie.setMaxAge(0); // 쿠키 삭제
                                    response.addCookie(cookie);
                                }else{
                                    // 배포
                                    Cookie cookie = new Cookie(cookieName, null);
                                    cookie.setHttpOnly(true);
                                    cookie.setSecure(this.isSecure); // 로컬 환경에서는 false, 프로덕션에서는 true로 설정
                                    cookie.setPath("/");
                                    cookie.setMaxAge(0); // 쿠키 삭제
                                    response.addCookie(cookie);
                                }
                            }
                        
                            public String getCookieValue(HttpServletRequest request, String cookieName){
                                Cookie[] cookieList = request.getCookies();
                                if(cookieList != null){
                                    for (Cookie cookie : cookieList){
                                        if (cookieName.equals(cookie.getName())) {
                                            return cookie.getValue();
                                        }
                                    }
                                }
                                return null;
                            }
                        
                            public String getJwtTokenFromCookie(HttpServletRequest request, boolean isAccessToken){
                                Cookie[] cookies = request.getCookies();
                                if (cookies != null) {
                                    for (Cookie cookie : cookies) {
                                        if(isAccessToken){
                                            if (ACCESS_TOKEN_KEY.equals(cookie.getName())) {
                                                return cookie.getValue();
                                            }
                                        }else{
                                            if (REFRESH_TOKEN_KEY.equals(cookie.getName())) {
                                                return cookie.getValue();
                                            }
                                        }
                                    }
                                }
                                throw new JwtCustomException(GlobalExceptionCode.MISSING_TOKEN);
                            }
                        }
                        
                        ```
                        
            4. 기대 효과
                1. MITM(Man-in-the-Middle) 공격 방지 및 데이터 보안이 강화됩니다.
                2. 클라이언트에서 JWT 토큰 접근 차단 (XSS, CSRF 공격 방어 가능)할 수 있습니다.
            - 해당 문제 상황 추가 정리
                
                [Https 설정](https://www.notion.so/Https-17c793fd0cc8801dbb3bfd910404eba9?pvs=21)
                
                [Cookie](https://www.notion.so/Cookie-195793fd0cc880b8b145f18c3aa784e5?pvs=21)
                
            
        3. 인프라 보안
            1. 문제 상황
                
                비정상적인 API 접근, 대용량 파일 업로드를 하는 경우에 그대로 application에 전달 되는 문제점이 발생합니다.
                
            2. 도입 이유
                1. 불필요한 트래픽이 발생하여, 비용이 추가적으로 발생하게 됩니다.
                2. 대용량 파일 업로드를 하게 되면 S3 스토리지에 대한 과한 비용이 발생 할 수 있음으로, 대용량 파일 업로드를 방지하고자 합니다.
                3. WAF을 활용하여, 추가적인 보안을 확장할 수 있습니다. 
            3. 구현 방식
                1. AWS WAF의 기존의 제한 방식을 추가로 사용 또는 자기 자신이 만든 제한을 추가하여 EC2 원본 서버의 접근을 앞단에서 막게 합니다.
                    - 10MB 이상의 파일 업로드 차단
                    - 특정 국가/지역의 접근을 제한
                    - SQL Injection 및 XSS 공격 탐지 및 차단
                2. CloudFront와 S3의 보안 설정 강화
                    - CloudFront에서만 S3 원본 접근 허용 (Origin Access Control)
                    - 파일 업로드 시, 특정 출처(Origin)만 허용
                    - CloudFront의 캐싱 무효화 정책 활성화
                3. 사진 첨부
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
                        
                        ![image.png](image%201.png)
                        
                    
            4. 기대 효과
                1. 서버 부하 방지 및 악성 트래픽 차단
                2. 대용량 요청에 대하여 트래픽 제한으로 인프라 비용 절감
            - 해당 문제 상황 추가 정리
                
                [AWS WAF](https://www.notion.so/AWS-WAF-1a1793fd0cc880eb873bf8f21525eb19?pvs=21)
                
            
        4. 확장 가능성
            1. 문제 상황
                
                application 레벨이 아닌 앞단 레벨의 문제점을 전부 로깅으로 기록할 수 없다는 문제점이 발생합니다.
                
            2. 도입 이유
                1. 로깅 검사를 application에서 로깅을 저장하는 것과 WAF에서도 로깅을 기록하여, 장애 감지 및 분석을 추가적으로 하여, 에러 방지에 힘쓰고자 합니다.
                2. AWS 인프라를 사용하고 있음으로 WAF뿐만 아니라, CloudWatch의 추가 확장을 고려할 수 있습니다.
            3. 차후 구현 방식
                1. AWS CloudWatch Logs 및 Metrics 적용을 통한 CloudFront, EC2, ALB 상태 모니터링을 효율적으로 할 수 있습니다.
                2. ELK Stack을 통한 API 요청 로그 시각화 및 분석을 할 수 있습니다.
            4. 기대 효과
                1. 실시간 장애 감지 및 빠른 대응 가능
                2. 트래픽 분석을 통한 최적화 가능
            
            | 카테고리 | 문제 상황 | 도입 이유 | 구현 방식 | 기대 효과 |
            | --- | --- | --- | --- | --- |
            | **JWT 인증** | 세션 기반 인증은 서버 부담 증가 | 유지보수 비용 절감, 확장성 고려 | AccessToken + RefreshToken 구조, WebSocket에서도 사용 | XSS, CSRF 방어 및 인증 통합 |
            | **HTTPS + Secure Cookie** | HTTP 프로토콜 사용 시 보안 취약 | MITM 공격 방어, JWT 토큰 보호 | AWS ACM TLS 적용, HttpOnly Secure Cookie 설정 | 데이터 암호화 및 클라이언트 접근 차단 |
            | **인프라 보안 (WAF + CloudFront + S3)** | 비정상적인 API 접근 및 대용량 파일 업로드 문제 | 불필요한 트래픽 차단 및 비용 절감 | WAF 차단 규칙 적용, CloudFront + S3 OAC 활용 | 서버 부하 방지, 악성 트래픽 차단 |
            | **인프라 기반 확장 가능성 (CloudWatch + ELK)** | 로깅 부족으로 문제 분석 어려움 | 장애 감지 및 분석 강화 | AWS CloudWatch, WAF 로그 분석, ELK Stack 연계 | 실시간 모니터링 및 최적화 가능 |
    - 반복되는 작업 공통화
        
        스케줄러
        
        1. 문제 상황
            
            현재 인사 담당자가  매일 출근할 때마다 개별적으로 근태 기록을 생성하고, 휴가가 끝난 멤버의 상태를 직접 변경해야 하는 번거로움이 있음. 또한, 연말이 되면 수작업으로 연간 데이터를 정리해야 하는 문제가 발생. 이를 자동화하여 인사 담당자의 업무 부담을 줄이고, 실수 없이 일관된 처리를 수행할 방법을 모색
            
        2. 도입 이유 
            1. 관리자의 업무를 자동화하여, 일의 부담감을 덜고자 함. 
            2. 모든 직원은 실수로 발생할 수 있는 데이터를 일관된 업무 처리로 오류를 최소화함.
            3. 신규 멤버는 일관된 프로세스를 통해 적응 속도를 향상시키고 조직 전체의 운영 효율성을 높일 수 있음.
        3. 구현 방식
            - DailyScheduler, MonthlyScheduler, YearlyScheduler를 나누어 실행하게 함.
            1. DailyScheduler : 당일에 처리해야 할 업무를 자동화
            2. MonthlyScheduler: 달 단위에 처리해야 할 업무를 자동화
            3. YearlyScheduler : 연 단위에 처리해야 할 업무를 자동화
            4. 코드 첨부
                - Scheduler
                    
                    ```java
                    package com.example.backoffice.global.scheduler;
                    
                    import lombok.RequiredArgsConstructor;
                    import org.springframework.scheduling.annotation.EnableScheduling;
                    import org.springframework.scheduling.annotation.Scheduled;
                    import org.springframework.stereotype.Component;
                    
                    @Component
                    @EnableScheduling
                    @RequiredArgsConstructor
                    public class Scheduler {
                    
                        private final DailyScheduler dailyScheduler;
                        private final MonthlyScheduler monthlyScheduler;
                        private final YearlyScheduler yearlyScheduler;
                    
                        @Scheduled(cron = "0 0 0 * * *")
                        public void executeDailyTask() {
                            dailyScheduler.execute();
                        }
                    
                        // 매월 1일 00:00 실행
                        @Scheduled(cron = "0 0 0 1 * *")
                        public void executeMonthlyTask() {
                            monthlyScheduler.execute();
                        }
                    
                        // 매년 1월 1일 00:00 실행
                        @Scheduled(cron = "0 0 0 1 1 *")
                        public void executeYearlyTask() {
                            yearlyScheduler.execute();
                        }
                    }
                    ```
                    
                - DailyScheduler
                    
                    ```java
                    package com.example.backoffice.global.scheduler;
                    
                    import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
                    import com.example.backoffice.domain.evaluation.entity.Evaluations;
                    import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
                    import com.example.backoffice.domain.member.entity.Members;
                    import com.example.backoffice.domain.member.service.MembersServiceV1;
                    import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
                    import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
                    import com.example.backoffice.domain.notification.converter.NotificationsConverter;
                    import com.example.backoffice.domain.notification.entity.NotificationType;
                    import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
                    import com.example.backoffice.domain.vacation.entity.Vacations;
                    import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
                    import com.example.backoffice.global.date.DateTimeUtils;
                    import com.example.backoffice.global.redis.service.RedisBackupService;
                    import lombok.RequiredArgsConstructor;
                    import org.springframework.stereotype.Component;
                    
                    import java.time.LocalDate;
                    import java.util.List;
                    
                    @Component
                    @RequiredArgsConstructor
                    public class DailyScheduler implements SchedulerTask{
                    
                        private final MembersServiceV1 membersService;
                        private final EvaluationsServiceV1 evaluationsService;
                        private final MembersEvaluationsServiceV1 membersEvaluationsService;
                        private final NotificationsServiceV1 notificationsService;
                        private final VacationsServiceV1 vacationsService;
                        private final AttendancesServiceV1 attendancesService;
                        private final RedisBackupService redisBackupService;
                    
                        @Override
                        public void execute() {
                            refreshCachedDateData();
                            sendNotificationForUnCompletedEvaluationMember();
                            updateMemberOnVacation();
                            createAttendances();
                            updateYesterdayAttendanceStatus();
                            sendNotificationForUpcomingAttendance();
                            scheduleRedisBackup();
                        }
                    
                        private void refreshCachedDateData(){
                            DateTimeUtils.refreshCached();
                        }
                    
                        private void sendNotificationForUnCompletedEvaluationMember(){
                            List<Evaluations> evaluationList
                                    = evaluationsService.findAllByEndDatePlusSevenDays(LocalDate.now().plusDays(7));
                            if(!evaluationList.isEmpty()){
                                List<Members> unCompletedEvaluationMemberList
                                        = membersEvaluationsService.findAllByIsCompleted(false).stream()
                                        .map(MembersEvaluations::getMember).toList();
                                for(Members unCompletedEvaluationMember : unCompletedEvaluationMemberList){
                                    String message = "설문 조사 마감 7일 전입니다. 신속히 마무리 해주시길 바랍니다.";
                                    notificationsService.generateEntityAndSendMessage(
                                            NotificationsConverter.toNotificationData(
                                                    unCompletedEvaluationMember, membersService.findHRManager(), message),
                                            NotificationType.EVALUATION
                                    );
                                }
                            }
                        }
                    
                        private void updateMemberOnVacation() {
                            // 휴가가 끝난 멤버들의 상태를 false로 설정
                            List<Vacations> endedVacationList
                                    = vacationsService.findAllBetweenYesterday(
                                            DateTimeUtils.getToday().minusSeconds(1));
                            for (Vacations vacation : endedVacationList) {
                                membersService.updateOneForOnVacationFalse(vacation.getOnVacationMember().getId());
                            }
                    
                            // 휴가가 시작된 멤버들의 상태를 true로 설정
                            List<Vacations> startedVacationList
                                    = vacationsService.findAllByStartDate(DateTimeUtils.getToday());
                            for (Vacations vacation : startedVacationList) {
                                membersService.updateOneForOnVacationTrue(vacation.getOnVacationMember().getId());
                            }
                        }
                    
                        // 여기가 에러가 나는듯
                        private void createAttendances(){
                            // 평일
                            if(DateTimeUtils.isWeekday()){
                                attendancesService.create(true);
                            }else{
                                attendancesService.create(false);
                            }
                        }
                    
                        private void updateYesterdayAttendanceStatus(){
                            attendancesService.updateYesterdayAttendanceList();
                        }
                    
                        private void sendNotificationForUpcomingAttendance(){
                            attendancesService.sendNotificationForUpcomingAttendance();
                        }
                    
                        public void scheduleRedisBackup() {
                            redisBackupService.backupAllRedisDataToS3();
                        }
                    }
                    ```
                    
                - MonthlyScheduler
                    
                    ```java
                    package com.example.backoffice.global.scheduler;
                    
                    import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
                    import com.example.backoffice.domain.member.service.MembersServiceV1;
                    import com.example.backoffice.domain.vacation.entity.VacationPeriodProvider;
                    import com.example.backoffice.global.date.DateTimeUtils;
                    import com.example.backoffice.global.exception.GlobalExceptionCode;
                    import com.example.backoffice.global.exception.JsonCustomException;
                    import com.example.backoffice.global.redis.UpdateVacationPeriodRepository;
                    import com.fasterxml.jackson.core.JsonProcessingException;
                    import lombok.RequiredArgsConstructor;
                    import lombok.extern.slf4j.Slf4j;
                    import org.springframework.stereotype.Component;
                    
                    import java.time.LocalDateTime;
                    
                    @Component
                    @RequiredArgsConstructor
                    @Slf4j(topic = "monthlyScheduler")
                    public class MonthlyScheduler implements SchedulerTask{
                    
                        private final MembersServiceV1 membersService;
                        private final AttendancesServiceV1 attendancesService;
                        private final VacationPeriodProvider vacationPeriodProvider;
                        private final UpdateVacationPeriodRepository vacationPeriodRepository;
                    
                        @Override
                        public void execute(){
                            updateRemainingVacationDays();
                            configureVacationRequestPeriod();
                            deleteBeforeTwoYearAttendanceList();
                        }
                    
                        private void updateRemainingVacationDays() {
                            membersService.updateOneForRemainingVacationDays(
                                    ScheduledEventType.MONTHLY_UPDATE);
                        }
                    
                        private void configureVacationRequestPeriod() {
                            // 현재 날짜 기준으로 해당 달의 첫 번째 날을 가져옴
                            LocalDateTime firstDayOfMonth = DateTimeUtils.getFirstDayOfMonth();
                            LocalDateTime firstMonday
                                    = DateTimeUtils.getFirstMonday(firstDayOfMonth);
                    
                            Long currentYear = (long) DateTimeUtils.getToday().getYear();
                            Long currentMonth = (long) DateTimeUtils.getToday().getMonthValue();
                    
                            // 두 번째 주의 월요일을 찾음
                            LocalDateTime secondMondayOfMonth
                                    = DateTimeUtils.findSecondMondayOfMonth(firstMonday);
                    
                            LocalDateTime thirdFridayofMonth = secondMondayOfMonth.plusDays(11); // 월요일 + 4일 -> 금요일
                    
                            LocalDateTime endOfDay
                                    = DateTimeUtils.getEndDayOfMonth(currentYear, currentMonth);
                            Long ttlMinutes
                                    = DateTimeUtils.calculateMinutesFromTodayToEndDate(endOfDay);
                            // 휴가 신청 기간을 VacationPeriod에 저장
                            vacationPeriodProvider.setVacationPeriod(secondMondayOfMonth, thirdFridayofMonth);
                            try {
                                vacationPeriodRepository.saveMonthlyVacationPeriod(
                                        vacationPeriodProvider.createKey(currentYear, currentMonth),
                                        Math.toIntExact(ttlMinutes),
                                        vacationPeriodProvider.createValues(
                                                secondMondayOfMonth.getDayOfMonth(), thirdFridayofMonth.getDayOfMonth()));
                            }catch (JsonProcessingException e) {
                                throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
                            }
                        }
                    
                        private void deleteBeforeTwoYearAttendanceList(){
                            attendancesService.delete();
                        }
                    }
                    
                    ```
                    
                - YearlyScheduler
                    
                    ```java
                    package com.example.backoffice.global.scheduler;
                    
                    import com.example.backoffice.domain.member.service.MembersServiceV1;
                    import lombok.RequiredArgsConstructor;
                    import org.springframework.stereotype.Component;
                    
                    @Component
                    @RequiredArgsConstructor
                    public class YearlyScheduler implements SchedulerTask{
                    
                        private final MembersServiceV1 membersService;
                        @Override
                        public void execute(){
                            updateRemainingVacationDays();
                        }
                    
                        private void updateRemainingVacationDays() {
                            membersService.updateOneForRemainingVacationDays(ScheduledEventType.YEARLY_UPDATE);
                        }
                    
                    }
                    
                    ```
                    
        4. 기대 효과
            1. 업무가 치중되어 있는 관리자의 업무가 자동화되어 전략적 업무에 집중할 수 있게 됨.
            2. 모든 멤버가 일괄적인 업무 처리에 따라 빠른 업무 적응 속도가 기대됨.
    - DB 성능 최적화
        1. 다양한 DB
            1. 문제 상황
                
                기존 인프라는 MySQL만 사용하고 있었으나, 특정 엔티티의 데이터가 수십만 개 이상이 쌓이면서 밑과 같은 문제가 발생함.
                
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
                        
                        ![image.png](image%203.png)
                        
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
                            
                            ![image.png](image%204.png)
                            
                        - 휴가 정정 기간(데이터베이스 2) :
                            
                            ![image.png](image%205.png)
                            
                        - 예정된 근태 기록(데이터베이스 3) :
                            
                            ![image.png](image%206.png)
                            
                    - 코드 첨부 :
                        - UpdateVacationPeriodRepository (휴가 정정 기간 수정 레포지토리)
                            
                            ```java
                            package com.example.backoffice.global.redis;
                            
                            import com.fasterxml.jackson.core.JsonProcessingException;
                            import com.fasterxml.jackson.databind.ObjectMapper;
                            import org.springframework.beans.factory.annotation.Qualifier;
                            import org.springframework.data.redis.core.RedisTemplate;
                            import org.springframework.stereotype.Component;
                            
                            import java.util.concurrent.TimeUnit;
                            
                            @Component
                            public class UpdateVacationPeriodRepository {
                            
                                private final ObjectMapper objectMapper;
                            
                                @Qualifier("redisTemplateForVacationPeriod")
                                private final RedisTemplate<String, Object> redisTemplateForVacationPeriod;
                            
                                public UpdateVacationPeriodRepository (
                                        ObjectMapper objectMapper,
                                        @Qualifier("redisTemplateForVacationPeriod") RedisTemplate<String, Object> redisTemplateForVacationPeriod){
                                    this.objectMapper = objectMapper;
                                    this.redisTemplateForVacationPeriod = redisTemplateForVacationPeriod;
                                }
                            
                                public <T> void saveMonthlyVacationPeriod(
                                        String key, Integer minutes, T value) throws JsonProcessingException{
                                    String valueString = null;
                                    try {
                                        valueString =
                                                !(value instanceof String)
                                                        ? objectMapper.writeValueAsString(value)
                                                        : (String) value;
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException();
                                    }
                            
                                    redisTemplateForVacationPeriod.opsForValue().set(key, objectMapper.writeValueAsString(value));
                                    redisTemplateForVacationPeriod.expire(key, minutes, TimeUnit.MINUTES);
                                }
                            
                                public void deleteVacationPeriod(String key){
                                    redisTemplateForVacationPeriod.delete(key);
                                }
                            
                                public boolean existsByKey(String key) {
                                    return redisTemplateForVacationPeriod.opsForValue().get(key) != null;
                                }
                            
                                public <T> T getValueByKey(String key, Class<T> valueType) {
                                    Object value = redisTemplateForVacationPeriod.opsForValue().get(key);
                            
                                    if (value == null) {
                                        return null;
                                    }
                            
                                    try {
                                        if (value instanceof String) {
                                            // JSON 문자열로 간주하여 역직렬화
                                            return objectMapper.readValue((String) value, valueType);
                                        } else if (valueType.isInstance(value)) {
                                            return valueType.cast(value);
                                        } else {
                                            throw new IllegalArgumentException("The value retrieved from Redis is not compatible with the specified type.");
                                        }
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException("Failed to deserialize value from Redis for key: " + key, e);
                                    }
                                }
                            }
                            
                            ```
                            
                        - UpcomingAttendancesRepository (예정된 외근 기록)
                            
                            ```java
                            package com.example.backoffice.global.redis;
                            
                            import com.example.backoffice.global.common.DateRange;
                            import com.example.backoffice.global.date.DateTimeUtils;
                            import com.example.backoffice.global.exception.GlobalExceptionCode;
                            import com.example.backoffice.global.exception.JsonCustomException;
                            import com.fasterxml.jackson.core.JsonProcessingException;
                            import com.fasterxml.jackson.databind.ObjectMapper;
                            import org.springframework.beans.factory.annotation.Qualifier;
                            import org.springframework.data.redis.core.RedisTemplate;
                            import org.springframework.stereotype.Component;
                            
                            import java.util.*;
                            import java.util.concurrent.TimeUnit;
                            
                            @Component
                            public class UpcomingAttendanceRepository {
                                // database 2 : cachedMemberAttendance
                                private final ObjectMapper objectMapper;
                            
                                private final RedisTemplate<String, Object> redisTemplateForCached;
                            
                                // Long, DateRange
                                public UpcomingAttendanceRepository(
                                        ObjectMapper objectMapper,
                                        @Qualifier("redisTemplateForCachedMemberAttendance") RedisTemplate<String, Object> redisTemplateForCached) {
                                    this.objectMapper = objectMapper;
                                    this.redisTemplateForCached = redisTemplateForCached;
                                }
                            
                                public <T> void saveOne(Long memberId, DateRange value, String description) {
                                    String key = RedisProvider.MEMBER_ID_PREFIX + memberId + ", "+description;
                                    String valueString = serializeValue(value);
                            
                                    Long ttl = DateTimeUtils.calculateMinutesFromTodayToEndDate(value.getEndDate());
                                    redisTemplateForCached.opsForValue().set(key, valueString, ttl, TimeUnit.MINUTES);
                                }
                            
                                public String getRawValue(String key) {
                                    return (String) redisTemplateForCached.opsForValue().get(key);
                                }
                            
                                // 키에 해당하는 value 조회
                                public <T> T getValue(Long memberId, Class<T> valueType) {
                                    String key = RedisProvider.MEMBER_ID_PREFIX + memberId;
                                    String value = (String) redisTemplateForCached.opsForValue().get(key);
                            
                                    if (Objects.isNull(value)) {
                                        return null; // 키가 없을 경우 null 반환
                                    }
                                    try {
                                        // JSON 문자열을 지정된 타입의 객체로 변환하여 반환
                                        return objectMapper.readValue(value, valueType);
                                    } catch (JsonProcessingException e) {
                                        throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
                                    }
                                }
                            
                                public Map<String, String> getAllRawValues() {
                                    Set<String> keys = redisTemplateForCached.keys(
                                            RedisProvider.MEMBER_ID_PREFIX + "*");
                                    Map<String, String> allValues = new HashMap<>();
                            
                                    if (keys != null) {
                                        for (String key : keys) {
                                            String value = getRawValue(key);
                                            allValues.put(key, value);
                                        }
                                    }
                            
                                    return allValues;
                                }
                            
                                // 토큰 삭제
                                public void delete(String key) {
                                    redisTemplateForCached.delete(key);
                                }
                            
                                // Key에서 memberId 추출
                                public Long extractMemberIdFromKey(String key) {
                                    try {
                                        String memberIdPart = key.split(",")[0].split(":")[1].trim();
                                        return Long.valueOf(memberIdPart);
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }
                            
                                // Key에서 description 추출
                                public String extractDescriptionFromKey(String key) {
                                    try {
                                        return key.split(",")[1].trim();
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }
                            
                                public Map<Long, String> getStartDatesByMemberIds(List<Long> memberIdList){
                                    // 모든 예정 근태 데이터 가져오기
                                    Map<String, String> upcomingAttendanceMap = getAllRawValues();
                                    Map<Long, String> memberStartDateMap = new HashMap<>();
                            
                                    for (Map.Entry<String, String> entry : upcomingAttendanceMap.entrySet()) {
                                        String key = entry.getKey();  // 예: "memberId:3, 상하이 외근"
                                        String value = entry.getValue(); // 예: "{\"startDate\":\"2025-03-07 09:00:00\",\"endDate\":\"2025-03-14 18:00:00\"}"
                            
                                        // JSON을 객체로 변환
                                        DateRange attendanceData = deserializeValue(value, DateRange.class);
                            
                                        // key에서 memberId 추출
                                        Long memberId = extractMemberIdFromKey(key);
                            
                                        // memberId가 리스트에 포함된 경우만 처리
                                        if (memberId != null && memberIdList.contains(memberId)) {
                                            memberStartDateMap.put(memberId, attendanceData.getStartDate().toString());
                                        }
                                    }
                            
                                    return memberStartDateMap;
                                }
                            
                                // JSON 직렬화
                                private String serializeValue(DateRange value) {
                                    try {
                                        return objectMapper.writeValueAsString(value);
                                    } catch (JsonProcessingException e) {
                                        throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
                                    }
                                }
                            
                                // JSON 역직렬화
                                public <T> T deserializeValue(String value, Class<T> valueType) {
                                    try {
                                        return objectMapper.readValue(value, valueType);
                                    } catch (JsonProcessingException e) {
                                        throw new JsonCustomException(GlobalExceptionCode.NOT_DESERIALIZED_JSON);
                                    }
                                }
                            }
                            
                            ```
                            
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
                            
                            ![image.png](image%207.png)
                            
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
                
                [DB 성능 최적화](https://www.notion.so/DB-1a3793fd0cc88079956fd1e878ea29cf?pvs=21)
