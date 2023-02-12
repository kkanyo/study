import { useState } from "react";

function App() {
  const [toDo, setToDo] = useState("");
  const [toDos, setToDos] = useState([]);
  const onChange = (event) => setToDo(event.target.value);
  const onSubmit = (event) => {
    event.preventDefault();
    if (toDo === "") {
      return;
    }
    setToDo("");
    setToDos((currentArray) => [toDo, ...currentArray]);
  };
  console.log(toDos);
  console.log(
    // map: 각 element를 전달받아 새로운 값을 반환
    toDos.map((item, index) => (
      // 각 item을 리스트로 만들어 반환
      <li key={index}>{item}</li>
    ))
  );
  return (
    <div>
      <h1>My To Dos ({toDos.length})</h1>
      <form onSubmit={onSubmit}>
        <input
          onChange={onChange}
          value={toDo}
          type="text"
          placeholder="Write your to do..."
        />
        <button>Add To Do</button>
      </form>
      <hr />
      <ur>
        {toDos.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ur>
    </div>
  );
}

export default App;
