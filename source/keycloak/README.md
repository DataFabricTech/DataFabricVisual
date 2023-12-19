Keycloak
========
해당 폴더는 Keycloak 예시 폴더

예시 목록
- Client/사용자/사용자 역할/그룹 추가
- Token 정보 수정(필요한 정보 추가)
- Custom 로그인/로케일설정

Keycloak 실행방법
========

Keycloak를 구성하는 도움말을 보려면 다음 실행

on Linux/Unix:

    $ bin/kc.sh

on Windows:

    $ bin\kc.bat

개발 모드로 실행 

on Linux/Unix:

    $ bin/kc.sh start-dev

on Windows:

    $ bin\kc.bat start-dev


서버 부팅된 후 별도 포트 설정을 하지 않았다면 웹 브라우저에서 http://localhost:8080 오픈.

Keycloak 서버 구성 시 해당 문서 확인 필요 [configuration guides](https://www.keycloak.org/guides#server).
