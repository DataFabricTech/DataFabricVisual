Keycloak
========
해당 폴더는 Keycloak 예시 폴더

예시 목록
- Client/사용자/사용자 역할/그룹 추가
- Token 정보 수정(필요한 정보 추가)
- Custom 로그인/로케일설정

Keycloak 실행방법
========

To get help configuring Keycloak via the CLI, run:

on Linux/Unix:

    $ bin/kc.sh

on Windows:

    $ bin\kc.bat

To try Keycloak out in development mode, run: 

on Linux/Unix:

    $ bin/kc.sh start-dev

on Windows:

    $ bin\kc.bat start-dev

After the server boots, open http://localhost:8080 in your web browser. The welcome page will indicate that the server is running.

To get started, check out the [configuration guides](https://www.keycloak.org/guides#server).
