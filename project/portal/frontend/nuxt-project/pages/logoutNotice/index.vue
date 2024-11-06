<template>
  <div class="container-page">
    <div class="container">
      <!-- 로고 부분 -->
      <div class="logo">
        <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI1MzIuMjkwNjQ3IiBoZWlnaHQ9IjE4Ny44NjcyODciIHZpZXdCb3g9IjAgMCAxMy42MTgyNzkgNC43NjQ2MTk2Ij48cGF0aCBmaWxsPSIjMjM0NzgxIiBkPSJNNi42OCA0Yy0uMDA4LjExLS4wMzkuMjAxLS4xNjMuMjAxLS4xMjYgMC0uMTU2LS4wOTItLjE2Ni0uMlYyLjg1NGMwLS4xMjYuMDExLS4yNy4xNjYtLjI3LjE1NCAwIC4xNjQuMTQ0LjE2NC4yN3ptLS4xNi0xLjg0MmMtLjUxMyAwLS44NjcuMTc0LS44NjcuNzk5di45NTJjMCAuNTQuMzkyLjcyMy44NjcuNzIzLjUwNSAwIC44MzMtLjE4NS44NjgtLjcyM3YtLjk1MmMwLS41NS0uMjg1LS44LS44NjgtLjhNNy40OCAyLjE4M3YuNDRoLjI0N1Y0LjE1YzAgLjM4Mi4yMS40NTUuNTguNDU1LjE1IDAgLjI1IDAgLjQzNS0uMDEzVjQuMTdIOC41N2MtLjA5IDAtLjE1LS4wNTMtLjE1LS4xNzNWMi42MjNoLjMyMnYtLjQ0TTEzLjAxIDIuMTUzYy0uMjcyIDAtLjQyNy4xNC0uNDk3LjM0MmgtLjAwOGMtLjA1NC0uMTg5LS4xOTItLjM0Mi0uNDY4LS4zNDItLjI1NCAwLS40MTMuMTMyLS41MTUuMzU0aC0uMDA4di0uMzIyaC0uNjU4djIuNDE4aC42NTZ2LTEuNzFjLjAwOS0uMTIyLjA0My0uMTk4LjE3NC0uMTk4LjEyMiAwIC4xNTguMDkyLjE1OC4xOTd2MS43MTFoLjY1NXYtMS43MWMwLS4xMTguMDUxLS4xOTguMTY1LS4xOTguMTI0IDAgLjE1OS4wOTQuMTU5LjE5N3YxLjcxMWguNjYzVjIuNjE3YzAtLjMwNS0uMi0uNDY0LS40NzYtLjQ2NE05Ljg4NiAzLjA2NWgtLjMyOHYtLjE2MmMwLS4xNjMtLjAxOC0uMzEzLjE2My0uMzEzLjE2NyAwIC4xNjUuMTYuMTY1LjI0MnptLS4xNjktLjkxMmMtLjQ5Ni4wMDItLjg2NC4xNy0uODY0Ljc2OVYzLjkzYzAgLjUzMy40MTcuNjk3Ljg2NC42OTcuNTY5IDAgLjg2NS0uMTk1Ljg2NS0uNjI3VjMuNjVoLS42OTd2LjI2M2MwIC4xNDEtLjAxOC4yOC0uMTY0LjI4LS4xMzggMC0uMTYyLS4xNDktLjE2Mi0uMjc4di0uNDQ5aDEuMDI0di0uNjM4YzAtLjUwMy0uMzkxLS42NzYtLjg2Ni0uNjc0TTQuNDY3IDIuOTA1aC0uMTA1di0uNzI3bC4wOS4wMDFjLjIyNCAwIC4yOTIuMTE0LjI5Mi4zNzYgMCAuMTk0LS4wNzUuMzUtLjI3Ny4zNW0uOTcyLS40NTFjMC0uNTYxLS4zNjctLjY3OS0uODQ4LS42NzloLS45M3YyLjgyOGguNzAxVjMuMzFINC41Yy4xNjMuMDEyLjIyOS4xMTguMjM5LjMwNC4wMDIuMDMuMDAzLjA2Ni4wMDMuMTAzdi44ODZoLjY5NXYtLjg4NmEzLjIxIDMuMjEgMCAwIDAtLjAwOC0uMjQ1Yy0uMDEyLS4xOTYtLjE1LS4zNzYtLjUzNC0uMzl2LS4wMDdjLjQxMi0uMDcyLjU0NS0uMzIuNTQ1LS42MjEiIHN0eWxlPSJzdHJva2Utd2lkdGg6LjM1Mjc3ODtmaWxsOiMwYTIzNjk7ZmlsbC1vcGFjaXR5OjEiLz48cGF0aCBmaWxsPSIjZDkyNjI4IiBkPSJtOS4xMTkuNjk2LTEuMzkyLjY4OXYuNjgybDEuMzkyLS42OSIgc3R5bGU9InN0cm9rZS13aWR0aDouMzUyNzc4Ii8+PHBhdGggZmlsbD0iI2Q5MjYyOCIgZD0ibTkuMTE5LjY5Ni0xLjM5Mi42ODl2LjY4MmwxLjM5Mi0uNjkiIHN0eWxlPSJzdHJva2Utd2lkdGg6LjM1Mjc3ODtmaWxsOnJlZDtmaWxsLW9wYWNpdHk6MSIvPjxnIGZpbGw9IiMyMzQ3ODEiIHN0eWxlPSJmaWxsOiMwYTIzNjk7ZmlsbC1vcGFjaXR5OjEiPjxwYXRoIGQ9Ik0wIDBoLTEuNDUzdjEuMjE0aC0uNjgxdi0zLjExMWguNjgxdjEuMjlIMHYtMS4yOWguNjgydjMuMTExSDAiIHN0eWxlPSJmaWxsOiMwYTIzNjk7ZmlsbC1vcGFjaXR5OjEiIHRyYW5zZm9ybT0ibWF0cml4KC4zNTI3OCAwIDAgLS4zNTI3OCAuODg1IC41NikiLz48cGF0aCBkPSJNMCAwaDEuMjc0di0uOTAzSC0uMTc0di43MjZjLjAwMy4wMzUtLjAwNC4wOTkuMDI3LjEzOS4wMzIuMDM5LjA4My4wMzYuMTQ3LjAzOG0tLjcwMi40MzFDLS44NDUuMjg0LS44NS4wNy0uODU1LS4wNTR2LTIuNDQ5aC42ODF2Ljk5M2gxLjQ0OHYtLjk5M2guNjgydjMuMTFILS4xNjlDLS4zNzEuNjA2LS41Ni41NzctLjcwMi40MzEiIHN0eWxlPSJmaWxsOiMwYTIzNjk7ZmlsbC1vcGFjaXR5OjEiIHRyYW5zZm9ybT0ibWF0cml4KC4zNTI3OCAwIDAgLS4zNTI3OCA2LjM1OCAuMzQ3KSIvPjxwYXRoIGQ9Ik0wIDBoLTIuMTN2LTMuMTExaC42ODJ2Mi41MDRoMS4yNzljLjA2NC0uMDAyLjExNi4wMDEuMTQ3LS4wMzkuMDMtLjAzOS4wMjUtLjEwNC4wMjYtLjEzOXYtMi4zMjZoLjY4MnYyLjQ1Yy0uMDAzLjEyMy0uMDA4LjMzOC0uMTUxLjQ4NEMuMzkxLS4wMzEuMjAzLS4wMDEgMCAwIiBzdHlsZT0iZmlsbDojMGEyMzY5O2ZpbGwtb3BhY2l0eToxIiB0cmFuc2Zvcm09Im1hdHJpeCguMzUyNzggMCAwIC0uMzUyNzggNC40MDggLjEzMikiLz48cGF0aCBkPSJNMCAwYy0uMDAzLS4wMzYuMDAzLS4wOTktLjAyNi0uMTM4LS4wMzMtLjA0MS0uMDg1LS4wMzctLjE0Ny0uMDRoLTEuMjh2Mi41MDNoLS42ODJWLS43ODZoMi4xM2MuMjAzLjAwMi4zOTIuMDMyLjUzMy4xNzguMTQ0LjE0Ni4xNDkuMzYuMTUzLjQ4NHYyLjQ0OUgwIiBzdHlsZT0iZmlsbDojMGEyMzY5O2ZpbGwtb3BhY2l0eToxIiB0cmFuc2Zvcm09Im1hdHJpeCguMzUyNzggMCAwIC0uMzUyNzggMy4yMDQgLjk1MikiLz48cGF0aCBkPSJNMCAwYy0uMDAyLS4wMzYuMDA0LS4wOTktLjAyNi0uMTM4LS4wMzEtLjA0MS0uMDg0LS4wMzctLjE0OC0uMDM5aC0xLjI4djEuODk1aDEuMjhjLjA2NC0uMDAyLjExNyAwIC4xNDgtLjAzOS4wMy0uMDM5LjAyNC0uMTA0LjAyNi0uMTM5Wm0uNjggMS42NjNjLS4wMDMuMTI1LS4wMS4zMzgtLjE1My40ODQtLjE0Mi4xNDctLjMzMS4xNzctLjUzMy4xNzhoLTIuMTI5Vi0uNzg2aDIuMTI5Yy4yMDIuMDAxLjM5MS4wMzEuNTMzLjE3OC4xNDMuMTQ2LjE1LjM1OS4xNTMuNDg1IiBzdHlsZT0iZmlsbDojMGEyMzY5O2ZpbGwtb3BhY2l0eToxIiB0cmFuc2Zvcm09Im1hdHJpeCguMzUyNzggMCAwIC0uMzUyNzggNS42MTcgLjk1MikiLz48cGF0aCBkPSJtMCAwLS44MjcgMS4zNjFoLS44MUwtLjM2NC0uNnYtMS4xNWguNzI4Vi0uNmwxLjI3MyAxLjk2MWgtLjgxIiBzdHlsZT0iZmlsbDojMGEyMzY5O2ZpbGwtb3BhY2l0eToxIiB0cmFuc2Zvcm09Im1hdHJpeCguMzUyNzggMCAwIC0uMzUyNzggMS43ODggLjYxMikiLz48cGF0aCBkPSJNMTI0LjU4OSAxNjUuNjkxaC4yNTZ2LTEuMDk3aC0uMjU2eiIgc3R5bGU9ImZpbGw6IzBhMjM2OTtmaWxsLW9wYWNpdHk6MSIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTExNy4zIC0xNjQuNDYyKSIvPjwvZz48L3N2Zz4=" alt="Hyundai Rotem Logo" />
        <hr>
      </div>

      <!-- 로그인 안내 문구 -->
      <div class="login-info">
        <h1>로그인 유지 시간이 만료되어 <br>자동으로 <span>로그아웃</span> 되었습니다.</h1><br><br>
        <p>
          데이터 포털 서비스를 이용해 주셔서 감사합니다.<br />
          로그인이 필요한 경우 그룹웨어 나의 업무에서 <span>데이터포털</span> 버튼을 클릭하세요.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'index',
};

onMounted(async () => {
  // access-token이 있는 경우 메인화면으로 이동 처리
  if (cookies.get('x-access-token') && cookies.get('x-refresh-token'))
    router.push('/main')
  else {
    sessionStorage.clear();
  }
});

</script>

<style scoped>
.container-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f9f9f9;
}


.container {
  text-align: center;
  min-width: 800px;
  max-width: 1200px;
  min-height: 500px;
  padding: 2rem;
  background-color: #fff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.logo {
  text-align: left;
  margin-bottom: 1rem;
}

.logo img {
  max-width: 200px; /* 로고 크기 조절 */
}

.login-info {
  margin-top: 40px;
}

.login-info h1 {
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
}

.login-info h1 span {
  font-size: 2.5rem;
  color: #0056b3
}

.login-info a:hover {
  text-decoration: underline;
}

.login-info p {
  font-size: 1.5rem;
}

.login-info p span {
  font-size: 1.5rem;
  color: #0056b3
}

</style>
