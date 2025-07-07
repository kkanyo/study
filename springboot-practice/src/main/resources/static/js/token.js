const token = searchParam('token')

// 파라미터로 받은 토큰이 있다면 토큰을 로컬 스토리지에 저장
if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}