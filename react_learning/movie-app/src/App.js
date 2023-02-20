import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Detail from "./routes/Detail";
import Home from "./routes/Home";

// 스크린 단위로 분기해주는 역할
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/movie" element={<Detail />} />
      </Routes>
    </Router>
  );
}

export default App;
